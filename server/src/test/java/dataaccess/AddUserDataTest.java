package dataaccess;

import model.AuthData;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import services.ClearService;

public class AddUserDataTest {
    @Test
    public void positiveAddUser() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        Assertions.assertEquals(userDao.findUser(testUser).username(), testUser.username());
    }

    @Test
    public void negitiveAddUser() throws UniqueError500, BadRequest400, Taken403, DataAccessException {
        ClearService.clear() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            userDao.addUser(testUser);
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
