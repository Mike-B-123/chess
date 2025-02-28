package dataaccess;

import model.*;

public interface AuthDAO {
    public AuthData createAuth(User inputUser) ;
    public Boolean verifyAuth(User inputUser) ;
    public void deleteAuth(User inputUser) ;
    public void clearAuths() ;
}
