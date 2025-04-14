package websocket.messages;

import chess.ChessGame;
import model.Game;

public class LoadGameMessage extends ServerMessage{
    private ChessGame game;
    private String message ;
    public LoadGameMessage(ChessGame game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game ;
    }
    public ChessGame getGame() {
        return game;
    }
    public void setMessage(String inputMess) {
        this.message = inputMess ;
    }
    public String getMessage() {
        return message;
    }
}
