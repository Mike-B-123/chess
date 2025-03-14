package dataaccess;

import model.*;

import java.sql.SQLException;

public interface AuthDAO {
    public AuthData createAuth(User inputUser) throws DataAccessException;
    public Boolean verifyAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public String getUsernameFromAuth(String authToken) throws DataAccessException;
    public void clearAuths() throws DataAccessException;
}
