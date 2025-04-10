package websocket;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

public class WebSocketFacade {
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
                notificationHandler.notify(serverMessage);
            }
        });
    } catch (Exception ex) {
        throw new Exception(ex.getMessage());
    }
}

//Endpoint requires this method, but you don't have to do anything
@Override
public void onOpen(Session session, EndpointConfig endpointConfig) {
}

public void enterPetShop(String visitorName) throws Exception {
    try {
        var action = new Action(Action.Type.ENTER, visitorName);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
    } catch (IOException ex) {
        throw new ResponseException(500, ex.getMessage());
    }
}

public void leavePetShop(String visitorName) throws Exception {
    try {
        var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, visitorName);
        this.session.getBasicRemote().sendText(new Gson().toJson(action));
        this.session.close();
    } catch (Exception ex) {
        throw new Exception(ex.getMessage()) ;
    }
}

}
}
