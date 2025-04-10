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
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage ;
import websocket.commands.UserGameCommand;

import java.io.IOException;
import java.util.Objects;
import java.util.Timer;



@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private GameDAO gameDAO ;
    private AuthDAO authDAO ;
    private String whiteUserName ;
    private String blackUserName ;
    private String currentUserName ;
    private String enemy ;
    HelperMethods helperMethods = HelperMethods.getInstance();
    public WebSocketHandler(GameDAO inputGameDAO, AuthDAO inputAuthDAO) {
        this.gameDAO = inputGameDAO ;
        this.authDAO = inputAuthDAO ;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
       if(helperMethods.falseInfo(gameDAO,authDAO,command,session)){
           return ;
       }
        blackUserName = gameDAO.getGame(command.getGameID()).blackUsername() ;
        whiteUserName = gameDAO.getGame(command.getGameID()).whiteUsername() ;
        currentUserName = authDAO.getUsernameFromAuth(command.getAuthToken());
        if(Objects.equals(currentUserName, whiteUserName)){
            enemy = blackUserName ;
        }
        else {
            enemy = whiteUserName ;
        }
        switch (command.getCommandType()) {
            case CONNECT -> connect(command, session);
            case MAKE_MOVE -> makeMove(message);
            case LEAVE ->  leave(command);
            case RESIGN -> resign(command);
        }
    }

    private void connect(UserGameCommand command, Session session) throws Exception {
        try {
            connections.addAuthMap(command.getAuthToken(), session, command.getGameID());
            if(gameDAO.getGame(command.getGameID()) == null){
                connections.broadcastIndividual(command, new ErrorMessage("This game does not exist!"));
            }
            ChessGame game = gameDAO.getGame(command.getGameID()).game();
            var noteNotification = new NotificationMessage("A new user has connected to the game!");
            connections.broadcastMultiple(command, noteNotification);
            var loadNotification = new LoadGameMessage(game);
            connections.broadcastIndividual(command, loadNotification);
        }
        catch (Exception ex){
            var error = new ErrorMessage("An error occured while connecting. Try a new input.") ;
            connections.broadcastIndividual(command, error);
        }
    }
    private void makeMove(String message) throws Exception {
        MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
        try {
            if(!Objects.equals(currentUserName, whiteUserName) || !Objects.equals(currentUserName, blackUserName)){
                connections.broadcastIndividual(command, new ErrorMessage("You're an observer and can not make a move."));
            }
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
            connections.removeAuthMap(command.getAuthToken());
            var notification = new NotificationMessage(String.format(" %s has left the Game :( wait for a new player!", currentUserName));
            connections.broadcastMultiple(command, notification);
            // probably gonna need to create a DAO method that allows me to update the players to null
            connections.broadcastIndividual(command, new NotificationMessage("You have successfully left the game!"));
        }
        catch (Exception ex){
            var notification = new ErrorMessage("You are unable to leave the game. Try again!") ;
            connections.broadcastIndividual(command, notification);
        }
        // if it's an observer do I need to update the game?
    }
    private void resign(UserGameCommand command) throws Exception {
        try {
            if(Objects.equals(currentUserName, blackUserName) || Objects.equals(currentUserName, whiteUserName)) {
                connections.removeAuthMap(command.getAuthToken());
                var notification = new NotificationMessage(String.format(" %s has resigned! %s won!! :)", currentUserName, enemy));
                connections.broadcastMultiple(command, notification);
                connections.broadcastIndividual(command, notification);
            }
            else{
                var notification = new ErrorMessage("You are an Observer and can not resign. Please leave instead.") ;
                connections.broadcastIndividual(command, notification);
            }
        }
        catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
}
//Questions:
// 1. How do I format my server message better
//2. should I have @OnWebSocketError that is connected when an error occurs, but how do I apply unique messages?
// 2. How do I "end" a game and make it where no one can move?


// 2. should we not be throwing excveptions? but instead Errors? (do a try catch exception, but instead it return ERROR)
// needs a check for if the perosn is a player or observer (use GameDAO to do this)
// Notification should be a server message
