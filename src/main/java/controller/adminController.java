package controller;

import dao.adminDAOImpl;
import io.javalin.http.Context;
import model.Employee;
import model.City;
import model.State;
import dto.CreateEmployeeRequest;
import dto.UpdateEmployeeRequest;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;

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
        CreateEmployeeRequest createRequest;
        try {
            createRequest = ctx.bodyAsClass(CreateEmployeeRequest.class);
        } catch (Exception e) { 
            ctx.status(400).result("Invalid request body format: " + e.getMessage());
            return; 
        }

        if (!createRequest.isValid()) {
            ctx.status(400).result(createRequest.getValidationMessage());
            return;
        }

        Employee newEmployee = new Employee();
        newEmployee.setEmpId(createRequest.getEmpid());
        newEmployee.setfName(createRequest.getfName());
        newEmployee.setlName(createRequest.getlName());
        newEmployee.setEmail(createRequest.getEmail());
        newEmployee.setHireDate(createRequest.getHireDate());
        newEmployee.setSalary(createRequest.getSalary());
        newEmployee.setSsn(createRequest.getSsn());

        // Set address information
        newEmployee.setStreet(createRequest.getStreet());
        newEmployee.setZip(createRequest.getZip());
        newEmployee.setGender(createRequest.getGender());
        newEmployee.setIdentifiedRace(createRequest.getIdentifiedRace());
        newEmployee.setDob(createRequest.getDob());
        newEmployee.setMobilePhone(createRequest.getMobilePhone());
        
        // Set city and state
        if (createRequest.getCityId() != null) {
            City city = getCityById(createRequest.getCityId());
            newEmployee.setCity(city);
        }
        
        if (createRequest.getStateId() != null) {
            State state = getStateById(createRequest.getStateId());
            newEmployee.setState(state);
        }

        boolean success = adminDAO.createEmployee(newEmployee, url, sql_user, sql_password);
        
        if (success) {
            ctx.status(201).json(newEmployee); 
        } else {
            ctx.status(500).result("Failed to create employee. Possible duplicate ID, email, or SSN.");
        }
    }
    
    public void updateEmployee(Context ctx) {
        int empId;
        try {
            empId = Integer.parseInt(ctx.pathParam("id"));
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid employee ID format in URL path.");
            return;
        }

        UpdateEmployeeRequest updateRequest;
        try {
            updateRequest = ctx.bodyAsClass(UpdateEmployeeRequest.class);
        } catch (Exception e) { 
            ctx.status(400).result("Invalid request body format: " + e.getMessage());
            return; 
        }

        if (!updateRequest.isValid()) {
            ctx.status(400).result(updateRequest.getValidationMessage());
            return;
        }

        Employee existingEmployee = adminDAO.getEmployeeById(empId, url, sql_user, sql_password);
        if (existingEmployee == null) {
            ctx.status(404).result("Employee with ID " + empId + " not found.");
            return;
        }

        existingEmployee.setfName(updateRequest.getfName());
        existingEmployee.setlName(updateRequest.getlName());
        existingEmployee.setEmail(updateRequest.getEmail());
        existingEmployee.setHireDate(updateRequest.getHireDate());
        existingEmployee.setSalary(updateRequest.getSalary());
        existingEmployee.setSsn(updateRequest.getSsn());
        
        // Update address information
        existingEmployee.setStreet(updateRequest.getStreet());
        existingEmployee.setZip(updateRequest.getZip());
        existingEmployee.setGender(updateRequest.getGender());
        existingEmployee.setIdentifiedRace(updateRequest.getIdentifiedRace());
        existingEmployee.setDob(updateRequest.getDob());
        existingEmployee.setMobilePhone(updateRequest.getMobilePhone());
        
        // Update city and state
        if (updateRequest.getCityId() != null) {
            City city = getCityById(updateRequest.getCityId());
            existingEmployee.setCity(city);
        }
        
        if (updateRequest.getStateId() != null) {
            State state = getStateById(updateRequest.getStateId());
            existingEmployee.setState(state);
        }

        boolean success = adminDAO.updateEmployee(existingEmployee, url, sql_user, sql_password);
        
        if (success) {
            ctx.json(existingEmployee);
        } else {
            ctx.status(500).result("Failed to update employee. Possible duplicate email or SSN, or database error.");
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
        Map<String, Object> updateParams = ctx.bodyAsClass(Map.class);
        
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

        if (percentage <= 0) {
            ctx.status(400).result("Percentage must be greater than 0");
            return;
        }
        
        if (minSalary >= maxSalary) {
            ctx.status(400).result("Minimum salary must be less than maximum salary");
            return;
        }

        int affectedCount = getEmployeeCountInSalaryRange(minSalary, maxSalary);
        
        boolean success = adminDAO.updateSalaryInRange(percentage, minSalary, maxSalary, url, sql_user, sql_password);
        if (success) {
            ctx.json(Map.of(
                "message", "Salaries updated successfully",
                "affectedEmployees", affectedCount,
                "percentage", percentage,
                "minSalary", minSalary,
                "maxSalary", maxSalary
            ));
        } else {
            if (affectedCount == 0) {
                ctx.status(404).result("No employees found in the specified salary range");
            } else {
                ctx.status(500).result("Failed to update salaries");
            }
        }
    }
    
    private int getEmployeeCountInSalaryRange(double minSalary, double maxSalary) {
        int count = 0;
        String sql = "SELECT COUNT(*) FROM employees WHERE salary >= ? AND salary < ?";
        
        try (Connection conn = DriverManager.getConnection(url, sql_user, sql_password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, minSalary);
            pstmt.setDouble(2, maxSalary);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error counting employees in salary range: " + e.getMessage());
        }
        
        return count;
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


    private City getCityById(int cityId) {
        String sql = "SELECT city_id, city_name FROM city WHERE city_id = ?";
        try (Connection conn = DriverManager.getConnection(url, sql_user, sql_password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, cityId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new City(rs.getInt("city_id"), rs.getString("city_name"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving city: " + e.getMessage());
        }
        return null;
    }
    
    private State getStateById(int stateId) {
        String sql = "SELECT state_id, state_name FROM state WHERE state_id = ?";
        try (Connection conn = DriverManager.getConnection(url, sql_user, sql_password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, stateId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new State(rs.getInt("state_id"), rs.getString("state_name"), null);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving state: " + e.getMessage());
        }
        return null;
    }
}
