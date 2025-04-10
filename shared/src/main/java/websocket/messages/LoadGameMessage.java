package websocket.messages;

import chess.ChessGame;
import model.Game;

public class LoadGameMessage extends ServerMessage{
    private ChessGame game;
    public LoadGameMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game ;
    }
    public ChessGame getGame() {
        return game;
    }
}
