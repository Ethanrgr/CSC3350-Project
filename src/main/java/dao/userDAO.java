package dao;
public interface userDAO {
    String register(User user); //add new user(admin/employee)
    User loginAuthentication(User user); //verify if user exists in db

}
