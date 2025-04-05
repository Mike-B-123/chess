package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private ConcurrentHashMap<String, Connection> usernameMap = new ConcurrentHashMap<>() ;
    private ConcurrentHashMap<String, Connection> connectionMap = new ConcurrentHashMap<>() ;
    public void addUsernameMap(String inputUsername, Session inputSession) throws Exception{
       usernameMap.put(inputUsername,  new Connection(inputUsername, inputSession)) ;
    }
    public void removeUsernameMap(String inputUsername){
        usernameMap.remove(inputUsername) ;
    }
    public void addConnectioneMap(String inputUsername, Session inputSession) throws Exception{
        connectionMap.put(inputUsername,  new Connection(inputUsername, inputSession)) ;
    }
    public void remove(String inputUsername){
        connectionMap.remove(inputUsername) ;
    }

    // broadcast let's everyone know if someone has joined the petShop, properly not needed except maybe joining a game
// talk to TA about who we should be broadcasting to and if the joining of players should be broadcasted from here
}
