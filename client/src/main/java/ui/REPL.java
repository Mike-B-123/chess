package ui;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
    //private final GameClient gameClient ;
    RegisterClient registerClient = RegisterClient.getInstance();
    GameClient gameClient = GameClient.getInstance();
    private State state = State.SIGNEDOUT ;
    public REPL(){}

    public void run(){
        System.out.println("Ready to play some Chess? First sign in! :)");
        System.out.print(help()) ;
        printPrompt();
        Scanner scanner = new Scanner(System.in);
        var result = "";
//        result = scanner.next() ;
        while (!result.equals("quit")) {
            String line = scanner.next();
            try {
                result = eval(line);
                if(result.contains("You signed in as")) {
                    setState(State.SIGNEDIN);
                }
                System.out.print(result );
                printPrompt();
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
            }
        }
        System.out.println();
    }

    public enum State{
        SIGNEDOUT ,
        SIGNEDIN
    }

    public void setState(State state) {
        this.state = state;
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - help
                    - register
                    - login
                    - quit
                    """;
        }
        return """
                - help
                - logout
                - create (create a game)
                - list (list games)
                - play
                - observe
                - quit
                """;
    }
public String eval(String input) {
    try {
        return switch (input.toLowerCase()) {
            case "register" -> registerClient.register();
            case "login" -> registerClient.login() ;
            case "logout" -> registerClient.logout() ;
            case "create" -> gameClient.createGame() ;
            case "list" -> gameClient.listGames();
            case "play" -> gameClient.play() ;
            case "observe" -> gameClient.observeGame(); // how in the world do we do this?
            case "quit" -> "quit";
            default -> help();
        };
    } catch (Exception ex) {
        return ex.getMessage();
    }
}
    private void printPrompt() {
        System.out.print("\n" + ">>> " + SET_TEXT_COLOR_GREEN); // should I keep the RESET thing?
    }
}
