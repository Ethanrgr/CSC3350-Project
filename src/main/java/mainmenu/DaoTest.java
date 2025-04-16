package mainmenu;

import dao.Role;
import dao.User;
import dao.adminDAOImpl;
import dao.employeeDAOImpl;

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

        System.out.println("Enter email address:");
        String email = scanner.next();
        System.out.println("Enter password: ");
        String password = scanner2.next();

        if (firstAction.toLowerCase() == "register"){

            if (userType.toLowerCase() == "employee"){
                User tempUser = new User(email,password,Role.EMPLOYEE);
                employeeDAOImpl1.register(tempUser); //Account registered - redirect to profile page
            }
            else {
                User tempUser = new User(email,password, Role.ADMIN);
                adminDAOImpl1.register(tempUser);
            }

        }
        else if (firstAction.toLowerCase() == "login"){
            if (userType.toLowerCase() == "employee"){
                employeeDAOImpl1.loginAuthentication(email,password);
            }
            else {
                adminDAOImpl1.loginAuthentication(email,password);
            }
        }
        else {

        }
    }
}
