package dataaccess;

import com.google.gson.Gson;
import model.User;
import org.mindrot.jbcrypt.BCrypt;
import responses.errors.* ;

import javax.lang.model.element.VariableElement;

import static java.sql.Types.NULL;
import java.sql.*;
import java.sql.ResultSet;



public class MySQLUserDAO implements UserDAO {
    private static MySQLUserDAO instance;
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

    private void executeNoReturn(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var ps = conn.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {

                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                } else if (param == null) {
                    ps.setNull(i + 1, NULL);
                }

            }
            ps.executeUpdate();
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    private PreparedStatement executeReturn(String statement, Connection connection, Object... params) throws DataAccessException {
        try{
            var ps = connection.prepareStatement(statement);
            for (var i = 0; i < params.length; i++) {
                var param = params[i];
                if (param instanceof String p) {
                    ps.setString(i + 1, p);
                }
            }
            return ps ;
        }
        catch (Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }


    public User findUser(User inputUser) throws DataAccessException{
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM userInfo WHERE username = ?";
            ResultSet resultSet;
            try (var ps = executeReturn(statement, conn, inputUser.username())) {
                resultSet = ps.executeQuery();
                if (resultSet.next()) {
                    return readUser(resultSet);
                }
                return null;
            }
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
            return new User(username, password, email) ;
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage()) ;
        }
    }
    User storeUserPassword(User inputUser) {
        String hashedPassword = BCrypt.hashpw(inputUser.password(), BCrypt.gensalt());
        return new User(inputUser.username(), hashedPassword, inputUser.email());
    }
    public boolean verifyUser(String inputUsername, String providedClearTextPassword) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM userInfo WHERE username = ?";
            ResultSet resultSet;
            try (var preparedStatement = executeReturn(statement, conn, inputUsername)) {
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    var hashedPassword = readUser(resultSet).password();
                    return BCrypt.checkpw(providedClearTextPassword, hashedPassword);
                }
                return false ;
            }
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage()) ;
        }
    }

    public void clearUsers() throws DataAccessException {
        var statement = "DELETE FROM userInfo"; // if there a problem?
        try (var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch(Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  userInfo (
              `username` varchar(256) NULL,
              `password` varchar(256) NULL,
              `email` varchar(256) NULL,
              PRIMARY KEY (`username`)
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
    public static MySQLUserDAO getInstance() throws DataAccessException {
        if(instance == null){
            return instance = new MySQLUserDAO() ;
        }
        return instance ;
    }
}
