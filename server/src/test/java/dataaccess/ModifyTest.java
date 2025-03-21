package dataaccess;

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
import services.ClearService;
import services.CreateGamesService;

public class ModifyTest {
    @Test
    public void positiveJoin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        GameDAO gameDao = MySQLGameDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        Game outputGame = gameDao.createGame("newTestGame") ;
        gameDao.getGame(outputGame.gameID()) ;
        gameDao.availableGame(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        gameDao.modifyInsert(authData.authToken(), joinData.playerColor(), joinData.gameID());
        Assertions.assertEquals("testUserName", gameDao.getGame(joinData.gameID()).blackUsername());
    }

    @Test
    public void negitiveJoin() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        GameDAO gameDao = MySQLGameDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            Game outPutErrorGame = CreateGamesService.create(authData.authToken(), "newTestGame") ;
            gameDao.getGame(outPutErrorGame.gameID()) ;
            gameDao.modifyInsert(authData.authToken(), ChessGame.TeamColor.BLACK, outPutErrorGame.gameID());
            JoinData testJoinData = new JoinData(ChessGame.TeamColor.BLACK, outPutErrorGame.gameID()) ;
            gameDao.availableGame(ChessGame.TeamColor.BLACK, testJoinData.gameID()) ;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
