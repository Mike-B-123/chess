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
    public Boolean verifyAuth(String authToken) {
        return auths.get(authToken) != null ;
    }
    public AuthData createAuth(User inputUser) {
        String authString = generateToken() ;
        auths.put(authString, inputUser.username());
        AuthData newAuth = new AuthData(authString, inputUser.username()) ;
        return newAuth ;
    }

    public void deleteAuth(String authToken) {
        auths.remove(authToken) ;
    }

    public void clearAuths(){
        auths.clear();
    }

    public String getUsernameFromAuth(String authToken){return auths.get(authToken) ;}

    public static MemoryAuthDAO getInstance(){
        if(instance == null){
            return instance = new MemoryAuthDAO() ;
        }
        return instance ;
    }
}
