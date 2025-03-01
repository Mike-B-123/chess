package services;
import dataaccess.* ;
import responses.errors.UniqueError500;

public class clearService {
    public static Object clear() throws UniqueError500{
        try{
        UserDAO userDao = MemoryUserDAO.getInstance() ;
        AuthDAO authDao = MemoryAuthDAO.getInstance() ;
        GameDAO gameDao = MemoryGameDAO.getInstance() ;
        userDao.clearUsers();
        authDao.clearAuths();
        gameDao.clearGame();}
        catch(Exception ex){
            throw new UniqueError500() ;
        }
        return "{}" ;
    }

}
