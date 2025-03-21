package services;

import dataaccess.*;
import model.Game;
import responses.errors.Unauthorized401;

import java.util.HashMap;

public class ListGamesService {
    public static HashMap<Integer, Game> listGames(String authToken) throws Unauthorized401, DataAccessException {
        AuthDAO authDao = MySQLAuthDAO.getInstance();
        GameDAO gameDAO = MySQLGameDAO.getInstance();
        if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
            return gameDAO.listAllGames() ;
        }
        throw new Unauthorized401();
    }
}
