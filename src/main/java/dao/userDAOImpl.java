package dao;

import model.Role;
import model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;

public class userDAOImpl implements userDAO{

    ArrayList<User> arrayList = new ArrayList<>();

    public userDAOImpl(){
        
    }
    
    @Override
    public Boolean register(User user, String url, String dbUser, String dbPass) {
        // Validate common requirements
        if (user.getEmail() == null || user.getPassword() == null || user.getRole() == null) {
            System.err.println("Error: Missing required user data");
            return false;
        }
        
        if (user.getRole() == Role.ADMIN) {
            user.setEmpid(null);
        }

        if (user.getRole() == Role.EMPLOYEE) {
            Integer empid = getEmployeeIdByEmail(user.getEmail(), url, dbUser, dbPass);
            if (empid == null) {
                System.err.println("Error: No employee record found with email: " + user.getEmail());
                return false;
            }
            user.setEmpid(empid);
            if (isEmployeeAlreadyRegistered(user.getEmpid(), url, dbUser, dbPass)) {
                System.err.println("Error: Employee ID " + user.getEmpid() + " already has a user account");
                return false;
            }
        }
        
        String sql = "INSERT INTO users (email, password, role, empid) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement p = conn.prepareStatement(sql)) {

            p.setString(1, user.getEmail());
            p.setString(2, user.getPassword());
            p.setString(3, user.getRole().name());

            if (user.getRole() == Role.EMPLOYEE) {
                p.setInt(4, user.getEmpid());
            } else {
                p.setNull(4, Types.INTEGER);
            }

            return p.executeUpdate() == 1;
        }
        catch (SQLException e) {
            System.err.println("Error in registration: " + e.getMessage());
            return false;
        }
    }

    private Integer getEmployeeIdByEmail(String email, String url, String dbUser, String dbPass) {
        String sql = "SELECT empid FROM employees WHERE email = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setString(1, email);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("empid");
                }
                return null; // No employee found with this email
            }
        } catch (SQLException e) {
            System.err.println("Error looking up employee by email: " + e.getMessage());
            return null;
        }
    }
    
    
    private boolean isEmployeeAlreadyRegistered(Integer empId, String url, String dbUser, String dbPass) {
        String sql = "SELECT 1 FROM users WHERE empid = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
             
            pstmt.setInt(1, empId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // Returns true if employee is already registered
            }
        } catch (SQLException e) {
            System.err.println("Error checking if employee is registered: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean loginAuthentication(User user, String url, String email, String password) {
        String sql = "SELECT * from users where email = ? AND password = ? AND role = ?";

        try (Connection myConn = DriverManager.getConnection(url,email,password);
             PreparedStatement pstmt = myConn.prepareStatement(sql)){

                 pstmt.setString(1,user.getEmail());
                 pstmt.setString(2,user.getPassword());
                 pstmt.setString(3,user.getRole().name());

                 ResultSet rs = pstmt.executeQuery();
                 return rs.next();
        }
        catch (SQLException e){
                 System.out.println("Login error: " + e.getMessage());
                 return false;
        }
    }

    @Override
    public User login(String email, String password, String url, String dbUser, String dbPass) {
        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        
        try (Connection conn = DriverManager.getConnection(url, dbUser, dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setEmail(rs.getString("email"));
                user.setPassword(rs.getString("password"));
                user.setRole(Role.fromString(rs.getString("role")));
                
                // If empid is not null, set it
                Integer empid = rs.getInt("empid");
                if (!rs.wasNull()) {
                    user.setEmpid(empid);
                }
                
                return user;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("Error in login: " + e.getMessage());
            return null;
        }
    }

    @Override
    public ArrayList<User> displayAll() {
        return arrayList;
    }
}
