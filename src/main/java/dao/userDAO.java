package dao;

import model.User;

import java.util.ArrayList;

public interface userDAO {
    Boolean register(User user, String url, String email, String password); //add new user(admin/employee)
    Boolean loginAuthentication(User user, String url, String email, String password); //verify if user exists in db
    ArrayList<User> displayAll();

}
