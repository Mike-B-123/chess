package ui;

import model.AuthData;
import serverfacade.ServerFacade;

import java.util.Scanner;
import websocket.ServerMessageObserver;
import websocket.WebSocketFacade;

import static ui.EscapeSequences.*;

public class REPL {

    private State state = State.SIGNEDOUT ;
    private Boolean inGame = Boolean.FALSE ;
    ServerFacade serverFacade = ServerFacade.getInstance(8080);
    WebSocketFacade ws = new WebSocketFacade("http://localhost:8080") ;
    private AuthData currentAuthData ;

    public REPL() throws Exception {}

    RegisterClient registerClient = new RegisterClient(serverFacade) ;
    GameClient gameClient = GameClient.getInstance(serverFacade, ws);

    public void run() throws Exception {
        System.out.println("Ready to play some Chess? Let's go!!");
        System.out.print(help()) ;
        printPrompt();
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (result == null || !result.equals("quit")) { // is the result == null ok?
            String line = scanner.next();
            try {
                result = eval(line);
                if(result.contains("You signed in as")) {
                    currentAuthData = registerClient.getCurrentAuthData() ;
                    setState(State.SIGNEDIN);
                } else if (result.contains("signed out")) {
                    setState(State.SIGNEDOUT);
                }
                if(result.contains("apart")){
                    inGame = Boolean.TRUE ;
                }
                System.out.print(result);
                System.out.print(SET_TEXT_COLOR_GREEN);
                printPrompt();
            } catch (Throwable e) {
                System.out.println("An Error has occured. Restart and try again. Tip: Focus on giving the EXACT inputs. :)") ;
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
        if(inGame == Boolean.TRUE) {
            return """
                    - help
                    - redraw (redraws board)
                    - leave (leaves game)
                    - move
                    - resign
                    - highlight (hightlights legal moves)
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
            case "play" -> gameClient.play(currentAuthData) ;
            case "observe" -> gameClient.observeGame(currentAuthData);// how in the world do we do this?
            case "redraw" -> gameClient.redraw() ;
            case "leave" -> gameClient.leave(currentAuthData);
            case "move" -> gameClient.makeMove(currentAuthData) ;
            case "resign" -> gameClient.resign(currentAuthData);
            case "highlight" -> gameClient.highlight();
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

// SP: a1 to EP: B1 letters can be numbers with 'a' - 96 = 1; watch out for row and column order col = value.charAt(0)
// Questions:
// 1. Who should be calling my WS and should I be passing it into my clients? like
// 2. How do I get the game from my LOAD_GAME message?
// 3. Should I be loading the game from my REPL or game client? (client)
// 4. should I be passing in my chessboard into board?
// 5. Do I add new optionality to the REPL? for connect, make move, ect.
