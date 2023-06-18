import api.AdminResource;
import api.HotelResource;
import model.Customer;
import model.IRoom;
import model.Room;
import model.RoomType;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class AdminMenu {
    private static AdminMenu _AdminMenu;
    private AdminMenu() {
    }

    public static AdminMenu getInstance() {
        if(_AdminMenu == null) {
            _AdminMenu = new AdminMenu();
        }

        return _AdminMenu;
    }

    private void DisplayAdminMenu()
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

    public void InputRoom() {
        Scanner scanner = new Scanner(System.in);

        boolean addMoreRooms = true;

        while (addMoreRooms) {
            System.out.print("Enter room number: ");
            Integer roomNumber = scanner.nextInt();

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
            Room room = new Room(roomNumber.toString(), price, roomType);
            //.add(room);
            AdminResource _AdminResource = AdminResource.getInstance();
            _AdminResource.addRoom(room);

            System.out.print("Add another room? (y/n): ");
            String addAnotherRoom = scanner.nextLine();
            addMoreRooms = addAnotherRoom.equalsIgnoreCase("y");
        }
    }

    public void AdminMenuOnConsole() {

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

                        System.out.println("in valid data");
                        System.out.println(ex.getMessage());
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

    private void FakeData() throws ParseException {
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

            System.out.println("pproblem when seed data");
            System.out.println(a.getMessage());
        }
    }
}
