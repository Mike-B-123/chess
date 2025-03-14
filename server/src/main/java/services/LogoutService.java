package services;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MySQLAuthDAO;
import responses.errors.Unauthorized401;

public class LogoutService {
    public static String logout(String authToken) throws Unauthorized401, DataAccessException {
            AuthDAO authDao = MySQLAuthDAO.getInstance();
            if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                authDao.deleteAuth(authToken);
                return "{}" ;
            }
            throw new Unauthorized401();
    }
}
