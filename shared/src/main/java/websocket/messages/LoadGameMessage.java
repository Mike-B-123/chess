package websocket.messages;

import model.Game;

public class LoadGameMessage {
    private Game game;
    public LoadGameMessage(Game game){
        this.game = game ;
    }
    public Game getGame() {
        return game;
    }
}
