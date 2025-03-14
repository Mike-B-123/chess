package dataaccess;

import model.AuthData;
import model.User;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

import java.util.UUID;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO {
    private static MySQLAuthDAO instance ;

    public MySQLAuthDAO() throws DataAccessException{
        configureDatabase() ;
    }


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public Boolean verifyAuth(String authToken) throws DataAccessException {
        try {
            var statement = "SELECT * FROM authInfo WHERE authToken = ?" ;
            var resultSet = executeReturn(statement, authToken) ;
            return readAuth(resultSet).authToken() != null ;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    public AuthData createAuth(User inputUser) throws DataAccessException {
        String authString = generateToken();
        try {
            var statement = "INSERT INTO authInfo (authToken, username) VALUES (?, ?)";
            var ResultSet = executeReturn(statement, authString, inputUser.username());
            return readAuth(ResultSet);
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        try {
            var statement = "DELETE FROM authInfo WHERE authToken = ?" ;
            executeNoReturn(statement, authToken); ;
            // Do I need to return something when I delete a authToken?
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    public String getUsernameFromAuth(String authToken) throws DataAccessException {
        try {
            var statement = "SELECT * FROM authInfo WHERE authToken = ?" ;
            var resultSet = executeReturn(statement, authToken) ;
            return readAuth(resultSet).username() ;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }


    private AuthData readAuth(ResultSet rs) throws DataAccessException {
        try {
            String authToken = rs.getString("authToken");
            String userrname = rs.getString("username") ;
            AuthData authData = new AuthData(authToken, userrname) ;
            return authData;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    public void clearAuths() throws DataAccessException {
        var statement = "DELETE FROM authInfo"; // is this a problem?
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeQuery();
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authInfo (
              `authToken` string NOT NULL primary key,
              `username` string NOT NULL,
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

    private void executeNoReturn(String statement, Object... params) throws DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {

                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }

                ps.executeUpdate();
            }
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
            }
            return ps.executeQuery();
        }
    }
    public static MySQLAuthDAO getInstance() throws DataAccessException {
        if(instance == null){
            return instance = new MySQLAuthDAO() ;
        }
        return instance ;
    }
}
