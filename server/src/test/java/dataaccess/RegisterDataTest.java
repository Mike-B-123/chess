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
import services.RegisterService;

public class RegisterDataTest {
    @Test
    public void positiveRegister() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        userDao.findUser(testUser) ;
        AuthData authData = authDao.createAuth(testUser);
        Assertions.assertEquals(authData.username(), testUser.username());
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    public void negitiveRegister() throws UniqueError500, BadRequest400, Taken403, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            userDao.addUser(testUser);
            authDao.createAuth(testUser);
            userDao.findUser(testUser) ;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
