package services;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.User;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

public class logoutService {
    public static String logout(String authToken) throws Unauthorized401{
            AuthDAO authDao = MemoryAuthDAO.getInstance();
            if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                return authDao.deleteAuth(authToken);
            }
            throw new Unauthorized401();
    }
}
