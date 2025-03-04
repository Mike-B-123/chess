package services;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import responses.errors.Unauthorized401;

public class LogoutService {
    public static String logout(String authToken) throws Unauthorized401{
            AuthDAO authDao = MemoryAuthDAO.getInstance();
            if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                authDao.deleteAuth(authToken);
                return "{}" ;
            }
            throw new Unauthorized401();
    }
}
