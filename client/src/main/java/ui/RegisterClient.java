package ui;
import ServerFacade.ServerFacade ;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import ui.REPL.* ;

public class RegisterClient {
    private final ServerFacade server;
    private String serverUrl;
    private static RegisterClient instance ;

    public RegisterClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public static String register() throws Exception {
        try {
            String[] variables = new String[3] ;
            String[] outPrompts = {"First pick a username.", "Now pick a password.", "Finally, what's your email?"};
            for (int count = 0; count != 3; count++) {
                System.out.println(outPrompts[count]);
                Scanner scanner = new Scanner(System.in); // this is an input stream and can be read from, and can take in a ton of different things like files
                variables[count] = scanner.next() ; // {john password email}
            }
            return String.format("You signed in as %s.", userName);
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
