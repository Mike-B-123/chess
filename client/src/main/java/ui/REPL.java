package ui;

import serverfacade.ServerFacade;

import java.util.Scanner;
import websocket.ServerMessageObserver;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import static ui.EscapeSequences.*;

public class REPL implements ServerMessageObserver {

    private State state = State.SIGNEDOUT ;
    private Boolean inGame = false ;
    ServerFacade serverFacade = ServerFacade.getInstance(8080);
    ServerMessageObserver notificationHandler;
    WebSocketFacade ws = new WebSocketFacade("http://localhost:8080", notificationHandler) ;

    public REPL() throws Exception {}

    RegisterClient registerClient = RegisterClient.getInstance(serverFacade);
    GameClient gameClient = GameClient.getInstance(serverFacade);

    public void run() throws Exception {
        System.out.println("Ready to play some Chess? First sign in! :)");
        System.out.print(help()) ;
        printPrompt();
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            String line = scanner.next();
            try {
                result = eval(line);
                if(result.contains("You signed in as")) {
                    setState(State.SIGNEDIN);
                } else if (result.contains("signed out")) {
                    setState(State.SIGNEDOUT);
                }
                System.out.print(result);
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
        if(inGame = true) {
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
            case "play" -> gameClient.play() ;
            case "observe" -> gameClient.observeGame();// how in the world do we do this?
            case "redraw" -> "placeholder" ;
            case "leave" -> "placeholder" ;
            case "move" -> "move" ;
            case "resign" -> "resign" ;
            case "highlight" -> "highlight" ; // are these additions ok?
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

    @Override
    public void notify(ServerMessage serverMessage) {
        // check for which message it is "load game" "error" "ect."
        if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            Board.main(null); //
            LoadGameMessage LGM = (LoadGameMessage) serverMessage;
            LGM.getGame() ;
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage noteMessage = (NotificationMessage) serverMessage;
            System.out.println(SET_TEXT_COLOR_RED + noteMessage.getNotificationMessage());
        }
        else if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage noteMessage = (ErrorMessage) serverMessage;
            System.out.println(SET_TEXT_COLOR_RED + noteMessage.getErrorMessage());
        }
        printPrompt();
    }
}

// SP: a1 to EP: B1 letters can be numbers with 'a' - 96 = 1; watch out for row and column order col = value.charAt(0)
// Questions:
// 1. Who should be calling my WS and should I be passing it into my clients? like
// 2. How do I get the game from my LOAD_GAME message?
// 3. Should I be loading the game from my REPL or game client? (client)
// 4. should I be passing in my chessboard into board?
// 5. Do I add new optionality to the REPL? for connect, make move, ect.
