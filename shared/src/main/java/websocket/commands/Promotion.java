package websocket.commands;

import chess.ChessMove;
import chess.ChessPiece;

public class Promotion extends UserGameCommand{
    private ChessPiece.PieceType type ;
    public Promotion(ChessPiece.PieceType type, String authToken, Integer gameID){
        super(CommandType.MAKE_MOVE,authToken, gameID);
        this.type = type ;
    }
    public ChessPiece.PieceType getType() {
        return type ;
    }
}
