package service;

import model.Customer;
import model.Reservation;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerService {
    static Collection<Customer> customers= new ArrayList<Customer>();
    public static void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        customers.add(newCustomer);
    }
    public static Customer getCustomer(String customerEmail)
    {

        for (Customer customer: customers) {
            if(customer.getCustomerEmail().equals(customerEmail)) {
                return customer;
            }
        }
        return null;
    }

    public static Collection<Customer> getAllCustomers() {
        return customers;
    }
}
