package service;


import chess.ChessGame;
import model.AuthData;
import model.Game;
import model.JoinData;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import services.*;

public class ListGamesTest {

    @Test
    public void positiveList() throws UniqueError500, BadRequest400, Taken403, Unauthorized401 {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = RegisterService.register(testUser) ;
        Game outputGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        JoinGameService.joinGame(authData.authToken(), joinData);
        ListGamesService.listGames(authData.authToken()) ;
        Assertions.assertEquals("testUserName", ListGamesService.listGames(authData.authToken()).get(outputGame.gameID()).blackUsername());
    }

    @Test
    public void negitiveList() throws UniqueError500, BadRequest400, Taken403, Unauthorized401 {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = RegisterService.register(testUser) ;
        Game outputGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        JoinGameService.joinGame(authData.authToken(), joinData);
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            Game outPutErrorGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
            ListGamesService.listGames(null) ;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
