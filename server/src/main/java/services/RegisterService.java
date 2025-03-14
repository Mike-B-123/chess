package services;
import dataaccess.* ;
import model.AuthData;
import model.User;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.UniqueError500;

// class need to be start with capital (upper camial case for tests)
public class RegisterService {

    public static AuthData register(User inputUser) throws BadRequest400, Taken403, UniqueError500, DataAccessException {
            if (inputUser.username() == null || inputUser.password() == null) {
                throw new BadRequest400();
            }
            if (getUser(inputUser) == null) {
                createUser(inputUser);
                return createAuth(inputUser);
            }
            throw new Taken403();

    }

    public static User getUser(User inputUser) throws DataAccessException {
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        return userDao.findUser(inputUser) ;
    }
    public static void createUser(User user) throws DataAccessException {
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        if(getUser(user) == null) {
            userDao.addUser(user);
        }
    }
    public static AuthData createAuth(User user){
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        return authDao.createAuth(user) ;
    }

}
