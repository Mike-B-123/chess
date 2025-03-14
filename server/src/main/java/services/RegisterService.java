package services;
import dataaccess.* ;
import model.AuthData;
import model.User;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

import java.sql.SQLException;


public class RegisterService {

    public static AuthData register(User inputUser) throws BadRequest400, Taken403, UniqueError500, DataAccessException, Unauthorized401{
            if (inputUser.username() == null || inputUser.password() == null) {
                throw new BadRequest400();
            }
            if (getUser(inputUser) == null) {
                createUser(inputUser);
                return createAuth(inputUser);
            }
            throw new Taken403();

    }

    public static User getUser(User inputUser) throws DataAccessException, Unauthorized401 {
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        return userDao.findUser(inputUser) ;
    }
    public static void createUser(User user) throws DataAccessException, Unauthorized401 {
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        if(getUser(user) == null) {
            userDao.addUser(user);
        }
    }
    public static AuthData createAuth(User user) throws DataAccessException {
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        return authDao.createAuth(user) ;
    }

}
