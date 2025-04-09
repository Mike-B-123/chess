package server.websocket;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;

public class Connection {
    String authToken ;
    Session session ;
    Integer gameID ;
    public Connection(String inputAuthToken, Session inputSession, Integer inputGameID){
        this.authToken = inputAuthToken ;
        this.session = inputSession ;
        this.gameID = inputGameID ;
    }
    public Session getSession(){
        return session ;
    }
    public void send(String msg) throws Exception {
        session.getRemote().sendString(msg); // does this send the right TYPE?
    }
}
