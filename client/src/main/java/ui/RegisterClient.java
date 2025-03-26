package ui;
import ServerFacade.ServerFacade ;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import model.User;
import ui.REPL.* ;

public class RegisterClient {
    private final ServerFacade server;
    String serverUrl;
    private REPL repl ;
    private static RegisterClient instance ;
    HelperMethods helperMethods = HelperMethods.getInstance();

    public RegisterClient() {
        server = new ServerFacade(8080);
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
            repl.setState(State.SIGNEDOUT);
            return String.format("%s is signed out! Hope to see you again soon!", server.getUsername());
        } catch (Exception ex) {
            throw new Exception();
        }

    }
    public static RegisterClient getInstance(){
        if(instance == null){
            return instance = new RegisterClient() ;
        }
        return instance ;
    }
}
