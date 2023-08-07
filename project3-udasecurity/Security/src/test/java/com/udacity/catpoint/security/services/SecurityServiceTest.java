package com.udacity.catpoint.security.services;

import com.udacity.catpoint.image.ImageService;
import com.udacity.catpoint.security.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.image.BufferedImage;
import java.util.*;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SecurityServiceTest {
    private SecurityService securityService;
    private Sensor sensor;
    private Set<Sensor> sensors;

    @Mock
    private SecurityRepository securityRepository;
    @Mock
    private ImageService imageService;

    @BeforeEach
    void init() {
        securityService = new SecurityService(this.securityRepository, this.imageService);

        List<SensorType> sensorTypes = new ArrayList<>();
        sensorTypes.add(SensorType.DOOR);
        sensorTypes.add(SensorType.WINDOW);
        sensorTypes.add(SensorType.MOTION);



        sensors = new HashSet<>();
        for (SensorType sensorType: sensorTypes) {
            String sensorName = "sensor" + sensorType.toString();
            sensors.add(new Sensor(sensorName, sensorType));
        }

        sensor = sensors.stream().findFirst().orElse(null);
    }

    // 1. If alarm is armed and a sensor becomes activated, put the system into pending alarm status
    @Test
    @DisplayName("[1] Armed Alarm, activated Sensor -> PENDING ALARM")
    public void ArmedAlarm_ActivatedSensor_SetPendingAlarmStatus() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.NO_ALARM);
        securityService.changeSensorActivationStatus(sensor, true);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.PENDING_ALARM);
    }

    // 2. If alarm is armed and a sensor becomes activated and the system is already pending alarm,
    // set the alarm status to alarm.
    @ParameterizedTest
    @DisplayName("[2] Armed Alarm, activated Sensor, already PENDING ALARM -> ALARM")
    @EnumSource(value = ArmingStatus.class, names= {"ARMED_HOME", "ARMED_AWAY"})
    public void setArmedAlarm_ActivatedSensor_SetFromPendingAlarmToAlarm(ArmingStatus armingStatus) {
        when(securityRepository.getArmingStatus()).thenReturn(armingStatus);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
        securityService.changeSensorActivationStatus(sensor, true);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
    }

    // 3. If pending alarm and all sensors are inactive, return to no alarm state.
    @ParameterizedTest
    @DisplayName("[3] Pending Alarm, inactivate all sensors -> NO ALARM")
    @EnumSource(SensorType.class)
    public void setPendingAlarm_InactivateAllSensors_NoAlarmState (SensorType sensorType) {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
        sensor.setActive(true);
        securityService.changeSensorActivationStatus(sensor, false);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    // 4. If alarm is active, change in sensor state should not affect the alarm state.
    @ParameterizedTest
    @DisplayName("[4] Active Alarm, Changed Sensor State -> no affect to the alarm state")
    @ValueSource(booleans = {true, false})
    public void setAlarmActive_ChangeSensorState_NotAffectAlarmState(boolean activate) {
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);
        securityService.changeSensorActivationStatus(sensor, activate);
        verify(securityRepository, never()).setAlarmStatus(any(AlarmStatus.class));
    }

    // 5. If a sensor is activated while already active and the system is in pending state,
    // change it to alarm state.
    @Test
    @DisplayName("[5] Activated Sensor, Pending State -> ALARM")
    public void ActivatedSensorWhileAlreadyActivate_PendingAlarm_SetALARM() {
        sensor.setActive(true);
        Sensor anotherSensor = sensors.stream().findFirst().orElse(null);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.PENDING_ALARM);
        securityService.changeSensorActivationStatus(anotherSensor, true);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
    }

    // 6. If a sensor is deactivated while already inactive,
    // make no changes to the alarm state.
    @Test
    @DisplayName("[6] If a sensor is deactivated while already inactive, " +
            "make no changes to the alarm state.")
    public void DeactivatedSensorWhileAlreadyInactivate_NoChangeAlarmSate (){
        sensor.setActive(false);
        securityService.changeSensorActivationStatus(sensor, false);
        verify(securityRepository, never()).setAlarmStatus(any(AlarmStatus.class));
    }

    // 7. If the image service identifies an image containing a cat while the system is armed-home,
    // put the system into alarm status.
    @Test
    @DisplayName("[7] Cat Identification, Armed Home -> ALARM")
    public void imageCatIdentification_WhileArmedHome_SetAlarm() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(imageService.imageContainsCat(any(), anyFloat())).thenReturn(true);
        securityService.processImage(mock(BufferedImage.class));
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
    }

    // 8. If the image service identifies an image that does not contain a cat,
    // change the status to no alarm as long as the sensors are not active.
    @Test
    @DisplayName("[8] No Cat in image -> No Alarm, Not Active Sensors")
    public void imageNoCat_ChangeToNoAlarm_NotActiveSensors() {
        sensors.forEach(s -> s.setActive(false));
        when(imageService.imageContainsCat(any(), anyFloat())).thenReturn(false);
        securityService.processImage(mock(BufferedImage.class));
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    // 9. If the system is disarmed, set the status to no alarm.
    @Test
    @DisplayName("[9] Disarmed System -> No ALARM")
    public void systemDisarmed_AlarmStatusSetToNoAlarm()
    {
        securityService.setArmingStatus(ArmingStatus.DISARMED);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.NO_ALARM);
    }

    // 10. If the system is armed, reset all sensors to inactive.
    @ParameterizedTest
    @DisplayName("[10] Armed System -> All Inactive Sensors")
    @EnumSource(value = ArmingStatus.class, names= {"ARMED_HOME", "ARMED_AWAY"})
    public void ArmedSystem_ResetAllSensorsInacive(ArmingStatus armingStatus) {
        sensors.forEach(s -> s.setActive(true));
        when(securityRepository.getSensors()).thenReturn(sensors);
        securityService.setArmingStatus(armingStatus);
        Set<Boolean> sensorStatus = new HashSet<>();
        sensors.forEach(s -> sensorStatus.add(s.getActive()));
        Set<Boolean> expected = new HashSet<>();
        expected.add(false);
        Assertions.assertEquals(sensorStatus, expected);
    }

    // 11. If the system is armed-home while the camera shows a cat, set the alarm status to alarm.
    @Test
    @DisplayName("[11] Armed-home System, Cat Identification -> ALARM")
    public void ArmedHome_CatDetect_SetAlarmStatus(){
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.ARMED_HOME);
        when(imageService.imageContainsCat(any(), anyFloat())).thenReturn(true);
        securityService.processImage(mock(BufferedImage.class));
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.ALARM);
    }

    // Others

    // If a sensor becomes activated and the system is already alarm,
    // set the alarm status to pending alarm.
    @Test
    public void activatedSensor_disarmedHomeAndAlarm_setPendingAlarmAndDeactivateSensor() {
        when(securityRepository.getArmingStatus()).thenReturn(ArmingStatus.DISARMED);
        when(securityRepository.getAlarmStatus()).thenReturn(AlarmStatus.ALARM);
        sensor.setActive(true);
        securityService.changeSensorActivationStatus(sensor, false);
        verify(securityRepository, times(1)).setAlarmStatus(AlarmStatus.PENDING_ALARM);
    }
}
