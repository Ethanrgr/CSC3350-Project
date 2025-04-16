package dao;

import java.util.ArrayList;
import java.util.Iterator;

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
}
