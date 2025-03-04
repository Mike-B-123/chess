package service;

import chess.ChessGame;
import dataaccess.MemoryGameDAO;
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
import services.ClearService;
import services.CreateGamesService;
import services.JoinGameService;
import services.RegisterService;

public class JoinTest {
    @Test
    public void positiveJoin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401 {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = RegisterService.register(testUser) ;
        Game outputGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        JoinGameService.joinGame(authData.authToken(), joinData);
        MemoryGameDAO gameDAO = MemoryGameDAO.getInstance() ;
        Assertions.assertEquals("testUserName", gameDAO.getGame(joinData.gameID()).blackUsername());
    }

    @Test
    public void negitiveJoin() throws UniqueError500, BadRequest400, Taken403 {
        ClearService.clear() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = RegisterService.register(testUser) ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            Game outPutErrorGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
            JoinData testJoinData = new JoinData(ChessGame.TeamColor.BLACK, outPutErrorGame.gameID()) ;
            JoinGameService.joinGame("hdkjfhaskdhf", testJoinData);
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
