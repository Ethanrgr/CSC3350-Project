package mainmenu;

import dao.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class DaoTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
//        Scanner scanner3 = new Scanner(System.in);
//        Scanner scanner4 = new Scanner(System.in);

        employeeDAOImpl employeeDAOImpl1 = new employeeDAOImpl();
        adminDAOImpl adminDAOImpl1 = new adminDAOImpl();
        userDAOImpl userDAOImpl1 = new userDAOImpl();

        int i = 0;
        do {

            System.out.println("Login/Register/Display");
            String firstAction = scanner.next();
            System.out.println("Enter role: Employee/Admin"); //In UI, we'd have a check mark to identify is the user is admin/employee
            String userType = scanner2.next();

            switch (firstAction.toLowerCase()) {
                case "register" -> {
                    System.out.println("Enter email address:");
                    String email = scanner.next();
                    System.out.println("Enter password: ");
                    String password = scanner2.next();

                    User tempUser;
                    if (userType.toLowerCase().equals("employee")) {
                        tempUser = new User(email, password, Role.EMPLOYEE);
                    } else {
                        tempUser = new User(email, password, Role.ADMIN);
                    }

                    boolean result = userDAOImpl1.register(tempUser);
                    if (result) {
                        System.out.println("Registered------Login page");
                    } else {
                        System.out.println("Error occured!");
                    }

                }
                case "login" -> {
                    System.out.println("Enter email address:");
                    String email = scanner.next();
                    System.out.println("Enter password: ");
                    String password = scanner2.next();

                    User tempUser;
                    if (userType.toLowerCase().equals("employee")) {
                        tempUser = new User(email, password, Role.EMPLOYEE);
                    } else {
                        tempUser = new User(email, password, Role.ADMIN);
                    }
                    boolean result = userDAOImpl1.loginAuthentication(tempUser);
                    if (result) {
                        System.out.println("Success: ---- Login Page");
                    } else {
                        System.out.println("Failure");
                    }
                    // if logged in - redirect to profile page; else try again

                }
                case "display" -> {
                    ArrayList<User> outputList = userDAOImpl1.displayAll();
                    for (User db_user : outputList) {
                        System.out.println(db_user.toString());
                    }
                }
                default -> System.out.println("Wrong action!");
            }

            System.out.println("Continue Again: ");

        } while (i == 0);
        scanner.close();
        scanner2.close();

    }
}
