package dataaccess;

import chess.ChessBoard;
import chess.ChessGame;
import model.Game;
import model.User;
import responses.errors.Taken403;
import com.google.gson.Gson;

import javax.xml.transform.Result;
import java.sql.*;

import java.util.HashMap;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.BOOLEAN;
import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO{
    private static MySQLGameDAO instance ;
    public MySQLGameDAO() throws DataAccessException{
        configureDatabase() ;

    }


    public Game getGame(Integer id) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM gameInfo WHERE gameID = ?";
            ResultSet resultSet ;
            try (var ps = executeQueryReturn(statement, conn, id)) {
                resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return readGame(resultSet);
                }
                return null;
            }
        }
        catch (Exception ex) {
                throw new DataAccessException(ex.getMessage());
        }

    }

    public HashMap<Integer, Game> listAllGames() throws DataAccessException{
        HashMap<Integer, Game> resultMap ;
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM gameInfo" ;
            var ps = executeQueryReturn(statement, conn);
            ResultSet resultSet = ps.executeQuery();
            resultMap = readGames(resultSet) ;
            return resultMap ;
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }

    }


    public Game createGame(String gameName) throws DataAccessException {
        try {
            // the 4 lines above are new
            String chessGame = new Gson().toJson(new ChessGame()) ;
            var statement = "INSERT INTO gameInfo (gameName, chessGame) VALUES (?, ?)";
            int id = executeUpdateReturn(statement, gameName, chessGame);
            return getGame(id) ;
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
            var whiteStatement = "UPDATE gameInfo SET whiteUsername= ? WHERE gameID = ?" ;
            executeUpdateReturn(whiteStatement, authDao.getUsernameFromAuth(authToken), gameID) ;
            }
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    public void updateGame(ChessGame game, Integer gameID) throws DataAccessException {
        try{
            String chessgame = new Gson().toJson(game) ;
            var statement = "UPDATE gameInfo SET chessGame= ? WHERE gameID = ?" ;
            executeUpdateReturn(statement, chessgame, gameID) ;
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    public void updateUser(ChessGame.TeamColor teamColor, Integer gameID) throws DataAccessException {
        try {
            var statement = "UPDATE gameInfo SET blackUsername= ? WHERE gameID = ?" ;
            if (teamColor == ChessGame.TeamColor.WHITE) {
                statement = "UPDATE gameInfo SET whiteUsername= ? WHERE gameID = ?";
            }
            executeUpdateReturn(statement, null, gameID) ;
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    // this is new so make sure this works!!
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
            ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
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
                ChessGame chessGame = new Gson().fromJson(rs.getString("chessGame"), ChessGame.class);
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
            """
            CREATE TABLE IF NOT EXISTS  gameInfo (
              `gameID` int NOT NULL auto_increment,
              `whiteUsername` varchar(256) NULL,
              `blackUsername` varchar(256) NULL,
              `gameName` varchar(256) NOT NULL,
              `chessGame` json NOT NULL,
              PRIMARY KEY (`gameID`)
            )"""
    };
    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var s : createStatements) {
                try (var ps = conn.prepareStatement(s)) {
                    ps.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private int executeUpdateReturn(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS);
            for (var i = 0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                } else if (param instanceof Integer p) {
                    ps.setInt(i + 1, p);
                } else if (param instanceof ChessGame p) {
                    ps.setString(i + 1, p.toString());
                }
            }
                ps.executeUpdate();
                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }


    private PreparedStatement executeQueryReturn(String statement, Connection connection, Object... params) throws DataAccessException, SQLException {
        try {
            var ps = connection.prepareStatement(statement) ;
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
            return ps ;
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
