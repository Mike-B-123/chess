package services;

import com.google.gson.Gson;
import model.AuthData;
import model.Game;
import model.JoinData;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import spark.* ;
import model.User ;
import java.util.HashMap;

// make a spark. exception to make the try and catch easier
public class Handler {
    public Object clear(Request req, Response res) {
        try{
            clearService.clear();
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
            AuthData outAuthData = registerService.register(user); // overarching method needs to be here
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
            AuthData outAuthData = loginService.login(user); // overarching method needs to be here
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
    public Object logout(Request req, Response res) {
        try{String authToken = new Gson().fromJson(req.body(), String.class);
        String outAuthData = logoutService.logout(authToken) ;
        res.status(200) ;
        return new Gson().toJson(outAuthData) ;
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
            String authToken = req.headers("authorization:") ;
            HashMap<Integer,Game> games = listGamesService.listGames(authToken) ; // this might need to become deep copy?
            res.status(200) ;
            return new Gson().toJson(games) ;
        }
        catch(UniqueError500 UniEx){
            res.status(UniEx.getErrorCode()) ;
            res.body(UniEx.getMessage());
            return res ;
        }
        catch(Unauthorized401 UnauthEx){
            res.status(UnauthEx.getErrorCode()) ;
            res.body(UnauthEx.getMessage());
            return res ;
        }
    }
    public Object createGame(Request req, Response res) {
        // How do I break this between two things? authToken and GameName?
        try{String gameName = new Gson().fromJson(req.body(), String.class);
            String authToken = req.headers("authorization:") ;
            int outGameID = createGamesService.create(authToken, gameName) ;
            res.status(200) ;
            return new Gson().toJson(outGameID) ;}
        catch(UniqueError500 UniEx){
            res.status(UniEx.getErrorCode()) ;
            res.body(UniEx.getMessage());
            return res ;
        }
        catch(Unauthorized401 UnauthEx) {
            res.status(UnauthEx.getErrorCode());
            res.body(UnauthEx.getMessage());
            return res;
        }
        catch (BadRequest400 Badreq){
            res.status(Badreq.getErrorCode()) ;
            res.body(new Gson().toJson(Badreq.getErrorMessage()));
            return res ;
       }
    }
    public Object joinGame(Request req, Response res) {
        try {
            JoinData inputJoinData = new Gson().fromJson(req.body(), JoinData.class);
            String authToken = req.headers("authorization:");
            joinGameService.joinGame(authToken, inputJoinData);
            res.status(200);
            return res;
        }
        catch(UniqueError500 UniEx){
            res.status(UniEx.getErrorCode()) ;
            res.body(UniEx.getMessage());
            return res ;
        }
        catch(Unauthorized401 UnauthEx){
            res.status(UnauthEx.getErrorCode()) ;
            res.body(UnauthEx.getMessage());
            return res ;
        }
        catch (BadRequest400 Badreq){
            res.status(Badreq.getErrorCode()) ;
            res.body(new Gson().toJson(Badreq.getErrorMessage()));
            return res ;
        }
        catch(Taken403 TakeEx){
            res.status(TakeEx.getErrorCode()) ;
            res.body(TakeEx.getMessage());
            return res ;
        }
    }

 //throw new BadREquest400 which will will trigger Catch(BadRequest400);


}
