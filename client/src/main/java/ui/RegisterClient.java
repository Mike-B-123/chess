package ui;
import serverfacade.ServerFacade ;

import model.User;

public class RegisterClient {
    private final ServerFacade server;;
    private static RegisterClient instance ;
    HelperMethods helperMethods = HelperMethods.getInstance();

    public RegisterClient(ServerFacade inputServer) {
        this.server = inputServer ;
    }

    public String register() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"First pick a username.", "Now pick a password.", "Finally, what's your email?"};
            variables = helperMethods.varLoop(variables, outPrompts) ;
            User outputUser = new User(variables[0], variables[1], variables[2]) ;
            server.registerCall(outputUser) ;
            return String.format("You signed in as %s.", outputUser.username());
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }

    }

    public String login() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"What's your username?", "What's your password?", "Finally, what's your email?"};
            variables = helperMethods.varLoop(variables, outPrompts) ;
            User outputUser = new User(variables[0], variables[1], variables[2]) ;
            server.loginCall(outputUser) ;
            return String.format("You signed in as %s .", outputUser.username());
        } catch (Exception ex) {
            throw new Exception();
        }

    }
    public String logout() throws Exception {
        try {
            String authToken = server.getAuthToken() ; // is this redudant or ok?
            server.logoutCall(authToken);
            return String.format("%s is signed out! Hope to see you again soon!", server.getUsername());
        } catch (Exception ex) {
            throw new Exception();
        }

    }
    public static RegisterClient getInstance(ServerFacade server){
        if(instance == null){
            return instance = new RegisterClient(server) ;
        }
        return instance ;
    }
}
