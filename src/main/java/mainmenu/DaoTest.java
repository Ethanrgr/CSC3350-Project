package mainmenu;

import dao.*;

import java.util.ArrayList;
import java.util.Scanner;

public class DaoTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
        Scanner scanner3 = new Scanner(System.in);
        Scanner scanner4 = new Scanner(System.in);

        System.out.println("Login/Register");
        String firstAction = scanner.next();
        System.out.println("Enter role: Employee/Admin"); //In UI, we'd have a check mark to identify is the user is admin/employee
        String userType = scanner2.next();

//        ArrayList<User> userList = new ArrayList<>();
        employeeDAOImpl employeeDAOImpl1 = new employeeDAOImpl();
        adminDAOImpl adminDAOImpl1 = new adminDAOImpl();
        userDAOImpl userDAOImpl1 = new userDAOImpl();

        System.out.println("Enter email address:");
        String email = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner2.next();

        if (firstAction.toLowerCase() == "register"){
            User tempUser;
            if (userType.toLowerCase() == "employee"){
                tempUser = new User(email, password, Role.EMPLOYEE);
            }
            else {
                tempUser = new User(email, password, Role.ADMIN);
            }
            userDAOImpl1.register(tempUser);
            //registered - redirect to login page
        }
        else if (firstAction.toLowerCase() == "login"){
            User tempUser;
            if (userType.toLowerCase() == "employee"){
                tempUser = new User(email, password, Role.EMPLOYEE);
            }
            else {
                tempUser = new User(email, password, Role.ADMIN);
            }
            boolean result = userDAOImpl1.loginAuthentication(tempUser);
            if (result == true){
                System.out.println("Success:");
            }
            else{
                System.out.println("Failure");
            }
            // if logged in - redirect to profile page; else try again
        }
        else {

        }
    }
}
