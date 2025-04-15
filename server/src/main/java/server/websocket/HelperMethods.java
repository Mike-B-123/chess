package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Game;
import model.GameID;
import model.User;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;

import java.util.Objects;

public class HelperMethods {
    private static HelperMethods instance ;
    public String mateCheck(Game orGame, ChessMove move, ChessGame.TeamColor color, MakeMoveCommand com) throws InvalidMoveException, DataAccessException {
        // how do I return a tuple? I mean I could do a record class but that seems silly
        ChessGame game = orGame.game() ;
        String White = orGame.whiteUsername() ;
        String Black = orGame.blackUsername();
        String userName = White ;
        ChessGame.TeamColor enemy = ChessGame.TeamColor.BLACK;
        String enemyUer = Black ;
        String middle = new Gson().toJson(game) ;
        ChessGame testGame = new Gson().fromJson(middle, ChessGame.class) ;
        testGame.makeMove(move);
        if(color == ChessGame.TeamColor.BLACK){
            enemy = ChessGame.TeamColor.WHITE ;
            enemyUer = White ;
            userName = Black ;
        }
        if(testGame.isInCheckmate(enemy)){
            return String.format("%s is in  checkmate! %s wins!", enemyUer, userName) ;
        }
        else if(testGame.isInCheckmate(color)) {
            return String.format("%s is in  checkmate! %s wins!", userName, enemyUer) ;
        }else if (testGame.isInStalemate(color)) {
            return "It's a stalemate!" ;
        } else if (testGame.isInCheck(enemy)) {
            return String.format("%s is in Check!", enemyUer) ;
        }
        else if (testGame.isInCheck(color)) {
            return String.format("%s is in Check!", userName);
        }
        return "False" ;
    }
    public Boolean falseInfo(GameDAO gDAO, AuthDAO aDAO, UserGameCommand com, Session ses) throws Exception {
        if(gDAO.getGame(com.getGameID()) == null){
            Connection con = new Connection(com.getAuthToken(), ses, com.getGameID());
            con.send(new Gson().toJson(new ErrorMessage("This game does not exist!")));
            return true ;
        }
        if(aDAO.verifyAuth(com.getAuthToken())== null){
            Connection con = new Connection(com.getAuthToken(), ses, com.getGameID());
            con.send(new Gson().toJson(new ErrorMessage("This user id not authorized!")));
            return true ;
        }
        return false ;
    }

    public static HelperMethods getInstance(){
        if(instance == null){
            return instance = new HelperMethods() ;
        }
        return instance ;
    }
}
