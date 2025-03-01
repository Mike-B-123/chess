import com.google.gson.Gson;
import model.AuthData;
import model.Message;
import responses.errors.BadRequest400;
import responses.errors.Taken403;
import responses.errors.UniqueError500;
import spark.* ;
import model.User ;
import services.*;



public class Handler {
    //Spark.delete("/db", this::clear)
    //Spark.post("/user", this::register)
    //Spark.post("/session", this::login)
    //Spark.delete("/session", this::logout)
    //Spark.get("/game", this::listGames)
    //Spark.post("/game", this::createGame)
    //Spark.put("/game", this::joinGame)
    public Object clear(Request req, Response res) {
        try{
            clearService.clear();
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
            return new Gson().toJson(outAuthData);
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
        User user = new Gson().fromJson(req.body(), User.class);
        AuthData outAuthData = loginService.login(user) ; // overarching method needs to be here
        return new Gson().toJson(outAuthData) ;
    }
    public Object logout(Request req, Response res) {
        String authToken = new Gson().fromJson(req.body(), String.class);
        String outAuthData = logoutService.logout(authToken) ;
        res.status(200) ;// overarching method needs to be here
        return new Gson().toJson(outAuthData) ;
    }
    public Object listGames(Request req, Response res) {

    }
    public Object createGame(Request req, Response res) {

    }
    public Object joinGame(Request req, Response res) {

    }

    ///  throw new BadREquest400 which will will trigger Catch(BadRequest400)



}
