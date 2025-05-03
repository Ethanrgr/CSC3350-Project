package dao;

import java.util.List;
import java.util.Map;
import model.Employee;

public interface adminDAO {
    List<Employee> getAllEmployees(String url, String dbUser, String dbPass);
    Employee getEmployeeById(int empId, String url, String dbUser, String dbPass);
    boolean createEmployee(Employee employee, String url, String dbUser, String dbPass);
    boolean updateEmployee(Employee employee, String url, String dbUser, String dbPass);
    boolean deleteEmployee(int empId, String url, String dbUser, String dbPass);
    
    List<Employee> searchEmployees(String name, String dob, String ssn, Integer empId, String url, String dbUser, String dbPass);
    boolean updateSalaryInRange(double percentage, double minSalary, double maxSalary, String url, String dbUser, String dbPass);
    
    List<Map<String, Object>> getAllPayStatements(String url, String dbUser, String dbPass);
    List<Map<String, Object>> getTotalPayByJobTitle(String month, String year, String url, String dbUser, String dbPass);
    List<Map<String, Object>> getTotalPayByDivision(String month, String year, String url, String dbUser, String dbPass);
}
