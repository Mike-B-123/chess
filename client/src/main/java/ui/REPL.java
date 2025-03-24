package ui;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
    //private final GameClient gameClient ;
    private final RegisterClient registerClient ;
    private State state = State.SIGNEDOUT ;
    public REPL(String serverURL){
        //gameClient = new GameClient(serverURL, this) ;
        registerClient = new RegisterClient(serverURL) ;
    }
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
            case "register" -> RegisterClient.register();
            //case "login" -> RegisterClient.login() ;
            // case "logout" -> RegisterClient.logout() ;
            // case "createGame" -> RegisterClient.createGame() ;
            //case "listGames" -> GameClient.listGames();
            //case "play" -> GameClient.play() ;
            //case "observe" -> GameClient.observeGame();
            case "quit" -> "quit";
            default -> help();
        };
    } catch (Exception ex) {
        return ex.getMessage();
    }
}
    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + SET_TEXT_COLOR_GREEN);
    }
}
