package services;
import dataaccess.* ;
import model.AuthData;
import model.User;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

// class need to be start with capital (upper camial case for tests)
public class registerService {

    public static AuthData register(User inputUser) throws BadRequest400, Taken403, UniqueError500 {
        try{ // when should I throw bad request?
        if(getUser(inputUser) == null){
            createUser(inputUser);
            return createAuth(inputUser);
        }
            throw new Taken403();
        }
        catch(Exception ex){
            throw new UniqueError500() ;
        }

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
