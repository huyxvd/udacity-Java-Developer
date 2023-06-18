import api.HotelResource;
import model.IRoom;
import model.Reservation;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {

    private static MainMenu _MainMenu;
    private static final AdminMenu _AdminMenu = AdminMenu.getInstance();
    private MainMenu() {
    }

    public static MainMenu getInstance() {
        if(_MainMenu == null) {
            _MainMenu = new MainMenu();
        }

        return _MainMenu;
    }

    public void DisplayMainMenu() {
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

    public void MainMenuOnConsole(int option) {
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
                Date today = new Date();

                try {
                    checkInDate = dateFormat.parse(checkInDateStr);
                    checkOutDate = dateFormat.parse(checkOutDateStr);

                    if(today.compareTo(checkInDate) > 0) {
                        throw new IllegalArgumentException("date check-in must be greater todayy");
                    }

                    if(checkOutDate.compareTo(checkInDate) < 0) {

                        throw new IllegalArgumentException("date check out must be greater than checkin");
                    }

                    Collection<IRoom> rooms = _HotelResource.findARoom(checkInDate, checkOutDate);

                    if(rooms.isEmpty()) {
                        System.out.println("no room available in search range");

                        // Create a Calendar instance and set the Date object
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(checkOutDate);

                        // Increase the date by a specific amount
                        calendar.add(Calendar.DAY_OF_MONTH, 7);

                        // Get the increased date as a Date object
                        Date increasedDate = calendar.getTime();
                        calendar.add(Calendar.DAY_OF_MONTH, 10);
                        Date increasedDate2 = calendar.getTime();
                        System.out.println("--------------suggest date & room (dd-MM-yyyy) -----------------");

                        System.out.println("From: " + dateFormat.format(increasedDate) + "To: " + dateFormat.format(increasedDate2) );
                        rooms = _HotelResource.findARoom(increasedDate, increasedDate2);
                        if(rooms.isEmpty()) {
                            System.out.println("--------------not found any room avaiable-----------------");
                            break;
                        }
                    }

                    for (IRoom room: rooms) {
                        room.displayInfo();
                    }

                    System.out.print("which room you want to reserve: ");
                    String roomIdReserver = scanner.nextLine();
                    IRoom room = _HotelResource.getRoom(roomIdReserver);
                    System.out.print("customer email: ");
                    String _customerEmail = scanner.nextLine();

                    Reservation res = _HotelResource.bookARoom(_customerEmail, room, checkInDate, checkOutDate );
                    res.displayInfo();

                } catch (Exception e) {
                    System.out.println("Invalid data.");
                    System.out.println(e.getMessage());
                }
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
                _AdminMenu.AdminMenuOnConsole();
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


}
