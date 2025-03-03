package services;

import dataaccess.AuthDAO;
import dataaccess.*;
import dataaccess.UserDAO;
import model.AuthData;
import model.Game;
import model.User;
import responses.errors.BadRequest400;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

public class createGamesService {
    public static Game create(String authToken, String gameName) throws BadRequest400, Unauthorized401 {
        AuthDAO authDao = MemoryAuthDAO.getInstance();
        GameDAO gameDAO = MemoryGameDAO.getInstance();
        if(authToken == null || gameName == null){
                throw new BadRequest400() ;
            }
        if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                return gameDAO.createGame(gameName);
            }
            throw new Unauthorized401();

    }
}
