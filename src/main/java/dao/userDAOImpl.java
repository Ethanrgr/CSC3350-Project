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
        User mock_admin = new User("test","test", Role.ADMIN);
        User mock_employee = new User("test","test", Role.EMPLOYEE);
        arrayList.add(mock_employee);
        arrayList.add(mock_admin);
        System.out.println("Added test user and employee");
    }
    @Override
    public Boolean register(User user, String url, String email, String password) {
        String sql = "INSERT INTO USERS (email,password,role) values (?,?,?);";

        try (Connection myConn = DriverManager.getConnection(url,email,password);
             PreparedStatement pstmt = myConn.prepareStatement(sql)){

            pstmt.setString(1,user.getEmail());
            pstmt.setString(2,user.getPassword());
            pstmt.setString(3,user.getRole().name());

            int rowAffected = pstmt.executeUpdate();
            return rowAffected > 0; // greater than one row affected/updated
        }
        catch (SQLException e){
            System.out.println("Error in registration: "+ e.getMessage());
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
    public ArrayList<User> displayAll() {
        return arrayList;
    }
}
