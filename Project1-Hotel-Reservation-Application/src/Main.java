import api.AdminResource;
import api.HotelResource;
import model.*;
import service.CustomerService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {

    public static void main(String[] args) {
        MainMenu mainMenu = MainMenu.getInstance();
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            mainMenu.DisplayMainMenu();
            try {

                option = scanner.nextInt();

                mainMenu.MainMenuOnConsole(option);

                System.out.println();
            } catch (Exception e) {
                System.out.println("Invalid input");
                System.out.println(e.getMessage());
                scanner.nextLine(); // Clear the invalid input from the scanner
                option = 0; // Set option to 0 to continue the loop
            }
        } while (option != 5);

        System.out.println("Application exited.");
    }


}