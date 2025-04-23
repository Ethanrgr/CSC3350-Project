package dao;
public interface userDAO {
    String register(User user); //add new user(admin/employee)
    Boolean loginAuthentication(User user); //verify if user exists in db

}
