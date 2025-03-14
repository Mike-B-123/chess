package dataaccess;

import chess.ChessGame;
import model.Game;
import model.User;
import responses.errors.Taken403;
import java.sql.*;

import java.util.ArrayList;
import java.util.HashMap;

import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO{
    public MySQLGameDAO() throws DataAccessException{
        configureDatabase() ;
    }


    public Game getGame(Integer id) throws DataAccessException{
        try {
            var statement = "SELECT * FROM gameInfo WHERE gameID = ?" ;
            var resultSet = executeReturn(statement, id) ;
            return readGame(resultSet);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }

    }

    public HashMap<Integer, Game> listAllGames() throws DataAccessException{
        HashMap<Integer, Game> resultMap = new HashMap<>();
        try {
            var statement = "SELECT COUNT(*)";
            int rows = executeIntReturn(statement);
            for(int i = 0; i != rows; i++){
              Game addGame = getGame(i);
              resultMap.put(i, addGame) ;
            }
            return resultMap ;
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }

    }


    public Game createGame(String inputGameName) throws DataAccessException {
        try {
            var countStatement = "SELECT COUNT(*)";
            int rows = executeIntReturn(countStatement) ;
            rows ++;
            ChessGame inputChessGame = new ChessGame();
            var statement = "INSERT INTO userInfo (gameID, whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?, ?)";
            var ResultSet = executeReturn(statement, rows, null, null, inputGameName, inputChessGame);
            return readGame(ResultSet);
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }

    }
    public void modifyInsert(String authToken, ChessGame.TeamColor color, Integer gameID) throws DataAccessException {
        try{
        Game storedGame = getGame(gameID) ;
        String username = authDao.getUsernameFromAuth(authToken) ;
        if(color == ChessGame.TeamColor.BLACK){
            var blackStatement = "REPLACE(gameID, whiteUsername, blackUsername, gameName, chessGame) FROM gameInfo WHERE gameID = ?" ;
            executeIntReturn(blackStatement, gameID, storedGame.whiteUsername(), getUsernameFromAuth, storedGame.game()) ;
        }
        else{
            var whiteStatement = "REPLACE(gameID, whiteUsername, blackUsername, gameName, chessGame) FROM gameInfo WHERE gameID = ?" ;
            executeIntReturn(whiteStatement, gameID, getUsernameFromAuth, storedGame.blackUsername(), storedGame.game()) ;
            }
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    public Boolean availableGame(ChessGame.TeamColor color, Integer gameID) throws Taken403, DataAccessException {
        if (color == ChessGame.TeamColor.BLACK) {
            if (getGame(gameID).blackUsername() != null) {
                throw new Taken403();
            }
            return Boolean.TRUE ;
        }
        if (getGame(gameID).whiteUsername() != null) {
            throw new Taken403();
        }
        return Boolean.TRUE ;
    }

    private Game readGame(ResultSet rs) throws DataAccessException {
        try {
            int gameID = rs.getInt("gameID") ;
            String whiteUsername = rs.getString("whiteUsername");
            String blackUsername = rs.getString("blackUsername") ;
            String gameName = rs.getString("gameName") ;
            ChessGame chessGame = rs.getObject("chessGame", ChessGame.class) ;
            // is this chessGame correct?
            Game newGame = new Game(gameID, whiteUsername, blackUsername, gameName, chessGame) ;
            return newGame ;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    public void clearGame() throws DataAccessException {
        var statement = "DELETE FROM gameInfo"; // is this a problem?
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeQuery();
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            // fixme: double check nulls
            """
            CREATE TABLE IF NOT EXISTS  gameInfo (
              `gameID` int NOT NULL primary key auto_increment,
              `whiteUsername` string,
              `blackUsername` string,
              `gameName` string NOT NULL,
              'chessGame' json NOT Null
            )"""
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private int executeIntReturn(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {

                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                } else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

                return 0;
            }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    private ResultSet executeReturn(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                }
                else if (param instanceof Integer p){
                    ps.setInt(i + 1, p);
                }
                else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
    }
}
