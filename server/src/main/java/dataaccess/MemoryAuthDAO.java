package dataaccess;

import model.AuthData;
import model.User;
import java.util.UUID;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
    private HashMap<String, String> auths = new HashMap<>();
    private static MemoryAuthDAO instance ;


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public Boolean verifyAuth(AuthData authData) {
        return auths.get(authData.authToken()) != null ;
    }
    public AuthData createAuth(User inputUser) {
        String AuthString = generateToken() ;
        AuthData newAuth = new AuthData(AuthString, inputUser.username()) ;
        auths.put(newAuth.authToken(), inputUser.username());
        return newAuth ;
    }

    public void deleteAuth(AuthData authToken) {
        auths.remove(inputUser.username()) ;
    }

    public void clearAuths(){
        auths.clear();
    }

    public static MemoryAuthDAO getInstance(){
        if(instance == null){
            return instance = new MemoryAuthDAO() ;
        }
        return instance ;
    }
}
