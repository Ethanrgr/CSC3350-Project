package dao;

import java.util.ArrayList;

public class employeeDAOImpl implements employeeDAO {
    User user = new User("test","test",Role.EMPLOYEE);
    ArrayList<User> arrayList = new ArrayList<>();
    public employeeDAOImpl(){
        arrayList.add(user);
        System.out.println("Test employee user account created");
    }
    @Override
    public String displayEmployee() {
        return null;
    }

    @Override
    public String displayPayStatementHistory() {
        return null;
    }

    @Override
    public String register(User user) {
        return null;
    }

    @Override
    public String loginAuthentication(String email, String password) {
        //SELECT email,password from users where Role = User; if returns null, not found; else found
        return null;
    }
}
