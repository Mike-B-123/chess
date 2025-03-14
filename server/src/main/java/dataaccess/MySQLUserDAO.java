package dataaccess;

import com.google.gson.Gson;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import responses.errors.* ;
import static java.sql.Types.NULL;
import java.sql.*;
import java.sql.ResultSet;



public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException{
        configureDatabase() ;
    }

    public void addUser(User inputUser) throws DataAccessException {
        try {
            var statement = "INSERT INTO userInfo (username, password, email) VALUES (?, ?, ?)";
            User newUser = storeUserPassword(inputUser);
            executeNoReturn(statement, newUser.username(), newUser.password(), newUser.email());
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
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


    public User findUser(User inputUser) throws DataAccessException, Unauthorized401{
        try {
            var statement = "SELECT * FROM userInfo WHERE username = ?" ;
                var resultSet = executeReturn(statement, inputUser.username()) ;
                if(!verifyUser(inputUser.username(), inputUser.password())) {
                    throw new Exception("could not verify password"); // Is this how I should be using verify?
                }
                return readUser(resultSet);
        }
        catch(Unauthorized401 UnAuth){
            throw new Unauthorized401() ;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    private User readUser(ResultSet rs) throws DataAccessException {
        try{
            String username = rs.getString("username");
            String password = rs.getString("password") ;
            String email = rs.getString("email") ;
            User user = new User(username, password, email) ;
            return user ;
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    User storeUserPassword(User inputUser) {
        String hashedPassword = BCrypt.hashpw(inputUser.password(), BCrypt.gensalt());
        return new User(inputUser.username(), hashedPassword, inputUser.email());
    }
    boolean verifyUser(String inputUsername, String providedClearTextPassword) throws DataAccessException {
        try {
            var statement = "SELECT * FROM userInfo WHERE username = ?" ;
            var resultSet = executeReturn(statement, inputUsername) ;
            var hashedPassword = readUser(resultSet).password() ;
            return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    public void clearUsers() throws DataAccessException {
        var statement = "DELETE FROM userInfo"; // if there a problem?
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeQuery();
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userInfo (
              `username` string NOT NULL primary key,
              `password` string NOT NULL,
              'email' string NOT NULL
            ) 
            """
    } ;

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
}
