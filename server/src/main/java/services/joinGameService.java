package services;

import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import model.Game;
import model.JoinData;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;


public class joinGameService {
    public static void joinGame(String authToken, JoinData joinData) throws UniqueError500, Unauthorized401, Taken403, BadRequest400 {
        try{
            AuthDAO authDao = MemoryAuthDAO.getInstance();
            GameDAO gameDAO = MemoryGameDAO.getInstance();
            if(authToken == null || joinData.gameID() == null || joinData.color() == null){
                throw new BadRequest400();
            }
            if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                try {
                    if (gameDAO.availableGame(joinData.color(), joinData.gameID()) == Boolean.TRUE) {
                        gameDAO.modifyInsert(authToken ,joinData.color(), joinData.gameID()) ;
                    }
                }
                catch(Taken403 TA){
                    throw new Taken403() ;
                }
            }
            throw new Unauthorized401();
        }
        catch(Exception ex){
            throw new UniqueError500() ;
        }
    }
}
