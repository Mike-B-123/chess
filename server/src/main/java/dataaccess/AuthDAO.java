package dataaccess;

import model.*;

public interface AuthDAO {
    public AuthData createAuth(User inputUser);
    public Boolean verifyAuth(String authToken) ;
    public String deleteAuth(String authToken) ;
    public void clearAuths() ;
}
