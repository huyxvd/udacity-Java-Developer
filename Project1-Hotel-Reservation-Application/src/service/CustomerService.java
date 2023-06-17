package service;

import model.Customer;
import model.Reservation;

import java.util.ArrayList;
import java.util.Collection;

public class CustomerService {
    private static CustomerService CustomerServiceInstance;
    private CustomerService() {
    }
    
    public static CustomerService getInstance() {
        if (CustomerServiceInstance == null) {
            CustomerServiceInstance = new CustomerService();
        }

        return CustomerServiceInstance;
    }
    
    static Collection<Customer> customers = new ArrayList<Customer>();
    
    public void addCustomer(String email, String firstName, String lastName) {
        Customer newCustomer = new Customer(firstName, lastName, email);
        customers.add(newCustomer);
    }
    public Customer getCustomer(String customerEmail)
    {

        for (Customer customer: customers) {
            if(customer.getCustomerEmail().equals(customerEmail)) {
                return customer;
            }
        }
        return null;
    }

    public Collection<Customer> getAllCustomers() {
        return customers;
    }
}
