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
import services.LoginService;
import services.RegisterService;

public class LoginDataTest {
    @Test
    public void positiveLogin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        String username = authDao.getUsernameFromAuth(authData.authToken()) ;
        Assertions.assertEquals(testUser.username(), username);
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    public void negitiveLogin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUserOne = new User("testUserName", "PassWord1", "testEmail") ;
        User testUserTwo = new User("testUserName2", "PassWord2647", "testEmail") ;
        userDao.addUser(testUserOne);
        authDao.createAuth(testUserOne) ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            if(!userDao.verifyUser(testUserTwo.username(), testUserTwo.password())){
                throw new DataAccessException("wrong password") ;
            }
            return;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
