package service;

import model.AuthData;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.UniqueError500;
import services.clearService;
import services.registerService;

public class RegisterTest {

    @Test
    public void positiveRegister() throws UniqueError500, BadRequest400, Taken403 {
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = registerService.register(testUser) ;
        String authToken = authData.authToken();
        String userName = authData.username();
        Assertions.assertEquals(userName, testUser.username());
        Assertions.assertNotNull(authToken);
    }

    @Test
    public void negitiveRegister() throws UniqueError500, BadRequest400, Taken403 {
        User testUser = new User(null, "testPassWord", "testEmail") ;
        AuthData authData = new AuthData("ashdfjkahsdfkjh", "testUser") ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            registerService.register(testUser);
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
