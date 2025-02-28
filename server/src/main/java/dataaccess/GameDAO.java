package dataaccess;

import model.Game;
import model.User;

import java.util.HashMap;

public interface GameDAO {
    public void createGame(User inputUser) ;
    public void insertGame(Game gameData) ;
    public Game getGame(String gameName) ;
    public HashMap<String, Game> listAllGames(User inputUser) ;
    public void clearGame() ;
}
