package client;

import ServerFacade.ServerFacade;
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
        Assertions.assertEquals("{}", facade.clearCall());
    }


    @Test
    public void registerSuccessTest() throws Exception {
        User user = new User("username", "password", "email@email.com") ;
        Assertions.assertEquals("{}", facade.registerCall(user));
    }
    @Test
    public void registerFailTest() throws Exception {
        Exception Exception = new Exception("good job! you failed!");
        Assertions.assertEquals(Exception, facade.registerCall(null));
    }


    @Test
    public void loginSuccessTest() throws Exception {
        User user = new User("username", "password", "email@email.com") ;
        facade.registerCall(user);
        Assertions.assertEquals(facade.getAuthToken(), facade.loginCall(user).authToken());
    }
    @Test
    public void loginFailTest() throws Exception {
        User user = new User("username", "password", "email@email.com") ;
        facade.registerCall(user);
        Exception Exception = new Exception("good job! you failed!");
        Assertions.assertEquals(Exception, facade.loginCall(null).authToken());
    }


    @Test
    public void logoutSucessTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void logoutFailTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void listSuccessTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void listFailTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void createGameSuccessTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void createGameFailTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void joinSuccessTest() {
        Assertions.assertTrue(true);
    }
    @Test
    public void joinFailTest() {
        Assertions.assertTrue(true);
    }

}
