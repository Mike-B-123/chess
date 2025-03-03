package dataaccess;

import model.Game;
import model.User;

import java.util.HashMap;

public interface GameDAO {
    public Integer createGame(String inputGameName) ;
    public void insertGame(Game gameData) ;
    public Game getGame(String gameName) ;
    public HashMap<Integer , Game> listAllGames() ;
    public void clearGame() ;
}
