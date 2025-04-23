package dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

public class adminDAOImpl implements adminDAO{
    public adminDAOImpl(){

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

}
