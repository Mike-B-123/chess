import com.google.gson.Gson;
import model.AuthData;
import responses.errors.BadRequest400;
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
        catch(UniqueError500 ex){

        }

    }
    public Object register(Request req, Response res) {
            User user = new Gson().fromJson(req.body(), User.class);
            AuthData outAuthData = registerService.register(user) ; // overarching method needs to be here
            return new Gson().toJson(outAuthData) ;
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
