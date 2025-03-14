package service;

import dataaccess.DataAccessException;
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

public class LoginTest {
    @Test
    public void positiveLogin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        RegisterService.register(testUser) ;
        AuthData authData = LoginService.login(testUser) ;
        Assertions.assertEquals(testUser.username(), authData.username());
        Assertions.assertNotNull(authData.authToken());
    }

    @Test
    public void negitiveLogin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        User testUserOne = new User("testUserName", "PassWord1", "testEmail") ;
        User testUserTwo = new User("testUserName", "PassWord2", "testEmail") ;
        RegisterService.register(testUserOne) ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            LoginService.login(testUserTwo) ;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
