package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import model.Game;

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
    public static HelperMethods getInstance(){
        if(instance == null){
            return instance = new HelperMethods() ;
        }
        return instance ;
    }
}
