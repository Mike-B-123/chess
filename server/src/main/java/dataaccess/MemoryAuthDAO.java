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
        String AuthString = generateToken() ;
        auths.put(AuthString, inputUser.username());
        AuthData newAuth = new AuthData(AuthString, inputUser.username()) ;
        return newAuth ;
    }

    public String deleteAuth(String authToken) {
        return auths.remove(authToken) ;
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
