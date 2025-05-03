package dao;

import model.Employee;
import java.sql.*;
import java.util.*;

public class adminDAOImpl implements adminDAO {

    @Override
    public List<Employee> getAllEmployees(String url, String dbUser, String dbPass) {
        List<Employee> employees = new ArrayList<>();
        String sql = "SELECT * FROM employees";
        
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
        String sql = "SELECT * FROM employees WHERE empid = ?";
        
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
        String sql = "INSERT INTO employees (fname, lname, email, hire_date, salary, ssn) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, employee.getfName());
            pstmt.setString(2, employee.getlName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getHireDate());
            pstmt.setFloat(5, employee.getSalary());
            pstmt.setString(6, employee.getSsn());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        employee.setEmpId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error creating employee: " + e.getMessage());
        }
        
        return false;
    }

    @Override
    public boolean updateEmployee(Employee employee, String url, String dbUser, String dbPass) {
        String sql = "UPDATE employees SET fname = ?, lname = ?, email = ?, hire_date = ?, salary = ?, ssn = ? WHERE empid = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, employee.getfName());
            pstmt.setString(2, employee.getlName());
            pstmt.setString(3, employee.getEmail());
            pstmt.setString(4, employee.getHireDate());
            pstmt.setFloat(5, employee.getSalary());
            pstmt.setString(6, employee.getSsn());
            pstmt.setInt(7, employee.getEmpId());
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteEmployee(int empId, String url, String dbUser, String dbPass) {
        String sql = "DELETE FROM employees WHERE empid = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, empId);
            
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Employee> searchEmployees(String name, String dob, String ssn, Integer empId, String url, String dbUser, String dbPass) {
        List<Employee> employees = new ArrayList<>();
        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM employees e LEFT JOIN address a ON e.empid = a.empid WHERE 1=1");
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
        String sql = "UPDATE employees SET salary = salary * (1 + ?) WHERE salary >= ? AND salary < ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setDouble(1, percentage / 100.0); // Convert percentage to decimal
            pstmt.setDouble(2, minSalary);
            pstmt.setDouble(3, maxSalary);
            
            int rowsUpdated = pstmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error updating salaries: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getAllPayStatements(String url, String dbUser, String dbPass) {
        List<Map<String, Object>> payStatements = new ArrayList<>();
        String sql = """
            SELECT e.empid, e.fname, e.lname, p.pay_date, p.amount
            FROM employees e
            JOIN payroll p ON e.empid = p.empid
            ORDER BY e.empid, p.pay_date
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
            SELECT jt.title, SUM(p.amount) as total_pay
            FROM payroll p
            JOIN employees e ON p.empid = e.empid
            JOIN employee_job_titles ejt ON e.empid = ejt.empid
            JOIN job_titles jt ON ejt.job_title_id = jt.job_title_id
            WHERE MONTH(p.pay_date) = ? AND YEAR(p.pay_date) = ?
            GROUP BY jt.title
            ORDER BY jt.title
            """;
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, month);
            pstmt.setString(2, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Map<String, Object> row = new HashMap<>();
                    row.put("job_title", rs.getString("title"));
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
            SELECT d.name as division, SUM(p.amount) as total_pay
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
                    row.put("division", rs.getString("division"));
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
        String fName = rs.getString("fname");
        String lName = rs.getString("lname");
        String email = rs.getString("email");
        String hireDate = rs.getString("hire_date");
        float salary = rs.getFloat("salary");
        String ssn = rs.getString("ssn");
        
        return new Employee(empId, fName, lName, email, hireDate, salary, ssn);
    }
}
