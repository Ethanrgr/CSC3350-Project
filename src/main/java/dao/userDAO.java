package dao;

import model.User;

import java.util.ArrayList;

public interface userDAO {
    Boolean register(User user, String url, String email, String password); 
    Boolean loginAuthentication(User user, String url, String email, String password); 
    User login(String email, String password, String url, String dbUser, String dbPass); 
    ArrayList<User> displayAll();
}
