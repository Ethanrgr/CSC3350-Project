package dao;

import java.util.ArrayList;
import java.util.Date;

public class adminDAOImpl implements adminDAO{
    User user = new User("test","test", Role.ADMIN);
    ArrayList<User> arrayList = new ArrayList<>();

    public adminDAOImpl(){
        arrayList.add(user);
        System.out.println("Added test admin user");
    }
    @Override
    public boolean addEmployeeRecord() {
        return false;
    }

    @Override
    public String searchEmployee(Date dob, String ssn, String empId) {
        return null;
    }

    @Override
    public String displayAllEmployees() {
        return null;
    }

    @Override
    public boolean updateEmployeeData() {
        return false;
    }

    @Override
    public boolean updateEmployeeSalary() {
        return false;
    }

    @Override
    public String displayEmployeePayStatement() {
        return null;
    }

    @Override
    public String displayTotalPayByJobTitle() {
        return null;
    }

    @Override
    public String displayTotalPayByDivision() {
        return null;
    }

    @Override
    public String register(User user) { //Registering new admin account
        return null;
    }

    @Override
    public String loginAuthentication(String email, String password) { //verify if admin user account exist in user table in db
        //SELECT email,password from users where Role = Admin; if returns null, not found; else found
        return null;
    }
}
