package dao;

import java.util.ArrayList;

public interface userDAO {
    Boolean register(User user); //add new user(admin/employee)
    Boolean loginAuthentication(User user); //verify if user exists in db

    ArrayList<User> displayAll();

}
