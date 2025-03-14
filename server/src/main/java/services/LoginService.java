package services;

import dataaccess.*;
import model.AuthData;
import model.User;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

import java.sql.SQLException;
import java.util.Objects;

public class LoginService {
    public static AuthData login(User inputUser) throws Unauthorized401, UniqueError500, DataAccessException{
        UserDAO userDAO = MySQLUserDAO.getInstance() ;
        if (getUser(inputUser) != null) {
            if (!userDAO.verifyUser(inputUser.username(), inputUser.password())) {
                throw new Unauthorized401();
            }
            return createAuth(inputUser);
        }
        throw new Unauthorized401() ;
    }

    public static User getUser(User inputUser) throws DataAccessException, Unauthorized401 {
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        return userDao.findUser(inputUser) ;
    }

    public static AuthData createAuth(User user) throws DataAccessException {
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        return authDao.createAuth(user) ;
    }
}
