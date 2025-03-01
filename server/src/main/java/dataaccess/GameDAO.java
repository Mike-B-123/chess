package dataaccess;

import model.Game;
import model.User;

import java.util.HashMap;

public interface GameDAO {
    public void createGame(String inputGameName) ;
    public void insertGame(Game gameData) ;
    public Game getGame(String gameName) ;
    public HashMap<Integer , Game> listAllGames(User inputUser) ;
    public void clearGame() ;
}
