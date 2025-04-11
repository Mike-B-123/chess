package websocket;
import chess.ChessMove;
import com.google.gson.Gson;
import ui.Board;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint{
    Session session;
    ServerMessageObserver notificationHandler;

    public WebSocketFacade(String url, ServerMessageObserver notificationHandler) throws Exception {
    try { // the notification Handler handles displaying recieved message, so it deals with UI work. Dont print is WS
        url = url.replace("http", "ws");
        URI socketURI = new URI(url + "/ws");
        this.notificationHandler = notificationHandler;

        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, socketURI);

        //set message handler
        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
                    Board.main(null);
                }
                notificationHandler.notify(serverMessage);
            }
        });
    } catch (Exception ex) {
        throw new Exception(ex.getMessage());
    }
}


//Endpoint requires this method, but you don't have to do anything
@Override // should I get rid of this?
public void onOpen(Session session, EndpointConfig endpointConfig) {
}
    public void connect(String authToken, Integer gameID) throws Exception {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage()) ;
        }
    }
    public void makeMove(String authToken, Integer gameID, ChessMove move) throws Exception {
        try {
            var action = new MakeMoveCommand(move, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (Exception ex) {
            throw new Exception(ex.getMessage()) ;
        }
    }
public void leave(String authToken, Integer gameID) throws Exception {
    try {
        var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        this.session.close();
    } catch (Exception ex) {
        throw new Exception(ex.getMessage()) ;
    }
}

public void resign(String authToken, Integer gameID) throws Exception {
    try {
        var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        this.session.close();
    } catch (Exception ex) {
        throw new Exception(ex.getMessage()) ;
    }
}

}
