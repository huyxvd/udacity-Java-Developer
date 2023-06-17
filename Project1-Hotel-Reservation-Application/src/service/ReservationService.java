package service;

import model.Customer;
import model.IRoom;
import model.Reservation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class ReservationService {

    public static Collection<Reservation> reservations = new ArrayList<Reservation>();
    public static Collection<IRoom> rooms = new ArrayList<IRoom>();
    public static void addRoom(IRoom room)
    {
        rooms.add(room);
    }

    public static IRoom getARoom(String roomId)
    {
        for (IRoom item : rooms) {
            if (item.getRoomNumber().equals(roomId)) {
                return item;
            }
        }
        return null;
    }

    public static Collection<IRoom> getAllRoom()
    {
        return rooms;
    }

    public static Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate)
    {
        var reservation =  new Reservation(customer, room, checkInDate, checkOutDate);
        reservations.add(reservation);
        return reservation;
    }

    public static Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate)
    {
        // get all roomId is already Reserved
        ArrayList<String> roomReserved = new ArrayList<>();
        for (Reservation element : reservations ) {
            int a = checkOutDate.compareTo(element.getCheckInDate());
            int b = checkInDate.compareTo(element.getCheckOutDate());
            if(a > 0 && b < 0) {
                roomReserved.add(element.room.getRoomNumber());
            }
        }

        // filter all the room that avaialbe tha
        ArrayList<IRoom> roomAvailable = new ArrayList<>();
        for (IRoom room: rooms ) {
            if(!roomReserved.contains(room.getRoomNumber())) {
                roomAvailable.add(room);
            }
        }
        return roomAvailable;
    }

    public static Collection<Reservation> getCustomersReservation(String customerEmail)
    {
        Collection<Reservation> sameCustomer = new ArrayList<>();
        for (Reservation element : reservations ) {
            if(element.getCustomerEmail().equals(customerEmail)) {
                sameCustomer.add(element);
            }
        }
        return sameCustomer;
    }

    public static void printAllReservation()
    {
        for (Reservation element : reservations ) {
            element.displayInfo();
        }
    }
}
