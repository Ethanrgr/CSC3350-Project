package dao;
public interface userDAO {
    String register(String email, String password);
    String loginAuthentication(String email, String password);

}
