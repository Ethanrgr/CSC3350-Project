package controller;

import dao.adminDAOImpl;
import io.javalin.http.Context;
import model.Employee;
import java.util.List;
import java.util.Map;

public class adminController {
    private final String url = "jdbc:mysql://localhost:3306/employeedata";
    private final String sql_user = "root";
    private final String sql_password = "jinash123";
    private final adminDAOImpl adminDAO = new adminDAOImpl();
    
    public void getAllEmployees(Context ctx) {
        List<Employee> employees = adminDAO.getAllEmployees(url, sql_user, sql_password);
        ctx.json(employees);
    }
    
    public void getEmployeeById(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid employee ID format");
            return;
        }
        
        Employee employee = adminDAO.getEmployeeById(id, url, sql_user, sql_password);
        if (employee != null) {
            ctx.json(employee);
        } else {
            ctx.status(404).result("Employee not found");
        }
    }
    
    public void createEmployee(Context ctx) {
        Employee newEmployee = ctx.bodyAsClass(Employee.class);
        boolean success = adminDAO.createEmployee(newEmployee, url, sql_user, sql_password);
        
        if (success) {
            ctx.status(201).json(newEmployee);
        } else {
            ctx.status(500).result("Failed to create employee");
        }
    }
    
    public void updateEmployee(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid employee ID format");
            return;
        }
        
        Employee updatedEmployee = ctx.bodyAsClass(Employee.class);
        updatedEmployee.setEmpId(id); // Ensure ID from path is used
        
        boolean success = adminDAO.updateEmployee(updatedEmployee, url, sql_user, sql_password);
        if (success) {
            ctx.json(updatedEmployee);
        } else {
            ctx.status(500).result("Failed to update employee");
        }
    }
    
    public void deleteEmployee(Context ctx) {
        int id;
        try {
            id = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid employee ID format");
            return;
        }
        
        boolean success = adminDAO.deleteEmployee(id, url, sql_user, sql_password);
        if (success) {
            ctx.status(204).result("Employee deleted successfully");
        } else {
            ctx.status(500).result("Failed to delete employee");
        }
    }
    
    public void searchEmployees(Context ctx) {
        String name = ctx.queryParam("name");
        String dob = ctx.queryParam("dob");
        String ssn = ctx.queryParam("ssn");
        String empIdStr = ctx.queryParam("empid");
        
        Integer empId = null;
        if (empIdStr != null && !empIdStr.isEmpty()) {
            try {
                empId = Integer.parseInt(empIdStr);
            } catch (NumberFormatException e) {
                ctx.status(400).result("Invalid employee ID format");
                return;
            }
        }
        
        List<Employee> employees = adminDAO.searchEmployees(name, dob, ssn, empId, url, sql_user, sql_password);
        ctx.json(employees);
    }
    
    public void updateSalaryRange(Context ctx) {
        Map updateParams = ctx.bodyAsClass(Map.class);
        
        // Extract parameters from the request body
        double percentage;
        double minSalary;
        double maxSalary;
        
        try {
            percentage = Double.parseDouble(updateParams.get("percentage").toString());
            minSalary = Double.parseDouble(updateParams.get("minSalary").toString());
            maxSalary = Double.parseDouble(updateParams.get("maxSalary").toString());
        } catch (Exception e) {
            ctx.status(400).result("Invalid parameters. Required: percentage, minSalary, maxSalary");
            return;
        }
        
        boolean success = adminDAO.updateSalaryInRange(percentage, minSalary, maxSalary, url, sql_user, sql_password);
        if (success) {
            ctx.json(Map.of("message", "Salaries updated successfully"));
        } else {
            ctx.status(500).result("Failed to update salaries");
        }
    }
    
    public void getAllPayStatements(Context ctx) {
        List<Map<String, Object>> payStatements = adminDAO.getAllPayStatements(url, sql_user, sql_password);
        ctx.json(payStatements);
    }
    
    public void getTotalPayByJobTitle(Context ctx) {
        String month = ctx.queryParam("month");
        String year = ctx.queryParam("year");
        
        if (month == null || year == null) {
            ctx.status(400).result("Month and year parameters are required");
            return;
        }
        
        List<Map<String, Object>> report = adminDAO.getTotalPayByJobTitle(month, year, url, sql_user, sql_password);
        ctx.json(report);
    }
    
    public void getTotalPayByDivision(Context ctx) {
        String month = ctx.queryParam("month");
        String year = ctx.queryParam("year");
        
        if (month == null || year == null) {
            ctx.status(400).result("Month and year parameters are required");
            return;
        }
        
        List<Map<String, Object>> report = adminDAO.getTotalPayByDivision(month, year, url, sql_user, sql_password);
        ctx.json(report);
    }
}
