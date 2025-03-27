package client;

import serverFacade.ServerFacade;
import chess.ChessGame;
import com.google.gson.Gson;
import model.CreateGameName;
import model.JoinData;
import model.User;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade ;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0); // ask about this port thing
        System.out.println("Started test HTTP server on 0" );
        facade = new ServerFacade(port);
    }
    @BeforeEach
    public void clear() throws Exception {
        facade.clearCall() ;
    }
    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void clearSuccessTest() throws Exception{
        Assertions.assertEquals("{}", new Gson().toJson(facade.clearCall()));
    }


    @Test
    public void registerSuccessTest() throws Exception {
        User user = new User("username", "password", "email@email.com") ;
        Assertions.assertTrue(new Gson().toJson(facade.registerCall(user).authToken()).length() > 1);
        // is this ok that I'm checking length and converting from Gson
    }
    @Test
    public void registerFailTest() throws Exception {
        try{
            facade.registerCall(null);
            Assertions.assertTrue(false);
        }
        catch(Exception ex){
            Assertions.assertTrue(true);
        }

    }


    @Test
    public void loginSuccessTest() throws Exception {
        User user = new User("username", "password", "email@email.com") ;
        facade.registerCall(user);
        Assertions.assertTrue(new Gson().toJson(facade.loginCall(user).authToken()).length() == 38);
    }
    @Test
    public void loginFailTest() throws Exception {
        User user = new User("fakeuser", "fakeword", "email@email.com") ;
        try{
            facade.loginCall(user);
            Assertions.assertTrue(false);
        }
        catch(Exception ex){
            Assertions.assertTrue(true);
        }
    }


    @Test
    public void logoutSucessTest() throws Exception {
        User user = new User("logoutuser1", "logoutword1", "email@email.com") ;
        String authToken = facade.registerCall(user).authToken();
        Assertions.assertEquals("{}", new Gson().toJson(facade.logoutCall(authToken)));
    }
    @Test
    public void logoutFailTest() {
        try{
            facade.logoutCall("faketoken");
            Assertions.assertTrue(false);
        }
        catch(Exception ex){
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void listSuccessTest() throws Exception {
        CreateGameName gameName = new CreateGameName("testGame") ;
        User user = new User("logoutuser1", "logoutword1", "email@email.com") ;
        String authToken = facade.registerCall(user).authToken();
        facade.createGameCall(gameName) ;
        Assertions.assertTrue(facade.listCall(authToken).toString().length() > 1);
    }
    @Test
    public void listFailTest() {
        try{
            facade.listCall("faketoken");
            Assertions.assertTrue(false);
        }
        catch(Exception ex){
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void createGameSuccessTest() throws Exception {
        CreateGameName gameName = new CreateGameName("testGame") ;
        User user = new User("logoutuser1", "logoutword1", "email@email.com") ;
       facade.registerCall(user).authToken();
        Assertions.assertTrue(facade.createGameCall(gameName).gameID() > 0);
    }
    @Test
    public void createGameFailTest() {
        try{
            facade.createGameCall(null) ;
            Assertions.assertTrue(false);
        }
        catch(Exception ex){
            Assertions.assertTrue(true);
        }
    }

    @Test
    public void joinSuccessTest() throws Exception {
        CreateGameName gameName = new CreateGameName("testGame") ;
        User user = new User("logoutuser1", "logoutword1", "email@email.com") ;
        String authToken = facade.registerCall(user).authToken();
        facade.createGameCall(gameName);
        facade.listCall(authToken) ;
        JoinData joinData = new JoinData(ChessGame.TeamColor.WHITE, facade.getGameNumList().get(1)) ;
        Assertions.assertEquals("{}", new Gson().toJson(facade.joinGameCall(joinData)));
    }
    @Test
    public void joinFailTest() {
        try{
            facade.joinGameCall(null) ;
            Assertions.assertTrue(false);
        }
        catch(Exception ex){
            Assertions.assertTrue(true);
        }
    }

}
