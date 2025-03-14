package dataaccess;

import model.User ;
import responses.errors.Unauthorized401;

public interface UserDAO {
        public void addUser(User inputUser) throws DataAccessException ;
        public User findUser(User inputUser) throws DataAccessException, Unauthorized401;
        public void clearUsers() throws DataAccessException;
        public boolean verifyUser(String inputUsername, String providedClearTextPassword) throws DataAccessException ;
    }
