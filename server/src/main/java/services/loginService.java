package services;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.User;

public class loginService {
    public static AuthData login(User inputUser){
        if(getUser(inputUser) != null){
            getUser(inputUser);
            return createAuth(inputUser);
        }
        return null ; // should actually return exception

    }

    public static User getUser(User inputUser){
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        return userDao.findUser(inputUser) ;
    }

    public static AuthData createAuth(User user){
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        return authDao.createAuth(user) ;
    }
}
