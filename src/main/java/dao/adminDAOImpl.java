package dao;

import java.util.Date;

public class adminDAOImpl implements adminDAO{

    @Override
    public boolean addUser() {
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
    public String register(String email, String password) {
        return null;
    }

    @Override
    public String loginAuthentication(String email, String password) {
        return null;
    }
}
