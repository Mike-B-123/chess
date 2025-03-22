package ui;

import java.util.Arrays;
import java.util.Scanner;
import static ui.EscapeSequences.*;

public class REPL {
    //private final JoinClient joinClient ;
    private final RegisterClient registerClient ;
    //private final BrowsingClient browsingClient ;
    private State state = State.SIGNEDOUT ;
    public REPL(String serverURL){
        //joinClient = new JoinClient(serverURL, this) ;
        registerClient = new RegisterClient(serverURL) ;
        //browsingClient = new BrowsingClient(serverURL, this) ;
    }
    public void run(String serverURL){
        System.out.println("\uD83D\uDC36 Ready to play some Chess? First sign in.");
        System.out.print(help()) ;
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.next();
            try {
                result = eval(line);
                System.out.print(BLUE + result);
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
                    - signIn
                    - quit
                    """;
        }
        return """
                - list
                - joinGame <gameID>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }
public String eval(String input) {
    try {
        return switch (input.toLowerCase()) {
            case "signin" -> RegisterClient.register();
            case "rescue" -> rescuePet(params);
            case "list" -> listPets();
            case "signout" -> signOut();
            case "adopt" -> adoptPet(params);
            case "adoptall" -> adoptAllPets();
            case "quit" -> "quit";
            default -> help();
        };
    } catch (Exception ex) {
        return ex.getMessage();
    }
}
    private void printPrompt() {
        System.out.print("\n" + RESET + ">>> " + GREEN);
    }
}
