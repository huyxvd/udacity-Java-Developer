package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    private final CustomerService _customerService = CustomerService.getInstance();
    private final ReservationService _reservationService = ReservationService.getInstance();

    private static HotelResource _HotelResource;
    private HotelResource() {
    }

    public static HotelResource getInstance() {
        if(_HotelResource == null) {
            _HotelResource = new HotelResource();
        }

        return _HotelResource;
    }

    public Customer getCustomer(String email) {
        return  _HotelResource.getCustomer(email);
    }

    public void createACustomer(String  email,  String firstName, String lastName) {
        _customerService.addCustomer(email, firstName, lastName);
    }

    public IRoom getRoom(String roomNumber)
    {
        return _reservationService.getARoom(roomNumber);
    }

    public Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date CheckOutDate)
    {
        Customer customer = _customerService.getCustomer(customerEmail);


        return _reservationService.reserveARoom(customer, room, checkInDate, CheckOutDate);
    }

    public Collection<Reservation> getCustomersReservations(String customerEmail)
    {
        return _reservationService.getCustomersReservation(customerEmail);
    }

    public Collection<IRoom> findARoom(Date checkIn, Date checkOut)
    {
        return _reservationService.findRooms(checkIn, checkOut);
    }
}
