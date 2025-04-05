package server.websocket;
import org.eclipse.jetty.websocket.api.Session;
public class Connection {
    String username ;
    Session session ;
    public Connection(String inputName, Session inputSession){
        this.username = inputName ;
        this.session = inputSession ;
    }
    public Session getSession(){
        return session ;
    }
}
