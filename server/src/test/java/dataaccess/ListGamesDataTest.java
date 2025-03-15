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
import services.*;

public class ListGamesDataTest {
    @Test
    public void positiveList() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        GameDAO gameDao = MySQLGameDAO.getInstance() ;

        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        Game outputGame = gameDao.createGame("newTestGame") ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        gameDao.modifyInsert(authData.authToken(), joinData.playerColor(), joinData.gameID());
        Assertions.assertEquals("testUserName", gameDao.listAllGames().get(joinData.gameID()).blackUsername());
    }

    @Test
    public void negitiveList() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        GameDAO gameDao = MySQLGameDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        Game outputGame = gameDao.createGame("newTestGame") ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.BLACK, outputGame.gameID()) ;
        gameDao.modifyInsert(authData.authToken(), joinData.playerColor(), joinData.gameID());;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            Game outPutErrorGame = gameDao.createGame("newTestGame") ;
            ListGamesService.listGames(null) ;
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
