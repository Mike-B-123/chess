package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.* ;
import model.Game;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage ;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;



@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDAO ;
    private UserDAO userDAO ;
    private String whiteUserName ;
    private String blackUserName ;
    private String currentUserName ;
    HelperMethods helperMethods = HelperMethods.getInstance();
    public WebSocketHandler(GameDAO inputGameDAO, UserDAO inputUserDAO) {
        this.gameDAO = inputGameDAO ;
        this.userDAO = inputUserDAO ;
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
        var message = "A new user has connected to the game!" ;
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION);
        // can i change the server message class?
        connections.broadcastMultiple(command, notification);
        message = "You are now connected!" ; // how should I be putting this in the child class?
        notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastIndividual(command, notification);
        // can i change the server message class?
    }
    // should we not be throwing excveptions? but instead Erros?
    private void makeMove(UserGameCommand command, MakeMoveCommand moveCommand) throws Exception {
        ChessGame game = gameDAO.getGame(command.getGameID()).game() ;
        ChessMove move = moveCommand.getMove() ;
        if(game.validMoves(move.getStartPosition()).contains(move)) {
            if()
                game.makeMove(move); // will this actually update the database?
                var message = "The other player has made a move!";
                var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
                notification.setMessage(message);
                connections.broadcastMultiple(command, notification);
                message = "nice move!";
                notification.setMessage(message);
                connections.broadcastIndividual(command, notification);
            }
        // ask about checkmate and so on
        else{
            var message = "That is an invalid move. Try again!";
            var notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR);
            notification.setMessage(message);
            connections.broadcastIndividual(command, notification);
        }
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
// needs a check for if the perosn is a player or observer (use GameDAO to do this)
// Notification should be a server message
