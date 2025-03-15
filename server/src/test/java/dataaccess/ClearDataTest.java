package dataaccess;

import model.AuthData;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.UniqueError500;
import services.ClearService;

public class ClearDataTest {
    String testClear = "{}" ;
    @Test
    public void positiveClearGame() throws UniqueError500, DataAccessException {
        GameDAO gameDao = MySQLGameDAO.getInstance() ;
        Assertions.assertEquals(testClear, ClearService.clear().toString());
    }
    @Test
    public void positiveClearAuth() throws UniqueError500, DataAccessException {
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User inputUser = new User("username","pass", "email") ;
        userDao.addUser(inputUser);
        AuthData authData = authDao.createAuth(inputUser) ;
        authDao.clearAuths();
        Assertions.assertEquals(testClear, authDao.getUsernameFromAuth(authData.authToken()));
    }
}
