package server;

import dataaccess.*;
import server.websocket.WebSocketHandler;
import services.Handler;
import spark.*;

public class Server {
    WebSocketHandler wsh ;
    public Server() {
        try {
            GameDAO gd = MySQLGameDAO.getInstance() ;
            AuthDAO ad = MySQLAuthDAO.getInstance() ;
            this.wsh = new WebSocketHandler(gd, ad);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
    public int run(int desiredPort) {
        Handler handle  = new Handler();
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/ws", wsh);

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.delete("/db", handle::clear) ;
        Spark.post("/user", handle:: register);
        Spark.post("/session", handle::login) ;
        Spark.delete("/session", handle::logoutHandle) ;
        Spark.get("/game", handle::listGames) ;
        Spark.post("/game", handle::createNewGame) ;
        Spark.put("/game", handle::joinGameHandle) ;

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
