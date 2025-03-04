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
            AuthDAO authDao = MemoryAuthDAO.getInstance();
            GameDAO gameDAO = MemoryGameDAO.getInstance();
            if(authToken == null || joinData.gameID() == null || joinData.playerColor() == null){
                throw new BadRequest400();
            }
            if (authDao.verifyAuth(authToken) == Boolean.TRUE) {
                    if (gameDAO.availableGame(joinData.playerColor(), joinData.gameID()) == Boolean.TRUE) {
                        gameDAO.modifyInsert(authToken, joinData.playerColor(), joinData.gameID());
                        return ;
                    }
                    else{
                    throw new Taken403() ;
                }
            }
            throw new Unauthorized401();
        }
}
