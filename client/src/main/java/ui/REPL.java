package ui;

import java.util.Scanner;

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

        }

    }

    public enum State{
        SIGNEDOUT ,
        SIGNEDIN
    }
    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
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
        var tokens = input.toLowerCase().split(" ");
        var cmd = (tokens.length > 0) ? tokens[0] : "help";
        var params = Arrays.copyOfRange(tokens, 1, tokens.length);
        return switch (cmd) {
            case "signin" -> signIn(params);
            case "rescue" -> rescuePet(params);
            case "list" -> listPets();
            case "signout" -> signOut();
            case "adopt" -> adoptPet(params);
            case "adoptall" -> adoptAllPets();
            case "quit" -> "quit";
            default -> help();
        };
    } catch (ResponseException ex) {
        return ex.getMessage();
    }
}
}
