package dataaccess;

import model.AuthData;
import model.Game;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import services.ClearService;

public class ClearDataTest {

    @Test
    public void positiveClearGame() throws UniqueError500, DataAccessException {
        GameDAO gameDao = MySQLGameDAO.getInstance();
        gameDao.clearGame();
        Game testGame = gameDao.createGame("testGame") ;
        gameDao.clearGame() ;
        Assertions.assertEquals(null, gameDao.getGame(testGame.gameID()));
    }
    @Test
    public void positiveClearUser() throws UniqueError500, DataAccessException, Unauthorized401 {
        UserDAO userDao = MySQLUserDAO.getInstance();
        userDao.clearUsers();
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        userDao.clearUsers();
        Assertions.assertEquals(null, userDao.findUser(testUser));
    }
    @Test
    public void positiveClearAuth() throws UniqueError500, DataAccessException {
        AuthDAO authDao = MySQLAuthDAO.getInstance();
        authDao.clearAuths();
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        AuthData authData = authDao.createAuth(testUser) ;
        authDao.clearAuths();
        Assertions.assertEquals(null, authDao.getUsernameFromAuth(authData.authToken()));
    }
}

//    @Test
//    public void positiveClearAuth() throws UniqueError500, DataAccessException {
//        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
//        UserDAO userDao = MySQLUserDAO.getInstance() ;
//        User inputUser = new User("username","pass", "email") ;
//        userDao.addUser(inputUser);
//        AuthData authData = authDao.createAuth(inputUser) ;
//        authDao.clearAuths();
//        Assertions.assertEquals(testClear, authDao.getUsernameFromAuth(authData.authToken()));
//    }
//}
