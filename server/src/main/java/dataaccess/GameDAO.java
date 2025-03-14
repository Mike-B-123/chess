package dataaccess;

import chess.ChessGame;
import model.Game;
import responses.errors.Taken403;

import java.util.HashMap;

public interface GameDAO {
    public Game createGame(String inputGameName) throws DataAccessException;
    public HashMap<Integer , Game> listAllGames() throws DataAccessException;
    public void clearGame() throws DataAccessException;
    public Boolean availableGame(ChessGame.TeamColor color, Integer gameID) throws Taken403, DataAccessException;
    public void modifyInsert(String authToken, ChessGame.TeamColor color, Integer gameID) throws DataAccessException;
    public Game getGame(Integer id) throws DataAccessException;
}
