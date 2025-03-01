package server;

import dataaccess.MemoryUserDAO;
import services.Handler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
        Handler handle  = new Handler();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        //Spark.delete("/db", this::clear)
        Spark.post("/user", handle:: register);
        //Spark.post("/session", this::login)
        //Spark.delete("/session", this::logout)
        //Spark.get("/game", this::listGames)
        //Spark.post("/game", this::createGame)
        //Spark.put("/game", this::joinGame)

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
