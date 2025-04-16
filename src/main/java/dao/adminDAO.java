package dao;

import java.util.Date;

public interface adminDAO extends userDAO{
    boolean addEmployeeRecord();
    String searchEmployee(Date dob, String ssn, String empId);
    String displayAllEmployees();

    boolean updateEmployeeData();
    boolean updateEmployeeSalary();

    String displayEmployeePayStatement(); //Will figure out sorting later
    String displayTotalPayByJobTitle();
    String displayTotalPayByDivision();


}
