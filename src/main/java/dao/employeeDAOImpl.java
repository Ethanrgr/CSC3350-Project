package dao;

import model.Employee;
import model.City;
import model.State;
import java.sql.*;
import java.util.*;

public class employeeDAOImpl implements employeeDAO {
    public employeeDAOImpl(){
    }

    @Override
    public Employee getEmployeeById(Integer empId, String url, String dbUser, String dbPass) {
        String sql = """
            SELECT e.*, a.street, a.zip, a.gender, a.identified_race, a.dob, a.mobile_phone,
                   c.city_id, c.city_name, 
                   s.state_id, s.state_name
            FROM employees e
            LEFT JOIN address a ON e.empid = a.empid
            LEFT JOIN city c ON a.city_id = c.city_id
            LEFT JOIN state s ON a.state_id = s.state_id
            WHERE e.empid = ?
            """;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, empId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractEmployeeWithAddressFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public List<Map<String, Object>> getEmployeePayStatements(Integer empId, String startDate, String endDate, String url, String dbUser, String dbPass) {
        List<Map<String, Object>> payStatements = new ArrayList<>();
        
        StringBuilder sqlBuilder = new StringBuilder("""
            SELECT e.empid, e.fname, e.lname, p.pay_date, p.earnings as amount
            FROM employees e
            JOIN payroll p ON e.empid = p.empid
            WHERE e.empid = ?
            """);
        
        List<Object> params = new ArrayList<>();
        params.add(empId);
        
        if (startDate != null && !startDate.trim().isEmpty()) {
            sqlBuilder.append(" AND p.pay_date >= ?");
            params.add(startDate);
        }
        
        if (endDate != null && !endDate.trim().isEmpty()) {
            sqlBuilder.append(" AND p.pay_date <= ?");
            params.add(endDate);
        }
        
        sqlBuilder.append(" ORDER BY p.pay_date DESC");
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> statement = new HashMap<>();
                    statement.put("empid", rs.getInt("empid"));
                    statement.put("fname", rs.getString("fname"));
                    statement.put("lname", rs.getString("lname"));
                    statement.put("pay_date", rs.getString("pay_date"));
                    statement.put("amount", rs.getDouble("amount"));
                    payStatements.add(statement);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pay statements: " + e.getMessage());
        }
        
        return payStatements;
    }
    
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        int empId = rs.getInt("empid");
        String fName = rs.getString("fname");
        String lName = rs.getString("lname");
        String email = rs.getString("email");
        String hireDate = rs.getString("hiredate");
        float salary = rs.getFloat("salary");
        String ssn = rs.getString("ssn");
        
        return new Employee(empId, fName, lName, email, hireDate, salary, ssn);
    }

    private Employee extractEmployeeWithAddressFromResultSet(ResultSet rs) throws SQLException {
        int empId = rs.getInt("empid");
        String fName = rs.getString("fname");
        String lName = rs.getString("lname");
        String email = rs.getString("email");
        String hireDate = rs.getString("hiredate");
        float salary = rs.getFloat("salary");
        String ssn = rs.getString("ssn");
        
        // Extract address information
        String street = rs.getString("street");
        String zip = rs.getString("zip");
        String gender = rs.getString("gender");
        String identifiedRace = rs.getString("identified_race");
        String dob = rs.getString("dob");
        String mobilePhone = rs.getString("mobile_phone");
        
        // Extract city information
        City city = null;
        int cityId = rs.getInt("city_id");
        if (!rs.wasNull()) {
            String cityName = rs.getString("city_name");
            city = new City(cityId, cityName);
        }
        
        // Extract state information
        State state = null;
        int stateId = rs.getInt("state_id");
        if (!rs.wasNull()) {
            String stateName = rs.getString("state_name");
            state = new State(stateId, stateName, null);
        }
        
        return new Employee(empId, fName, lName, email, hireDate, salary, ssn,
                          street, zip, city, state, gender, identifiedRace, dob, mobilePhone);
    }
}
