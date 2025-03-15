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

public class DeleteAuthTest {
    public void positiveDelete() throws UniqueError500, BadRequest400, Taken403, Unauthorized401, DataAccessException {
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        UserDAO userDao = MySQLUserDAO.getInstance() ;
        User testUser = new User("testUserName", "testPassWord", "testEmail") ;
        userDao.addUser(testUser);
        AuthData authData = authDao.createAuth(testUser) ;
        String authToken = authData.authToken();
        authDao.deleteAuth(authToken);
        Assertions.assertEquals(null, authDao.verifyAuth(authToken));
    }

    @Test
    public void negitiveDelete() throws UniqueError500, BadRequest400, Taken403, DataAccessException{
        ClearService.clear() ;
        AuthDAO authDao = MySQLAuthDAO.getInstance() ;
        Boolean exceptionThrown = Boolean.FALSE ;
        try{
            String authToken = "nhnuhnuhn";
            authDao.deleteAuth(authToken);
            if(authDao.getUsernameFromAuth(authToken) == null){
                throw new DataAccessException("did not delete") ;
            };
        }
        catch(Exception ex){ // double check that this was ok to look up
            exceptionThrown = Boolean.TRUE ;
        }
        Assertions.assertEquals(Boolean.TRUE, exceptionThrown);
    }
}
