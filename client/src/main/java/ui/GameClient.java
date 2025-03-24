package ui;

import ServerFacade.ServerFacade;
import model.User;

import java.util.Scanner;

public class GameClient {
    private final ServerFacade server;
    private String serverUrl;
    private static RegisterClient instance ;

    public GameClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String listGames() throws Exception {
        try {
            System.out.println("Let's get you those games!");
            //server.listCall() again need the authtoken, where should I store this?
            return String.format("You signed in as %s.", outputUser.username());
        } catch (Exception ex) {
            throw new Exception();
        }

    }

    public String login() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"What's your username?", "What's your password?", "Finally, what's your email?"};
            for (int count = 0; count != 3; count++) {
                System.out.println(outPrompts[count]);
                Scanner scanner = new Scanner(System.in); // this is an input stream and can be read from, and can take in a ton of different things like files
                variables[count] = scanner.next() ; // {john password email}
            }
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
    public static RegisterClient getInstance(){
        if(instance == null){
            return instance = new RegisterClient(instance.serverUrl) ;
        }
        return instance ;
    }
}
}
