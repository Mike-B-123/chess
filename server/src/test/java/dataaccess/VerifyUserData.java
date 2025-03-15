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

public class VerifyUserData {
    @Test
    public void positiveVerify() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testVerify", "testVerifyPassWord", "testVerifyEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        String username = authDao.getUsernameFromAuth(authData.authToken()) ;
        Assertions.assertTrue(userDao.verifyUser(username, testUser.password()));
    }

    @Test
    public void negitiveVerify() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUserOne = new User("testUserName", "PassWord1", "testEmail") ;
        String password = "PassWord2647";
        userDao.addUser(testUserOne);
        AuthData authData = authDao.createAuth(testUserOne) ;
        Boolean exceptionThrown ;
        try{
            Boolean checkTwo = authDao.verifyAuth(authData.authToken()) ;
            if(!userDao.verifyUser(testUserOne.username(), password) && checkTwo){
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
