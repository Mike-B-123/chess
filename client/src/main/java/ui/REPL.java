package ui;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
    //private final GameClient gameClient ;
    RegisterClient registerClient = RegisterClient.getInstance();
    GameClient gameClient = GameClient.getInstance();
    private State state = State.SIGNEDOUT ;
    public REPL(String serverURL){}
    public void run(String serverURL){
        System.out.println("\uD83D\uDC36 Ready to play some Chess? First sign in! :)");
        System.out.print(help()) ;
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.next();
            try {
                result = eval(line);
                System.out.print(result + SET_TEXT_COLOR_BLUE);
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
                - createGame
                - listGames
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
            case "createGame" -> gameClient.createGame() ;
            //case "listGames" -> GameClient.listGames();
            case "play" -> gameClient.play() ;
            //case "observe" -> GameClient.observeGame(); // how in the world do we do this?
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
