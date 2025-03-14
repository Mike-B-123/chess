package dataaccess;

import chess.ChessGame;
import model.Game;
import responses.errors.Taken403;

import javax.xml.transform.Result;
import java.sql.*;

import java.util.HashMap;

import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO{
    private static MySQLGameDAO instance ;
    public MySQLGameDAO() throws DataAccessException{
        configureDatabase() ;

    }


    public Game getGame(Integer id) throws DataAccessException{
        try {
            var statement = "SELECT * FROM gameInfo WHERE gameID = ?" ;
            var resultSet = executeQueryReturn(statement, id) ;
            return readGame(resultSet);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }

    }

    public HashMap<Integer, Game> listAllGames() throws DataAccessException{
        HashMap<Integer, Game> resultMap = new HashMap<>();
        try {
            var statement = "SELECT * FROM gameInfo" ;
            ResultSet resultSet = executeQueryReturn(statement);
            resultMap = readGames(resultSet) ;
            return resultMap ;
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }

    }


    public Game createGame(String inputGameName) throws DataAccessException {
        try {
            var countStatement = "SELECT COUNT(*)";
            int rows = executeUpdateReturn(countStatement) ;
            rows ++;
            ChessGame inputChessGame = new ChessGame();
            var statement = "INSERT INTO gameInfo (whiteUsername, blackUsername, gameName, chessGame) VALUES (?, ?, ?, ?)";
            var ResultSet = executeQueryReturn(statement, null, null, inputGameName, inputChessGame);
            return readGame(ResultSet);
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }

    }
    public void modifyInsert(String authToken, ChessGame.TeamColor color, Integer gameID) throws DataAccessException {
        try{
            AuthDAO authDao = MySQLAuthDAO.getInstance();
        Game storedGame = getGame(gameID) ;
        if(color == ChessGame.TeamColor.BLACK){
            var blackStatement = "UPDATE gameInfo SET blackUsername= ? WHERE gameID = ?" ;
            executeUpdateReturn(blackStatement, authDao.getUsernameFromAuth(authToken), gameID) ;
        }
        else{
            var whiteStatement = "UPDATE gameInfo SET blackUsername= ? WHERE gameID = ?" ;
            executeUpdateReturn(whiteStatement, gameID, authDao.getUsernameFromAuth(authToken), storedGame.blackUsername(), storedGame.game()) ;
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

    private HashMap<Integer, Game> readGames (ResultSet rs) throws DataAccessException {
        try {
            HashMap<Integer, Game> resultMap = new HashMap<>() ;
            while(rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                ChessGame chessGame = rs.getObject("chessGame", ChessGame.class);
                // is this chessGame correct?
                Game newGame = new Game(gameID, whiteUsername, blackUsername, gameName, chessGame);
                resultMap.put(newGame.gameID(), newGame);
            }
            return  resultMap ;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    public void clearGame() throws DataAccessException {
        var statement = "DELETE FROM gameInfo"; // is this a problem?
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            // fixme: double check nulls
            """
            CREATE TABLE IF NOT EXISTS  gameInfo (
              `gameID` int NOT NULL auto_increment,
              `whiteUsername` varchar(256),
              `blackUsername` varchar(256),
              `gameName` varchar(256) NOT NULL,
              `chessGame` json NOT Null,
              PRIMARY KEY (`gameID`)
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

    private int executeUpdateReturn(String statement, Object... params) throws DataAccessException, SQLException {
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


    private ResultSet executeQueryReturn(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }
            }
            return ps.executeQuery();
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
    public static MySQLGameDAO getInstance() throws DataAccessException {
        if(instance == null){
            return instance = new MySQLGameDAO() ;
        }
        return instance ;
    }
}
