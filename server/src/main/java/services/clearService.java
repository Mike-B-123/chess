package services;
import dataaccess.* ;
import responses.errors.UniqueError500;

public class clearService {
    public static Object clear(){
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        GameDAO gameDao = MemoryGameDAO.getInstance() ;
        if(userDao == null){
            throw UniqueError500 ;
        }
        userDao.clearUsers();
        authDao.clearAuths();
        gameDao.clearGame();
        return "{}" ;
    }

}
