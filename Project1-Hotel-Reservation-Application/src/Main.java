import api.AdminResource;
import api.HotelResource;
import model.*;
import service.CustomerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    private static void DisplayAdminMenu()
    {
        System.out.println("Admin Menu:");
        System.out.println("------------------------------------");
        System.out.println("1.  See all Customers");
        System.out.println("2.  See all Rooms");
        System.out.println("3.  See all Reservations");
        System.out.println("4.  Add a Room");
        System.out.println("5.  Fake data");
        System.out.println("6.  back to main menu");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }

    private static void InputRoom() {
        Scanner scanner = new Scanner(System.in);
        ArrayList<IRoom> rooms = new ArrayList<IRoom>();

        boolean addMoreRooms = true;

        while (addMoreRooms) {
            System.out.print("Enter room number: ");
            String roomNumber = scanner.nextLine();

            System.out.print("Enter price: ");
            double price = scanner.nextDouble();
            scanner.nextLine(); // Consume the newline character

            System.out.print("Enter room type (1 - SINGLE, 2 or any - DOUBLE,): ");
            String roomTypeString = scanner.nextLine();
            int roomT = Integer.parseInt(roomTypeString);

            RoomType roomType;
            if(roomT == 1) {
                roomType = RoomType.SINGLE;
            } else {

                roomType = RoomType.SINGLE;
            }

            // Create an instance of the Room class with the entered information
            Room room = new Room(roomNumber, price, roomType);
            //.add(room);
            AdminResource _AdminResource = AdminResource.getInstance();
            _AdminResource.addRoom(room);

            System.out.print("Add another room? (y/n): ");
            String addAnotherRoom = scanner.nextLine();
            addMoreRooms = addAnotherRoom.equalsIgnoreCase("y");
        }
    }

    private static void AdminMenu() {

        AdminResource _AdminResource = AdminResource.getInstance();
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            try {
                DisplayAdminMenu();
                option = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid option number.");
                scanner.nextLine(); // Clear the invalid input from the scanner
                option = 0; // Set option to 0 to continue the loop
            }

            switch (option) {
                case 1:
                    // Perform actions for Option 1
                    System.out.println("-----------See all Customers---------------");
                    for (Customer element : _AdminResource.getAllCustomers()) {
                        element.displayInfo();
                    }
                    break;
                case 2:
                    // Perform actions for Option 2
                    System.out.println("-----------See all Rooms---------------");
                    for (IRoom element : _AdminResource.getAllRooms()) {
                        element.displayInfo();
                    }
                    break;
                case 3:
                    // Perform actions for Option 3
                    System.out.println("-----------See all Reservations---------------");
                    _AdminResource.displayAllReservations();
                    break;
                case 4:
                    System.out.println("4.  Add a Room");
                    // Perform actions for Option 4
                    InputRoom();
                    break;
                case 5:
                    System.out.println("5.  fake some room");
                    try{
                        FakeData();
                    } catch (Exception ex) {

                    }
                    break;
                case 6:
                    System.out.println("6.  Back to Main Menu");
                    break;
                default:
                    System.out.println("Invalid option! Please choose a valid option.");
                    break;
            }

            System.out.println();
        } while (option != 6);

        System.out.println("Admin menu exited.");
    }

    private  static  void FakeData() throws ParseException {
        AdminResource _AdminResource = AdminResource.getInstance();
        Room room1 = new Room("101", 100.0, RoomType.SINGLE);
        Room room2 = new Room("102", 150.0, RoomType.DOUBLE);
        Room room3 = new Room("103", 250.0, RoomType.DOUBLE);
        Room room4 = new Room("104", 350.0, RoomType.DOUBLE);
        _AdminResource.addRoom(room1);
        _AdminResource.addRoom(room2);
        _AdminResource.addRoom(room3);
        _AdminResource.addRoom(room4);

        HotelResource _HotelResource = HotelResource.getInstance();
        _HotelResource.createACustomer("john.doe@example.com", "John", "Doe");
        _HotelResource.createACustomer("alice.smith@example.com", "Alice", "Smith");
        _HotelResource.createACustomer("bob.johnson@example.com", "Bob", "Johnson");
        _HotelResource.createACustomer("emily.davis@example.com", "Emily", "Davis");
        _HotelResource.createACustomer("michael.brown@example.com", "Michael", "Brown");


        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            _HotelResource.bookARoom("john.doe@example.com", room1, dateFormat.parse("1-1-2000"), dateFormat.parse("10-1-2000") );
            _HotelResource.bookARoom("john.doe@example.com", room2, dateFormat.parse("1-1-2000"), dateFormat.parse("10-1-2000") );
            Date in = dateFormat.parse("5-1-2000");
            Date out = dateFormat.parse("6-1-2000");
            Collection<IRoom> rooms = _HotelResource.findARoom( in, out );

            for (IRoom room: rooms) {
                room.displayInfo();
            }
        } catch (Exception a) {

        }
    }
    private static void MainMenu(int option) {
        HotelResource _HotelResource = HotelResource.getInstance();
        Scanner scanner = new Scanner(System.in);
        switch (option) {
            case 1:
                System.out.println("------------------Find and reserve a room------------------");

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

                System.out.print("Enter check-in date (dd-MM-yyyy): ");
                String checkInDateStr = scanner.nextLine();

                System.out.print("Enter check-out date (dd-MM-yyyy): ");
                String checkOutDateStr = scanner.nextLine();
                Date checkInDate = new Date();
                Date checkOutDate = new Date();

                try {
                    checkInDate = dateFormat.parse(checkInDateStr);
                    checkOutDate = dateFormat.parse(checkOutDateStr);

                    Collection <IRoom> rooms = _HotelResource.findARoom(checkInDate, checkOutDate);

                    if(rooms.isEmpty()) {
                        System.out.println("no room available");
                        break;
                    }

                    for (IRoom room: rooms) {
                        room.displayInfo();
                    }

                } catch (Exception e) {
                    System.out.println("Invalid date format. Please enter dates in the format dd-MM-yyyy.");
                }

                System.out.print("which room you want to reserve: ");
                String roomIdReserver = scanner.nextLine();
                IRoom room = _HotelResource.getRoom(roomIdReserver);
                System.out.print("customer email: ");
                String _customerEmail = scanner.nextLine();

                Reservation res = _HotelResource.bookARoom(_customerEmail, room, checkInDate, checkOutDate );
                res.displayInfo();

                // Perform actions for Option 1
                break;
            case 2:
                System.out.println("--------------------See my reservations---------------------");
                System.out.println("customer email: ");
                String customerEmail = scanner.nextLine();
                Collection<Reservation> reservations = _HotelResource.getCustomersReservations(customerEmail);

                if(reservations.isEmpty()) {
                    System.out.println("no reservations found");
                    break;
                }

                for (Reservation element: reservations) {
                    element.displayInfo();
                }
                // Perform actions for Option 2
                break;
            case 3:
                System.out.println("-----------------------------Create an account---------------------------");

                System.out.println("Enter customer information:");

                String firstName = "";
                String lastName = "";
                String email = "";

                System.out.print("First Name: ");
                firstName = scanner.nextLine();

                System.out.print("Last Name: ");
                lastName = scanner.nextLine();

                System.out.print("Email: ");
                email = scanner.nextLine();

                _HotelResource.createACustomer(email, firstName, lastName);
                // Perform actions for Option 3
                break;
            case 4:
                System.out.println("-------------open admin menu----------------");
                // calling admin menu
                AdminMenu();
                // Perform actions for Option 4
                break;
            case 5:
                System.out.println("5.  Exit");
                break;
            default:
                System.out.println("Invalid option! Please choose a valid option.");
                break;
        }
    }
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            DisplayMainMenu();
            try {

                option = scanner.nextInt();

                MainMenu(option);

                System.out.println();
            } catch (Exception e) {
                System.out.println("Invalid input. Please enter a valid option number.");
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear the invalid input from the scanner
                option = 0; // Set option to 0 to continue the loop
            }
        } while (option != 5);

        System.out.println("Application exited.");
    }

    private static void DisplayMainMenu() {
        System.out.println("Menu:");
        System.out.println("------------------------------------");
        System.out.println("1.  Find and reserve a room");
        System.out.println("2.  See my reservations");
        System.out.println("3.  Create an account");
        System.out.println("4.  Admin");
        System.out.println("5.  Exit");
        System.out.println("------------------------------------");
        System.out.print("Enter your choice: ");
    }
}