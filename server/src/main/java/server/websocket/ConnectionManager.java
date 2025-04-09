package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private ConcurrentHashMap<String, Connection> authMap = new ConcurrentHashMap<>() ;
    public void addAuthMap(String inputAuth, Session inputSession, Integer inputGameID) throws Exception{
       authMap.put(inputAuth,  new Connection(inputAuth, inputSession, inputGameID)) ;
    }
    public void removeAuthMap(String inputAuth){
        authMap.remove(inputAuth) ;
    }
//    public void addGameMap(Integer gameID, String authToken) throws Exception{
//        gameMap.put(gameID,  authToken) ;
//    }
//    public void removeGameMap(Integer gameID){
//        gameMap.remove(gameID) ;
//    }

    public void broadcast(UserGameCommand command, ServerMessage message) throws Exception {
        var removeList = new ArrayList<Connection>();
        for (var con : authMap.values()) {
            if (con.session.isOpen()) {
                if (!con.authToken.equals(command.getAuthToken()) && con.gameID == command.getGameID()) {
                    con.send(message.toString());
                }
            }
            else {
                removeList.add(con);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            authMap.remove(c.authToken);
        }

    // broadcast let's everyone know if someone has joined the petShop, properly not needed except maybe joining a game
    // change broadcast to broadcast to everyone in one game
// talk to TA about who we should be broadcasting to and if the joining of players should be broadcasted from here
} // client is the one that checsk for the observer
