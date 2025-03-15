package services;
import dataaccess.* ;
import responses.errors.UniqueError500;

public class ClearService {
    public static Object clear() throws UniqueError500{
        try{
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        GameDAO gameDao = MySQLGameDAO.getInstance() ;
        userDao.clearUsers();
        authDao.clearAuths();
        gameDao.clearGame();}
        catch(Exception ex){
            ex.printStackTrace();
            throw new UniqueError500() ;
        }
        return "{}" ;
    }

}
