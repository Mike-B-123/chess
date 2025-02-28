package services;
import dataaccess.* ;
import model.AuthData;
import model.User;

// class need to be start with capital (upper camial case for tests)
public class registerService {

    public static AuthData register(User inputUser){
        if(getUser(inputUser) != null){
            createUser(inputUser);
            return createAuth(inputUser);
        }
        return null ; // should actually return exception

    }

    public static User getUser(User inputUser){
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        return userDao.findUser(inputUser) ;
    }
    public static void createUser(User user){
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        if(getUser(user) == null)
            userDao.addUser(user) ;
    }
    public static AuthData createAuth(User user){
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
            return authDao.createAuth(user) ;
    }

}
