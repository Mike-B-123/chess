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
import websocket.messages.ErrorMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage ;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Timer;



@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDAO ;
    private AuthDAO authDAO ;
    private String whiteUserName ;
    private String blackUserName ;
    private String currentUserName ;
    HelperMethods helperMethods = HelperMethods.getInstance();
    public WebSocketHandler(GameDAO inputGameDAO, AuthDAO inputAuthDAO) {
        this.gameDAO = inputGameDAO ;
        this.authDAO = inputAuthDAO ;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);

        blackUserName = gameDAO.getGame(command.getGameID()).blackUsername() ;
        whiteUserName = gameDAO.getGame(command.getGameID()).whiteUsername() ;
        currentUserName = authDAO.getUsernameFromAuth(command.getAuthToken());
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE ->  leave(command);
            case RESIGN -> resign(command);
        }
    }

    private void connect(UserGameCommand command, Session session) throws Exception {
        connections.addAuthMap(command.getAuthToken(), session, command.getGameID());
        var message = "A new user has connected to the game!" ;
        var notification = new NotificationMessage(message);
        connections.broadcastMultiple(command, notification);
        message = "You are now connected!" ;
        notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME);
        connections.broadcastIndividual(command, notification);
    }
    private void makeMove(String message) throws Exception {
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        try {
            ChessGame game = gameDAO.getGame(command.getGameID()).game();
            ChessMove move = command.getMove();
            String checking = helperMethods.mateCheck(game, move, game.getTeamTurn());
            if (game.validMoves(move.getStartPosition()).contains(move)) {
                if (!checking.contains("False")) {
                    var notification = new NotificationMessage(checking);
                    connections.broadcastMultiple(command, notification);
                    connections.broadcastIndividual(command, notification);
                }
                game.makeMove(move);
                gameDAO.updateGame(game, command.getGameID());
                var notification = new NotificationMessage("The other player has made a move!");
                connections.broadcastMultiple(command, notification);
                notification.setNotificationMessage("Nice Move!");
                connections.broadcastIndividual(command, notification);
            } else {
                var notification = new ErrorMessage("That is an invalid move. Try again!");
                connections.broadcastIndividual(command, notification);
            }
        } catch (Exception ex) {
            var notification = new ErrorMessage("An error occured when attempting the move. Try again!");
            connections.broadcastIndividual(command, notification);
        }
    }
    private void leave(UserGameCommand command) throws Exception {
        try {
            String enemy = "placeholder";
            connections.removeAuthMap(command.getAuthToken());
            var notification = new NotificationMessage(String.format(" %s has left the Game :( %s wins!", currentUserName, enemy));
            connections.broadcastMultiple(command, notification);
            connections.broadcastIndividual(command, notification);
        }
        catch (Exception ex){
            var notification = new ErrorMessage("You are unable to leave the game. Try again!") ;
            connections.broadcastIndividual(command, notification);
        }
        // if it's an observer do I need to update the game?
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
//Questions:
// 1. if it's an observer do I need to update the game?


// 2. should we not be throwing excveptions? but instead Errors? (do a try catch exception, but instead it return ERROR)
// needs a check for if the perosn is a player or observer (use GameDAO to do this)
// Notification should be a server message
