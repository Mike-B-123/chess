package dataaccess;
import model.User;

import java.util.Collection ;
import java.util.HashMap ;
public class MemoryUserDAO implements UserDAO {
    private HashMap<String, User> users = new HashMap<>();
    private static MemoryUserDAO instance ;

    public void addUser(User inputUser) {
        users.put(inputUser.username(), inputUser);
    }

    public User findUser(User inputUser) {
            return users.get(inputUser.username()) ;
    }

    public void clearUsers(){
        users.clear();
    }

    public static MemoryUserDAO getInstance(){
        if(instance == null){
            return instance = new MemoryUserDAO() ;
        }
        return instance ;
    }

}
