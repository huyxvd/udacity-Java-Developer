package api;

import model.Customer;
import model.IRoom;
import model.Reservation;
import service.CustomerService;
import service.ReservationService;

import java.util.Collection;
import java.util.Date;

public class HotelResource {
    public static Customer getCustomer(String email) {
        return  null;
    }

    public static void createACustomer(String  email,  String firstName, String lastName) {
        CustomerService.addCustomer(email, firstName, lastName);
    }

    public static IRoom getRoom(String roomNumber)
    {

        return ReservationService.getARoom(roomNumber);
    }

    public static Reservation bookARoom(String customerEmail, IRoom room, Date checkInDate, Date CheckOutDate)
    {
        Customer customer = CustomerService.getCustomer(customerEmail);
        return ReservationService.reserveARoom(customer, room, checkInDate, CheckOutDate);
    }

    public static Collection<Reservation> getCustomersReservations(String customerEmail)
    {
        return ReservationService.getCustomersReservation(customerEmail);
    }

    public static Collection<IRoom> findARoom(Date checkIn, Date checkOut)
    {
        return ReservationService.findRooms(checkIn, checkOut);
    }
}
