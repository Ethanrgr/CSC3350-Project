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
    public Boolean register(User user) {
        try{
            arrayList.add(user);
        } catch(Exception e) {
            System.out.println("Error registering: " + e);
            return false;
        }

        return true;
    }

    @Override
    public Boolean loginAuthentication(User user) {
        Iterator<User> iterator = arrayList.iterator();
        while (iterator.hasNext()){
            User db_user = iterator.next();
            if (user.getEmail().equals(db_user.getEmail()) && user.getPassword().equals(db_user.getPassword()) && user.getRole() == db_user.getRole()){
                return true;
            }
        }
        return false;
    }

    @Override
    public ArrayList<User> displayAll() {
        return arrayList;
    }
}
