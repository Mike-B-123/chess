package services;

import com.google.gson.Gson;
import model.AuthData;
import model.Game;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.Unauthorized401;
import responses.errors.UniqueError500;
import spark.* ;
import model.User ;

import java.util.ArrayList;
import java.util.Collection;

// make a spark. exception to make the try and catch easier
public class Handler {
    public Object clear(Request req, Response res) {
        try{
            clearService.clear();
            res.status(200) ;
            return res ;
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
            return new Gson().toJson(outAuthData);
        }
        catch(BadRequest400 BadEx){
            res.status(BadEx.getErrorCode()) ;
            res.body(BadEx.getMessage());
            return res ;
        }
        catch(UniqueError500 UniEx){
            res.status(UniEx.getErrorCode()) ;
            res.body(UniEx.getMessage());
            return res ;
        }
        catch(Taken403 TakeEx){
            res.status(TakeEx.getErrorCode()) ;
            res.body(TakeEx.getMessage());
            return res ;
        }

    }
     public Object login(Request req, Response res) {
        try {
           User user = new Gson().fromJson(req.body(), User.class);
            AuthData outAuthData = loginService.login(user); // overarching method needs to be here
            res.status(200) ;
            return new Gson().toJson(outAuthData);
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
    public Object logout(Request req, Response res) {
        try{String authToken = new Gson().fromJson(req.body(), String.class);
        String outAuthData = logoutService.logout(authToken) ;
        res.status(200) ;
        return new Gson().toJson(outAuthData) ;}
        catch(UniqueError500 UniEx){
                res.status(UniEx.getErrorCode()) ;
                res.body(UniEx.getMessage());
                return res ;
            }
        // Bad request is if the input is "null" so just do an if statement for that in the service
        catch(Unauthorized401 UnauthEx){
                res.status(UnauthEx.getErrorCode()) ;
                res.body(UnauthEx.getMessage());
                return res ;
            }
    }
    public Object listGames(Request req, Response res) {
        try{
            String authToken = req.headers("authorization:") ;
            Collection<Game> games = new ArrayList<>() ;
            games.addAll(listGames(authToken)) ;
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
        catch(Unauthorized401 UnauthEx){
            res.status(UnauthEx.getErrorCode()) ;
            res.body(UnauthEx.getMessage());
            return res ;
        }
        catch (BadRequest400 Badreq){
            res.status(Badreq.getErrorCode()) ;
            res.body(Badreq.getMessage());
            return res ;
       }
    }
    public Object joinGame(Request req, Response res) {

    }

 throw new BadREquest400 which will will trigger Catch(BadRequest400);


}
