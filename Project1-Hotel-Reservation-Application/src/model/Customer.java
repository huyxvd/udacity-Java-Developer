package model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Customer {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+$";
    private final String firstName;
    private final String lastName;
    private final String email;

    public Customer(String firstName, String lastName, String email) {

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if(!matcher.matches()) {
            throw new IllegalArgumentException("email is not correct format");
        }
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
    public void displayInfo() {
        System.out.println("firstName: " + firstName);
        System.out.println("lastName: " + lastName);
        System.out.println("email: " + email);
        System.out.println("---------------------------");
    }

    public String getCustomerEmail() {
        return this.email;
    }

    @Override
    public String toString() {
        return String.format("customer override");
    }
}
