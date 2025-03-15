package dataaccess;

import model.AuthData;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import services.ClearService;
import services.LogoutService;
import services.RegisterService;


public class LogoutDataTest {
    @Test
    public void positiveLogout() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        String authToken = authData.authToken();
        authDao.deleteAuth(authToken);
        Assertions.assertEquals(null, authDao.verifyAuth(authToken));
    }

    @Test
    public void negitiveLogout() throws UniqueError500, BadRequest400, Taken403, DataAccessException{
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User(null, "testPassWord", "testEmail") ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            userDao.addUser(testUser);
            authDao.createAuth(testUser) ;
            authDao.deleteAuth("nhnuhnuhn");
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
