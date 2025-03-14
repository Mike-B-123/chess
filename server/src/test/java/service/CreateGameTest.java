package service;

import dataaccess.DataAccessException;
import model.AuthData;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import services.ClearService;
import services.CreateGamesService;
import services.RegisterService;

public class CreateGameTest {
    @Test
    public void positiveCreate() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = RegisterService.register(testUser) ;
        Boolean test = Boolean.FALSE ;
        Game outputGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
        if(outputGame.gameID() > -1 ){
            test = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, test);
    }

    @Test
    public void negitiveCreate() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = RegisterService.register(testUser) ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            CreateGamesService.create("hasdkfhakjsdhfk", "newTestGame") ;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
