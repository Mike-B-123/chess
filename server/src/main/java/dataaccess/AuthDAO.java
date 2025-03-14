package dataaccess;

import model.*;

public interface AuthDAO {
    public AuthData createAuth(User inputUser) throws DataAccessException;
    public Boolean verifyAuth(String authToken) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public String getUsernameFromAuth(String authToken) throws DataAccessException;
    public void clearAuths() throws DataAccessException;
}
