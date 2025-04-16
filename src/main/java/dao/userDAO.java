package dao;
public interface userDAO {
    String register(User user); //add new user(admin/employee)
    String loginAuthentication(String email, String password); //verify if user exists in db

}
