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

    public RegisterClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String register() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"First pick a username.", "Now pick a password.", "Finally, what's your email?"};
            variables = helperMethods.varLoop(variables, outPrompts) ;
            User outputUser = new User(variables[0], variables[1], variables[2]) ;
            server.registerCall(outputUser) ;
            repl.setState(State.SIGNEDIN);
            return String.format("You signed in as %s.", outputUser.username());
        } catch (Exception ex) {
            throw new Exception();
        }

    }

    public String login() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"What's your username?", "What's your password?", "Finally, what's your email?"};
            variables = helperMethods.varLoop(variables, outPrompts) ;
            User outputUser = new User(variables[0], variables[1], variables[2]) ;
            server.loginCall(outputUser) ;
            repl.setState(State.SIGNEDIN);
            return String.format("You signed in as %s.", outputUser.username());
        } catch (Exception ex) {
            throw new Exception();
        }

    }
    public String logout() throws Exception {
        try {
            server.
            repl.setState(State.SIGNEDOUT);
            return String.format("%s is signed out.", outputUser.username());
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
