package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
import model.Game;

public class HelperMethods {
    private static HelperMethods instance ;
    public String[] mateCheck(ChessGame game, ChessMove move, ChessGame.TeamColor teamColor){
        String[] possibilities = {} ;
        // how do I return a tuple? I mean I could do a record class but that seems silly
        if(game.isInCheckmate(teamColor)){
            possibilities[0] = {true, String.format("%s is in check!", teamColor)} ;
        } else if (game.isInStalemate(teamColor)) {

        }
        return possibilities ;
    }
    public static HelperMethods getInstance(){
        if(instance == null){
            return instance = new HelperMethods() ;
        }
        return instance ;
    }
}
