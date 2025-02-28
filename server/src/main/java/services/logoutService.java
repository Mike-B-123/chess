package services;

import dataaccess.AuthDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.User;

public class logoutService {
    public static String logout(String authToken){
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        if(authDao.verifyAuth(authToken) != null){
            return authDao.deleteAuth(authToken);
        }
        return null ; // should actually return exception

    }
}
