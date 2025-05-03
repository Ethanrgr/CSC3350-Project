package controller;

import dao.employeeDAOImpl;
import io.javalin.http.Context;
import model.Employee;
import model.User;
import java.util.List;
import java.util.Map;

public class employeeController {
    private final String url = "jdbc:mysql://localhost:3306/employeedata";
    private final String sql_user = "root";
    private final String sql_password = "jinash123";
    private final employeeDAOImpl employeeDAO = new employeeDAOImpl();
    
    public void getEmployeeProfile(Context ctx) {
        
        User user = ctx.sessionAttribute("user");
        if (user == null || user.getEmpid() == null) {
            ctx.status(401).result("Unauthorized or invalid user");
            return;
        }
        
        Employee employee = employeeDAO.getEmployeeById(user.getEmpid(), url, sql_user, sql_password);
        if (employee != null) {
            ctx.json(employee);
        } else {
            ctx.status(404).result("Employee profile not found");
        }
    }
    
    public void getEmployeePayStatements(Context ctx) {
        User user = ctx.sessionAttribute("user");
        if (user == null || user.getEmpid() == null) {
            ctx.status(401).result("Unauthorized or invalid user");
            return;
        }
        
        String startDate = ctx.queryParam("startDate");
        String endDate = ctx.queryParam("endDate");
        
        List<Map<String, Object>> payStatements = 
            employeeDAO.getEmployeePayStatements(user.getEmpid(), startDate, endDate, url, sql_user, sql_password);
        
        ctx.json(payStatements);
    }
}
