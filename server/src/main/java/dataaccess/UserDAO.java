package dataaccess;

import model.User ;
import java.util.Collection;

public interface UserDAO {
        public void addUser(User inputUser) ;
        public User findUser(User inputUser) ;
        public void clearUsers() ;
    }
