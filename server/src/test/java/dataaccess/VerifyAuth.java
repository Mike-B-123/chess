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

public class VerifyAuth {
    @Test
    public void positiveVerifyAuth() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = authDao.createAuth(testUser) ;
        Assertions.assertTrue(authDao.verifyAuth(authData.authToken()));
    }

    @Test
    public void negitiveVerifyAuth() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            if(!authDao.verifyAuth("wrong")){
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
