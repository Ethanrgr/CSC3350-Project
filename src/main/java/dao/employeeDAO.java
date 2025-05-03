package dao;

import model.Employee;
import java.util.List;
import java.util.Map;

public interface employeeDAO {
    Employee getEmployeeById(Integer empId, String url, String dbUser, String dbPass);
    List<Map<String, Object>> getEmployeePayStatements(Integer empId, String startDate, String endDate, String url, String dbUser, String dbPass);
}
