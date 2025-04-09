package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand {
    private ChessMove move ;
    public MakeMoveCommand(ChessMove move){
        this.move = move ;
    }
    public ChessMove getMove() {
        return move ;
    }
}
