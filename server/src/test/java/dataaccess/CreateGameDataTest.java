package dataaccess;

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

public class CreateGameDataTest {
    @Test
    public void positiveCreate() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        User testUser = new User("testUserName1", "testPassWord1", "testEmail1") ;
        AuthData authData = RegisterService.register(testUser) ;
        Boolean testData = Boolean.FALSE ;
        Game outputGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
        if(outputGame.gameID() > -1 ){
            testData = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, testData);
    }

    @Test
    public void negitiveCreate() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        GameDAO gameDao = MySQLGameDAO.getInstance() ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            gameDao.createGame(null) ;
        }
        catch(Exception ex){
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
