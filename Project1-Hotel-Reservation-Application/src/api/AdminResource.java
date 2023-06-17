package api;

import model.Customer;
import model.IRoom;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.List;

public class AdminResource {

    private final CustomerService _customerService = CustomerService.getInstance();
    private final ReservationService _reservationService = ReservationService.getInstance();

    private static AdminResource _AdminResource;
    private AdminResource() {
    }

    public static AdminResource getInstance() {
        if(_AdminResource == null) {
            _AdminResource = new AdminResource();
        }

        return _AdminResource;
    }

    public Customer getCustomer(String email)
    {
        return _customerService.getCustomer(email);
    }

    public void addRoom(IRoom room)
    {
        Collection<IRoom> rooms = _reservationService.getAllRoom();
        for (IRoom a: rooms) {
            if(a.getRoomNumber().equals(room.getRoomNumber())) {
                throw new IllegalArgumentException("RoomID exited please choose other id");
            }
        }
        _reservationService.addRoom(room);
    }

    public Collection<IRoom> getAllRooms()
    {
        return _reservationService.getAllRoom();
    }

    public Collection<Customer> getAllCustomers()
    {
        return _customerService.getAllCustomers();
    }

    public void displayAllReservations()
    {
        _reservationService.printAllReservation();
    }
}
