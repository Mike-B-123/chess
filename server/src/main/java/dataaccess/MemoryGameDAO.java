package dataaccess;

import chess.ChessGame;
import model.Game;
import responses.errors.Taken403;

import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private HashMap<Integer, Game> gameDatas = new HashMap<>();
    private static MemoryGameDAO instance ;
    private int gameID = 0 ;
    public HashMap<Integer, Game> listAllGames() {
        return gameDatas ;
    }
    public Game getGame(Integer id){
        return gameDatas.get(id) ;
    }


    public Game createGame(String inputGameName) {
        gameID ++ ;
        ChessGame inputChessGame = new ChessGame() ;
        Game newGameData = new Game(gameID, null, null, inputGameName, inputChessGame) ;
        gameDatas.put(gameID, newGameData) ;
        return newGameData ;

    }
    public boolean isGameOver(){
        return true ;
    };
    public void setGameOver(boolean gameOver) {
        return ;
    };

    public void modifyInsert(String authToken, ChessGame.TeamColor color, Integer gameID) throws DataAccessException {
        AuthDAO authDao = MemoryAuthDAO.getInstance();
        Game game = gameDatas.get(gameID) ;
        String username = authDao.getUsernameFromAuth(authToken) ;
        if(color == ChessGame.TeamColor.BLACK){
            gameDatas.put(gameID, gameDatas.get(gameID).setBlack(username)) ;
        }
        else{
            gameDatas.put(gameID, gameDatas.get(gameID)) ;
            {
            }
        }
    }
    public void updateGame(ChessGame game, Integer gameID) throws DataAccessException {
        String white = gameDatas.get(gameID).whiteUsername() ;
        String black = gameDatas.get(gameID).blackUsername() ;
        String gameName = gameDatas.get(gameID).gameName() ;
        Game newGame = new Game(gameID,white, black, gameName, game) ;
        gameDatas.put(gameID, newGame) ;
    }

    @Override
    public void updateUser(ChessGame.TeamColor teamColor, Integer gameID) throws DataAccessException {
        return ;
    }

    public Boolean availableGame(ChessGame.TeamColor color, Integer gameID) throws Taken403 {
        if (color == ChessGame.TeamColor.BLACK) {
            if (gameDatas.get(gameID).blackUsername() != null) {
                throw new Taken403();
            }
            return Boolean.TRUE ;
        }
        if (gameDatas.get(gameID).whiteUsername() != null) {
            throw new Taken403();
        }
        return Boolean.TRUE ;
    }
    public void clearGame(){
            gameDatas.clear();
        }

        public static MemoryGameDAO getInstance(){
            if(instance == null){
                return instance = new MemoryGameDAO() ;
            }
            return instance ;
        }
    }
