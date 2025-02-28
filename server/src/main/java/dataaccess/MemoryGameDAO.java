package dataaccess;

import model.Game;
import model.User;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<String, Game> gameDatas = new HashMap<>();
    private static MemoryGameDAO instance ;
// Don't I want the hash table to have an 'int' key for GameID? but it says that's primative.
    public HashMap<String, Game> listAllGames(User inputUser) {
        return gameDatas ;
    }

    public void createGame(User inputUser) {
// How should I create a game? I understand there is a game name, but should I also assign players to Blakc and White or is that done seperately
    }
    public Game getGame(String gameName) {

    }
    public void insertGame(Game gameData) {

    }
    public void clearGame(){
            gameDatas.clear();
        }

        public static MemoryGameDAO getInstance(){
            if(instance == null){
                return instance = new MemoryGameDAO() ;
            }
            return instance ;
        }
    }
