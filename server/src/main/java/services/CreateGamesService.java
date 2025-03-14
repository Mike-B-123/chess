package services;

import dataaccess.AuthDAO;
import dataaccess.*;
import model.Game;
import responses.errors.BadRequest400;
import responses.errors.Unauthorized401;

public class CreateGamesService {
    public static Game create(String authToken, String gameName) throws BadRequest400, Unauthorized401, DataAccessException {
        AuthDAO authDao = MySQLAuthDAO.getInstance();
        GameDAO gameDAO = MySQLGameDAO.getInstance();
        if(authToken == null || gameName == null){
                throw new BadRequest400() ;
            }
        if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                return gameDAO.createGame(gameName);
            }
            throw new Unauthorized401();

    }
}
