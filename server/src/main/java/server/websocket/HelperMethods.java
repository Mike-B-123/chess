package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.Game;
import model.GameID;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;

import java.util.Objects;

public class HelperMethods {
    private static HelperMethods instance ;
    public String mateCheck(ChessGame game, ChessMove move, ChessGame.TeamColor teamColor){
        // how do I return a tuple? I mean I could do a record class but that seems silly
        ChessGame.TeamColor enemy = ChessGame.TeamColor.BLACK;
        if(teamColor == ChessGame.TeamColor.BLACK){
            enemy = ChessGame.TeamColor.WHITE ;
        }
        if(game.isInCheckmate(enemy)){
            return String.format("That's checkmate! %s wins!", teamColor) ;
        } else if (game.isInStalemate(teamColor)) {
            return "It's a stalemate!" ;
        } else if (game.isInCheck(enemy)) {
            return String.format("%s is in Check!", enemy) ;
        }
        else if (game.isInCheck(teamColor)) {
            return String.format("%s is in Check!", teamColor);
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
