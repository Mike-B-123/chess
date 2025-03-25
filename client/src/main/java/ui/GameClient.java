package ui;

import ServerFacade.ServerFacade;
import model.User;
import ui.HelperMethods ;

import java.util.Scanner;

public class GameClient {
    private final ServerFacade server;
    public String serverUrl;
    private static GameClient instance ;


    public GameClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String listGames() throws Exception {
        try {
            System.out.println("Let's get you those games!");
            server.listCall(server.getAuthToken()) ;
            // how should I list the games?
            return String.format("What's your next 'move'? ;) ");
        } catch (Exception ex) {
            throw new Exception();
        }

    }

    public String createGame() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"What's your username?", "What's your password?", "Finally, what's your email?"};

            User outputUser = new User(variables[0], variables[1], variables[2]) ;
            server.loginCall(outputUser) ;
            return String.format("You signed in as %s.", outputUser.username());
        } catch (Exception ex) {
            throw new Exception();
        }

    }
    public User logout() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"What's your username?", "What's your password?", "Finally, what's your email?"};
            for (int count = 0; count != 3; count++) {
                System.out.println(outPrompts[count]);
                Scanner scanner = new Scanner(System.in); // this is an input stream and can be read from, and can take in a ton of different things like files
                variables[count] = scanner.next() ; // {john password email}
            }
            User outputUser = new User(variables[0], variables[1], variables[2]) ;
            // server.logoutCall() needs an auth Token how do we get that?
            return outputUser ;
        } catch (Exception ex) {
            throw new Exception();
        }

    }
    public static GameClient getInstance(){
        if(instance == null){
            return instance = new GameClient(instance.serverUrl) ;
        }
        return instance ;
    }
}
}
