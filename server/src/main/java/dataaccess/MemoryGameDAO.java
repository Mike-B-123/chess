package dataaccess;

import chess.ChessGame;
import model.Game;
import model.User;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, Game> gameDatas = new HashMap<>();
    private static MemoryGameDAO instance ;
    private int gameID = 0 ;
    public HashMap<Integer, Game> listAllGames() {
        return gameDatas ;
    }

    public Integer createGame(String inputGameName) {
        gameID ++ ;
        ChessGame inputChessGame = new ChessGame() ;
        Game newGameData = new Game(gameID, null, null, inputGameName, inputChessGame) ;
        gameDatas.put(gameID, newGameData) ;
        return gameID ;

// How should I create a game? I understand there is a game name, but should I also assign players to Blakc and White or is that done seperately
    }
    public Game getGame(String gameName) {
        return gameDatas.get(gameName) ;

    }
    public void insertGame(Game gameData) {
        gameDatas.replace(gameData.gameID(), gameData) ;
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
