package dao;

import model.Employee;
import model.City;
import model.State;
import java.sql.*;
import java.util.*;

public class adminDAOImpl implements adminDAO {

    @Override
    public List<Employee> getAllEmployees(String url, String dbUser, String dbPass) {
        List<Employee> employees = new ArrayList<>();
        String sql = """
            SELECT e.*, a.street, a.zip, a.gender, a.identified_race, a.dob, a.mobile_phone,
                   c.city_id, c.city_name, 
                   s.state_id, s.state_name
            FROM employees e
            LEFT JOIN address a ON e.empid = a.empid
            LEFT JOIN city c ON a.city_id = c.city_id
            LEFT JOIN state s ON a.state_id = s.state_id
            """;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Employee employee = extractEmployeeFromResultSet(rs);
                employees.add(employee);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employees: " + e.getMessage());
        }
        
        return employees;
    }

    @Override
    public Employee getEmployeeById(int empId, String url, String dbUser, String dbPass) {
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
                    return extractEmployeeFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving employee: " + e.getMessage());
        }
        
        return null;
    }

    @Override
    public boolean createEmployee(Employee employee, String url, String dbUser, String dbPass) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPass);
            conn.setAutoCommit(false);
            
            // Insert into employees table
            String employeeSql = "INSERT INTO employees (empid, Fname, Lname, Email, HireDate, Salary, SSN) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(employeeSql)) {
                pstmt.setInt(1, employee.getEmpId());
                pstmt.setString(2, employee.getfName());
                pstmt.setString(3, employee.getlName());
                pstmt.setString(4, employee.getEmail());
                pstmt.setString(5, employee.getHireDate());
                pstmt.setFloat(6, employee.getSalary());
                pstmt.setString(7, employee.getSsn());
                
                int employeeResult = pstmt.executeUpdate();
                if (employeeResult == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            // Insert into address table
            String addressSql = "INSERT INTO address (empid, street, zip, city_id, state_id, gender, identified_race, dob, mobile_phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(addressSql)) {
                pstmt.setInt(1, employee.getEmpId());
                pstmt.setString(2, employee.getStreet());
                pstmt.setString(3, employee.getZip());
                
                if (employee.getCity() != null) {
                    pstmt.setInt(4, employee.getCity().getId());
                } else {
                    pstmt.setNull(4, java.sql.Types.INTEGER);
                }
                
                if (employee.getState() != null) {
                    pstmt.setInt(5, employee.getState().getId());
                } else {
                    pstmt.setNull(5, java.sql.Types.INTEGER);
                }
                
                pstmt.setString(6, employee.getGender());
                pstmt.setString(7, employee.getIdentifiedRace());
                pstmt.setString(8, employee.getDob());
                pstmt.setString(9, employee.getMobilePhone());
                
                int addressResult = pstmt.executeUpdate();
                if (addressResult == 0) {
                    conn.rollback();
                    return false;
                }
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            
            if (e.getErrorCode() == 1062) {
                System.err.println("Error creating employee: Duplicate entry - " + e.getMessage());
            } else {
                System.err.println("Error creating employee: " + e.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean updateEmployee(Employee employee, String url, String dbUser, String dbPass) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPass);
            conn.setAutoCommit(false);
            
            // Update employees table
            String employeeSql = "UPDATE employees SET Fname = ?, Lname = ?, email = ?, HireDate = ?, Salary = ?, SSN = ? WHERE empid = ?";
            try (PreparedStatement pstmt = conn.prepareStatement(employeeSql)) {
                pstmt.setString(1, employee.getfName());
                pstmt.setString(2, employee.getlName());
                pstmt.setString(3, employee.getEmail());
                pstmt.setString(4, employee.getHireDate());
                pstmt.setFloat(5, employee.getSalary());
                pstmt.setString(6, employee.getSsn());
                pstmt.setInt(7, employee.getEmpId());
                
                pstmt.executeUpdate();
            }

            String checkAddressSql = "SELECT COUNT(*) FROM address WHERE empid = ?";
            boolean addressExists = false;
            try (PreparedStatement pstmt = conn.prepareStatement(checkAddressSql)) {
                pstmt.setInt(1, employee.getEmpId());
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        addressExists = rs.getInt(1) > 0;
                    }
                }
            }
            
            String addressSql;
            if (addressExists) {
                addressSql = "UPDATE address SET street = ?, zip = ?, city_id = ?, state_id = ?, gender = ?, identified_race = ?, dob = ?, mobile_phone = ? WHERE empid = ?";
            } else {
                addressSql = "INSERT INTO address (street, zip, city_id, state_id, gender, identified_race, dob, mobile_phone, empid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            }
            
            try (PreparedStatement pstmt = conn.prepareStatement(addressSql)) {
                pstmt.setString(1, employee.getStreet());
                pstmt.setString(2, employee.getZip());
                
                if (employee.getCity() != null) {
                    pstmt.setInt(3, employee.getCity().getId());
                } else {
                    pstmt.setNull(3, java.sql.Types.INTEGER);
                }
                
                if (employee.getState() != null) {
                    pstmt.setInt(4, employee.getState().getId());
                } else {
                    pstmt.setNull(4, java.sql.Types.INTEGER);
                }
                
                pstmt.setString(5, employee.getGender());
                pstmt.setString(6, employee.getIdentifiedRace());
                pstmt.setString(7, employee.getDob());
                pstmt.setString(8, employee.getMobilePhone());
                pstmt.setInt(9, employee.getEmpId());
                
                pstmt.executeUpdate();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public boolean deleteEmployee(int empId, String url, String dbUser, String dbPass) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPass);
            conn.setAutoCommit(false);

            // 1. Delete from address table first (child table)
            String deleteAddressSql = "DELETE FROM address WHERE empid = ?";
            try (PreparedStatement pstmtAddress = conn.prepareStatement(deleteAddressSql)) {
                pstmtAddress.setInt(1, empId);
                pstmtAddress.executeUpdate();
            }

            // 2. Delete from employees table (parent table)
            String deleteEmployeeSql = "DELETE FROM employees WHERE empid = ?";
            try (PreparedStatement pstmtEmployee = conn.prepareStatement(deleteEmployeeSql)) {
                pstmtEmployee.setInt(1, empId);
                int rowsAffected = pstmtEmployee.executeUpdate();
                if (rowsAffected == 0) {
                    // If no employee was deleted rollback
                    conn.rollback();
                    return false;
                }
            }

            conn.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            // Rollback transaction in case of error
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Employee> searchEmployees(String name, String dob, String ssn, Integer empId, String url, String dbUser, String dbPass) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("""
            SELECT e.*, a.street, a.zip, a.gender, a.identified_race, a.dob, a.mobile_phone,
                   c.city_id, c.city_name, 
                   s.state_id, s.state_name
            FROM employees e
            LEFT JOIN address a ON e.empid = a.empid
            LEFT JOIN city c ON a.city_id = c.city_id
            LEFT JOIN state s ON a.state_id = s.state_id
            WHERE 1=1
            """);
        List<Object> params = new ArrayList<>();
        
        if (name != null && !name.trim().isEmpty()) {
            sqlBuilder.append(" AND (e.fname LIKE ? OR e.lname LIKE ?)");
            String nameLike = "%" + name.trim() + "%";
            params.add(nameLike);
            params.add(nameLike);
        }
        
        if (dob != null && !dob.trim().isEmpty()) {
            sqlBuilder.append(" AND a.dob = ?");
            params.add(dob.trim());
        }
        
        if (ssn != null && !ssn.trim().isEmpty()) {
            sqlBuilder.append(" AND e.ssn LIKE ?");
            params.add("%" + ssn.trim() + "%");
        }
        
        if (empId != null) {
            sqlBuilder.append(" AND e.empid = ?");
            params.add(empId);
        }
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sqlBuilder.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Employee employee = extractEmployeeFromResultSet(rs);
                    employees.add(employee);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching employees: " + e.getMessage());
        }
        
        return employees;
    }

    @Override
    public boolean updateSalaryInRange(double percentage, double minSalary, double maxSalary, String url, String dbUser, String dbPass) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, dbUser, dbPass);
            conn.setAutoCommit(false);
            
            // First get employees whose salary will be updated
            String selectSql = "SELECT empid, salary FROM employees WHERE salary >= ? AND salary < ?";
            List<Integer> updatedEmpIds = new ArrayList<>();
            Map<Integer, Float> newSalaries = new HashMap<>();
            
            try (PreparedStatement pstmt = conn.prepareStatement(selectSql)) {
                pstmt.setDouble(1, minSalary);
                pstmt.setDouble(2, maxSalary);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        int empId = rs.getInt("empid");
                        float oldSalary = rs.getFloat("salary");
                        float newSalary = oldSalary * (1 + (float)(percentage / 100.0));
                        
                        updatedEmpIds.add(empId);
                        newSalaries.put(empId, newSalary);
                    }
                }
            }
            
            if (updatedEmpIds.isEmpty()) {
                return false;
            }
            
            // Update salary in employees table
            String updateEmployeesSql = "UPDATE employees SET salary = salary * (1 + ?) WHERE salary >= ? AND salary < ?";
            try (PreparedStatement pstmt = conn.prepareStatement(updateEmployeesSql)) {
                pstmt.setDouble(1, percentage / 100.0); // Convert percentage to decimal
                pstmt.setDouble(2, minSalary);
                pstmt.setDouble(3, maxSalary);
                
                pstmt.executeUpdate();
            }
            
            // Insert new pay records in payroll table for each affected employee
            String insertPayrollSql = "INSERT INTO payroll (empid, pay_date, earnings, fed_tax, fed_med, fed_SS, state_tax, retire_401k, health_care) VALUES (?, CURRENT_DATE(), ?, 0, 0, 0, 0, 0, 0)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertPayrollSql)) {
                for (int empId : updatedEmpIds) {
                    float newSalary = newSalaries.get(empId);
                    
                    pstmt.setInt(1, empId);
                    pstmt.setFloat(2, newSalary / 12); // Monthly pay (annual salary / 12)
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
            }
            
            conn.commit();
            return true;
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException ex) {
                System.err.println("Error rolling back transaction: " + ex.getMessage());
            }
            
            System.err.println("Error updating salaries: " + e.getMessage());
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    @Override
    public List<Map<String, Object>> getAllPayStatements(String url, String dbUser, String dbPass) {
        List<Map<String, Object>> payStatements = new ArrayList<>();
        String sql = """
            SELECT e.empid, e.fname, e.lname, p.pay_date, 
                   p.earnings as amount
            FROM employees e
            JOIN payroll p ON e.empid = p.empid
            ORDER BY e.empid ASC, p.pay_date DESC
            """;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                Map<String, Object> statement = new HashMap<>();
                statement.put("empid", rs.getInt("empid"));
                statement.put("fname", rs.getString("fname"));
                statement.put("lname", rs.getString("lname"));
                statement.put("pay_date", rs.getString("pay_date"));
                statement.put("amount", rs.getDouble("amount"));
                payStatements.add(statement);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving pay statements: " + e.getMessage());
        }
        
        return payStatements;
    }

    @Override
    public List<Map<String, Object>> getTotalPayByJobTitle(String month, String year, String url, String dbUser, String dbPass) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = """
            SELECT jt.job_title as job_title, SUM(p.earnings) as total_pay
            FROM payroll p
            JOIN employees e ON p.empid = e.empid
            JOIN employee_job_titles ejt ON e.empid = ejt.empid
            JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id
            WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ?
            GROUP BY jt.job_title
            ORDER BY jt.job_title
            """;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, month);
            pstmt.setString(2, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("job_title", rs.getString("job_title"));
                    row.put("total_pay", rs.getDouble("total_pay"));
                    report.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating job title pay report: " + e.getMessage());
        }
        
        return report;
    }

    @Override
    public List<Map<String, Object>> getTotalPayByDivision(String month, String year, String url, String dbUser, String dbPass) {
        List<Map<String, Object>> report = new ArrayList<>();
        String sql = """
            SELECT d.name as division_name, SUM(p.earnings) as total_pay
            FROM payroll p
            JOIN employees e ON p.empid = e.empid
            JOIN employee_division ed ON e.empid = ed.empid
            JOIN division d ON ed.div_ID = d.ID
            WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ?
            GROUP BY d.name
            ORDER BY d.name
            """;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, month);
            pstmt.setString(2, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("division_name", rs.getString("division_name"));
                    row.put("total_pay", rs.getDouble("total_pay"));
                    report.add(row);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error generating division pay report: " + e.getMessage());
        }
        
        return report;
    }
    
    private Employee extractEmployeeFromResultSet(ResultSet rs) throws SQLException {
        int empId = rs.getInt("empid");
        String fName = rs.getString("Fname");
        String lName = rs.getString("Lname");
        String email = rs.getString("email");
        String hireDate = rs.getString("HireDate");
        float salary = rs.getFloat("Salary");
        String ssn = rs.getString("SSN");
        
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
