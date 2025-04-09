package server.websocket;

import com.google.gson.Gson;
import dataaccess.* ;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage ;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;



@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDAO ;
    public WebSocketHandler(GameDAO inputGameDAO) {
        this.gameDAO = inputGameDAO ;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(command);
            case LEAVE ->  leave(command);
            case RESIGN -> resign(command);
        }
    }

    private void connect(UserGameCommand command, Session session) throws Exception {
        connections.addAuthMap(command.getAuthToken(), session, command.getGameID());
        var message = "A new user has connected to the game!" ; // Should I be sending a message along with the notification?
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(message);// can i change the server message class?
        connections.broadcast(command, notification);
    }
    private void makeMove(UserGameCommand command) throws Exception {
        var message = "The other player has made a move! Your turn!";
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        notification.setMessage(message);
        connections.broadcast(command, notification);
    }
    private void leave(UserGameCommand command) throws Exception {
        connections.removeAuthMap(command.getAuthToken());
        var message = "A user has left the Game :(" ;
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        notification.setMessage(message);
        connections.broadcast(command, notification);
    }
    private void resign(UserGameCommand command) throws Exception {
        try {
            if() {
                connections.removeAuthMap(command.getAuthToken());
                var message = "The other player has resigned! You won!! :)";
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                notification.setMessage(message);
                connections.broadcast(command, notification);
            }
            else{
                var message = "There has been an error please try again";
                var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
                notification.setMessage(message);
                connections.au
            }
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
// needs a check for if the perosn is a player or observer
// Notification should be a server message
