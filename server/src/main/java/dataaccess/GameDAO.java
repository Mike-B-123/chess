package dataaccess;

import chess.ChessGame;
import model.Game;
import model.User;
import responses.errors.Taken403;

import java.util.HashMap;

public interface GameDAO {
    public Integer createGame(String inputGameName) ;
    public void insertGame(Game gameData) ;
    public Game getGame(String gameName) ;
    public HashMap<Integer , Game> listAllGames() ;
    public void clearGame() ;
    public Boolean availableGame(ChessGame.TeamColor color, Integer gameID) throws Taken403;
    public void modifyInsert(String authToken, ChessGame.TeamColor color, Integer gameID) ;
}
