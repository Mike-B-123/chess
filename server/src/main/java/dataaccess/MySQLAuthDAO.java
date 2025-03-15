package dataaccess;

import model.AuthData;
import model.User;
import java.sql.*;

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
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM authInfo WHERE authToken = ?";
            try (var ps = executeReturn(statement, conn, authToken)) {
                var resultSet = ps.executeQuery() ;
                if (resultSet.next()) {
                    return readAuth(resultSet).authToken() != null;
                }
                return null;
            }
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    public AuthData createAuth(User inputUser) throws DataAccessException {
        String authString = generateToken();
        try {
            var statement = "INSERT INTO authInfo (authToken, username) VALUES (?, ?)";
            executeNoReturn(statement, authString, inputUser.username());
            return new AuthData(authString, inputUser.username()) ;
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
            try(var conn = DatabaseManager.getConnection()) {
                var statement = "SELECT * FROM authInfo WHERE authToken = ?" ;
                try (var ps = executeReturn(statement, conn, authToken)) {
                    ResultSet resultSet = ps.executeQuery();
                    if (resultSet.next()) {
                        return readAuth(resultSet).username();
                    }
                    return null;
                }
            }
            catch (Exception ex) {
                throw new DataAccessException(ex.getMessage()) ;
            }
        }


    private AuthData readAuth(ResultSet rs) throws DataAccessException {
        try {
            String authToken = rs.getString("authToken");
            String username = rs.getString("username") ;
            AuthData authData = new AuthData(authToken, username) ;
            return authData;
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    public void clearAuths() throws DataAccessException {
        var statement = "DELETE FROM authInfo"; // is this a problem?
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  authInfo (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
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

    private void executeNoReturn(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement1 = conn.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {

                var param = params[i];
                if (param instanceof String part) {
                    statement1.setString(i + 1, part);
                } else if (param == null) {
                    statement1.setNull(i + 1, NULL);
                }

            }
            statement1.executeUpdate();
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    private PreparedStatement executeReturn(String statement, Connection connection, Object... params) throws DataAccessException{
        try {
            var preparedStatement = connection.prepareStatement(statement);
            for (var index = 0; index < params.length; index++) {
                var param = params[index];
                if (param instanceof String p) {
                    preparedStatement.setString(index + 1, p);
                }
            }
            return preparedStatement ;
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    public static MySQLAuthDAO getInstance() throws DataAccessException {
        if(instance == null){
            return instance = new MySQLAuthDAO() ;
        }
        return instance ;
    }
}
