package services;

import dataaccess.*;
import model.AuthData;
import model.Game;
import model.User;
import responses.errors.BadRequest400;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;

import java.util.Collection;
import java.util.HashMap;

public class listGamesService {
    public static HashMap<Integer, Game> listGames(String authToken) throws UniqueError500, Unauthorized401{
        try{AuthDAO authDao = MemoryAuthDAO.getInstance();
        GameDAO gameDAO = MemoryGameDAO.getInstance();
        if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
            return gameDAO.listAllGames() ;
        }
        throw new Unauthorized401();
    }
        catch(Exception ex){
        throw new UniqueError500() ;// should actually return exception
    }
    }
}
