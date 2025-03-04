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
        catch(UniqueError500 UE){
            res.status(UE.getErrorCode()) ;
            res.body(UE.getMessage());
            return res ;
        }
    }
    public Object register(Request req, Response res) {
        try {
            User user = new Gson().fromJson(req.body(), User.class);
            AuthData outAuthData = RegisterService.register(user); // overarching method needs to be here
            res.status(200) ;
            return new Gson().toJson(outAuthData) ;
        }
        catch(BadRequest400 BadEx){
            res.status(BadEx.getErrorCode()) ;
            return new Gson().toJson(BadEx.getErrorMessage()) ;
        }
        catch(Taken403 TakeEx){
            res.status(TakeEx.getErrorCode()) ;
            return new Gson().toJson(TakeEx.getErrorMessage()) ;
        }
        catch(Exception Ex){
             UniqueError500 UniEx = new UniqueError500();
            res.status(UniEx.getErrorCode()) ;
            return new Gson().toJson(UniEx.getErrorMessage());
        }

    }
     public Object login(Request req, Response res) {
        try {
           User user = new Gson().fromJson(req.body(), User.class);
            AuthData outAuthData = LoginService.login(user); // overarching method needs to be here
            res.status(200) ;
            return new Gson().toJson(outAuthData);
        }
        catch(Unauthorized401 UnAuthEx){
            res.status(UnAuthEx.getErrorCode()) ;
            return new Gson().toJson(UnAuthEx.getErrorMessage());
        }
        catch(Exception Ex){
            UniqueError500 UniEx = new UniqueError500();
            res.status(UniEx.getErrorCode()) ;
            return new Gson().toJson(UniEx.getErrorMessage());
        }

     }
    public Object logoutHandle(Request req, Response res) {
        try {
            String authToken = req.headers("Authorization") ;
        String result = LogoutService.logout(authToken) ;
        res.status(200) ;
        return result ;
        }
        catch(Unauthorized401 UnauthEx){
            res.status(UnauthEx.getErrorCode()) ;
            return new Gson().toJson(UnauthEx.getErrorMessage()) ;
            }
        catch(Exception Ex){
            UniqueError500 UniEx = new UniqueError500() ;
            res.status(UniEx.getErrorCode()) ;
            return new Gson().toJson(UniEx.getErrorMessage()) ;
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
        catch(Unauthorized401 UnAuthEx){
            res.status(UnAuthEx.getErrorCode()) ;
            return new Gson().toJson(UnAuthEx.getErrorMessage()) ;
        }
        catch(Exception Ex){
            UniqueError500 UniEx = new UniqueError500();
            res.status(UniEx.getErrorCode()) ;
            return new Gson().toJson(UniEx.getErrorMessage());
        }
    }
    public Object createNewGame(Request req, Response res) {
        try{
            CreateGameName gameName = new Gson().fromJson(req.body(), CreateGameName.class);
            String authToken = req.headers("Authorization") ;
            Game outGame = CreateGamesService.create(authToken, gameName.gameName()) ;
            GameID ID = new GameID(outGame.gameID()) ;
            res.status(200) ;
            return new Gson().toJson(ID) ;
        }
        catch(BadRequest400 BadEx){
            res.status(BadEx.getErrorCode()) ;
            return new Gson().toJson(BadEx.getErrorMessage()) ;
        }
        catch(Unauthorized401 UnAuthEx){
            res.status(UnAuthEx.getErrorCode()) ;
            return new Gson().toJson(UnAuthEx.getErrorMessage()) ;
        }
       catch(Exception Ex){
            UniqueError500 UniEx = new UniqueError500();
            res.status(UniEx.getErrorCode()) ;
            return new Gson().toJson(UniEx.getErrorMessage());
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
        catch(BadRequest400 BadEx){
            res.status(BadEx.getErrorCode()) ;
            return new Gson().toJson(BadEx.getErrorMessage()) ;
        }
        catch(Taken403 TakeEx){
            res.status(TakeEx.getErrorCode()) ;
            return new Gson().toJson(TakeEx.getErrorMessage()) ;
        }
        catch(Unauthorized401 UnAuthEx){
            res.status(UnAuthEx.getErrorCode()) ;
            return new Gson().toJson(UnAuthEx.getErrorMessage()) ;
        }
        catch(Exception Ex){
            UniqueError500 UniEx = new UniqueError500();
            res.status(UniEx.getErrorCode()) ;
            return new Gson().toJson(UniEx.getErrorMessage());
        }
    }

 //throw new BadREquest400 which will will trigger Catch(BadRequest400);


}
