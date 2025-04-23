package dao;

import java.util.ArrayList;
import java.util.Iterator;

public class userDAOImpl implements userDAO{

    ArrayList<User> arrayList = new ArrayList<>();

    public userDAOImpl(){
        User mock_admin = new User("test","test",Role.ADMIN);
        User mock_employee = new User("test","test", Role.EMPLOYEE);
        arrayList.add(mock_employee);
        arrayList.add(mock_admin);
        System.out.println("Added test user and employee");
    }
    @Override
    public String register(User user) {
        arrayList.add(user);
        return null;
    }

    @Override
    public Boolean loginAuthentication(User user) {
        Iterator<User> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            User db_user = iterator.next();
            if (user.getEmail() == db_user.getEmail() && user.getPassword() == db_user.getPassword() && user.getRole() == db_user.getRole()){
                return true;
            }
        }
        return false;
    }
}
