package services;

import com.google.gson.Gson;
import model.*;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import spark.* ;

import java.util.Collection;
import java.util.HashMap;

// make a spark. exception to make the try and catch easier
public class Handler {
    public Object clear(Request req, Response res) {
        try{
            ClearService.clear();
            res.status(200) ;
            return "{}" ;
            // make sure I am still returning something.
        }
        catch(Exception ex){
            UniqueError500 uniEx = new UniqueError500();
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage());

        }
    }
    public Object register(Request req, Response res) {
        try {
            User user = new Gson().fromJson(req.body(), User.class);
            AuthData outAuthData = RegisterService.register(user); // overarching method needs to be here
            res.status(200) ;
            return new Gson().toJson(outAuthData) ;
        }
        catch(BadRequest400 badEx){
            res.status(badEx.getErrorCode()) ;
            return new Gson().toJson(badEx.getErrorMessage()) ;
        }
        catch(Taken403 takeEx){
            res.status(takeEx.getErrorCode()) ;
            return new Gson().toJson(takeEx.getErrorMessage()) ;
        }
        catch(Exception ex){
             UniqueError500 uniEx = new UniqueError500();
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage());
        }

    }
     public Object login(Request req, Response res) {
        try {
           User user = new Gson().fromJson(req.body(), User.class);
            AuthData outAuthData = LoginService.login(user); // overarching method needs to be here
            res.status(200) ;
            return new Gson().toJson(outAuthData);
        }
        catch(Unauthorized401 unAuthEx){
            res.status(unAuthEx.getErrorCode()) ;
            return new Gson().toJson(unAuthEx.getErrorMessage());
        }
        catch(Exception ex){
            UniqueError500 uniEx = new UniqueError500();
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage());
        }

     }
    public Object logoutHandle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization") ;
        String result = LogoutService.logout(authToken) ;
        res.status(200) ;
        return result ;
        }
        catch(Unauthorized401 unauthEx){
            res.status(unauthEx.getErrorCode()) ;
            return new Gson().toJson(unauthEx.getErrorMessage()) ;
            }
        catch(Exception ex){
            UniqueError500 uniEx = new UniqueError500() ;
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage()) ;
        }
    }
    public Object listGames(Request req, Response res) {
        try{
            String authToken = req.headers("Authorization") ;
            HashMap<Integer,Game> gamesHash = ListGamesService.listGames(authToken) ; // this might need to become deep copy?
            res.status(200) ;
            Collection<Game> gameCollection = gamesHash.values() ;
            GamesList games = new GamesList(gameCollection) ;
            return new Gson().toJson(games) ;
        }
        catch(Unauthorized401 unAuthEx){
            res.status(unAuthEx.getErrorCode()) ;
            return new Gson().toJson(unAuthEx.getErrorMessage()) ;
        }
        catch(Exception ex){
            UniqueError500 uniEx = new UniqueError500();
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage());
        }
    }
    public Object createNewGame(Request req, Response res) {
        try{
            CreateGameName gameName = new Gson().fromJson(req.body(), CreateGameName.class);
            String authToken = req.headers("Authorization") ;
            Game outGame = CreateGamesService.create(authToken, gameName.gameName()) ;
            GameID id = new GameID(outGame.gameID()) ;
            res.status(200) ;
            return new Gson().toJson(id) ;
        }
        catch(BadRequest400 badEx){
            res.status(badEx.getErrorCode()) ;
            return new Gson().toJson(badEx.getErrorMessage()) ;
        }
        catch(Unauthorized401 unAuthEx){
            res.status(unAuthEx.getErrorCode()) ;
            return new Gson().toJson(unAuthEx.getErrorMessage()) ;
        }
       catch(Exception ex){
            UniqueError500 uniEx = new UniqueError500();
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage());
        }
    }
    public Object joinGameHandle(Request req, Response res) {
        try {
            JoinData inputJoinData = new Gson().fromJson(req.body(), JoinData.class);
            String authToken = req.headers("Authorization");
            JoinGameService.joinGame(authToken, inputJoinData);
            res.status(200);
            return "{}" ;
        }
        catch(BadRequest400 badEx){
            res.status(badEx.getErrorCode()) ;
            return new Gson().toJson(badEx.getErrorMessage()) ;
        }
        catch(Taken403 takeEx){
            res.status(takeEx.getErrorCode()) ;
            return new Gson().toJson(takeEx.getErrorMessage()) ;
        }
        catch(Unauthorized401 unAuthEx){
            res.status(unAuthEx.getErrorCode()) ;
            return new Gson().toJson(unAuthEx.getErrorMessage()) ;
        }
        catch(Exception ex){
            UniqueError500 uniEx = new UniqueError500();
            res.status(uniEx.getErrorCode()) ;
            return new Gson().toJson(uniEx.getErrorMessage());
        }
    }



}
