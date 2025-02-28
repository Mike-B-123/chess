package services;
import dataaccess.* ;

public class clearService {
    public static Object clear(){
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        GameDAO gameDao = MemoryGameDAO.getInstance() ;
        userDao.clearUsers();
        authDao.clearAuths();
        gameDao.clearGame();
        return "{}" ;
    }

}
