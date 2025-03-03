package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.Game;
import model.JoinData;
import model.User;
import responses.errors.Taken403;

import java.util.Collection;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, Game> gameDatas = new HashMap<>();
    private static MemoryGameDAO instance ;
    private int gameID = 0 ;
    public HashMap<Integer, Game> listAllGames() {
        return gameDatas ;
    }


    public Game createGame(String inputGameName) {
        gameID ++ ;
        ChessGame inputChessGame = new ChessGame() ;
        Game newGameData = new Game(gameID, null, null, inputGameName, inputChessGame) ;
        gameDatas.put(gameID, newGameData) ;
        return newGameData ;

    }
    public void modifyInsert(String authToken, ChessGame.TeamColor color, Integer gameID){
        AuthDAO authDao = MemoryAuthDAO.getInstance();
        Game game = gameDatas.get(gameID) ;
        String username = authDao.getUsernameFromAuth(authToken) ;
        if(color == ChessGame.TeamColor.BLACK){
            Game newGameData = new Game(gameID, game.whiteUsername(), username, game.gameName(), game.game()) ;
            gameDatas.replace(game.gameID(), game, newGameData) ;
        }
        else{
            Game newGameData = new Game(gameID, username, game.blackUsername(), game.gameName(), game.game()) ;
            gameDatas.replace(game.gameID(), game, newGameData) ;
        }
    }
    public Boolean availableGame(ChessGame.TeamColor color, Integer gameID) throws Taken403 {
        if (color == ChessGame.TeamColor.BLACK) {
            if (gameDatas.get(gameID).blackUsername() != null) {
                throw new Taken403();
            }
            return Boolean.TRUE ;
        }
        if (gameDatas.get(gameID).whiteUsername() != null) {
            throw new Taken403();
        }
        return Boolean.TRUE ;
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
