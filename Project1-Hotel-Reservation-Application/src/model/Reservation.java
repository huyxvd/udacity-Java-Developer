package model;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Reservation {
    private Customer customer;
    public IRoom room;

    private final Date checkInDate;
    private final Date checkOutDate;

    public Date getCheckInDate() {
        return checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate) {
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public String getCustomerEmail() {
        return customer.getCustomerEmail();
    }

    @Override
    public String toString() {
        return String.format("this is reservation object");
    }

    public void displayInfo() {
        System.out.println("---------------------------");
        customer.displayInfo();
        System.out.println("checkInDate: " + checkInDate);
        System.out.println("checkOutDate: " + checkOutDate);
        System.out.println("roomNumber: " + room.getRoomNumber());
        long differenceInMillis = checkOutDate.getTime() - checkInDate.getTime();

        long differenceInDays = TimeUnit.MILLISECONDS.toDays(differenceInMillis);
        System.out.println("total: " + differenceInDays * room.getRoomPrice());
        System.out.println("---------------------------");
    }

}
