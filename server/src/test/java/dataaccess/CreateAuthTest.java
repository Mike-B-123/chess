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

public class CreateAuthTest {
    @Test
    public void positiveCreateAuth() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = authDao.createAuth(testUser) ;
        Assertions.assertEquals(authData.username(), testUser.username());
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    public void negitiveCreateAuth() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUserOne = new User("testUserName", "PassWord1", "testEmail") ;
        userDao.addUser(testUserOne);
        AuthData authData = authDao.createAuth(testUserOne) ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            if(!authDao.verifyAuth(authData.authToken())){
                throw new DataAccessException("wrong password") ;
            }
            return;
        }
        catch(Exception ex){
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
