package ui;

import chess.*;
import com.sun.jdi.Value;
import serverfacade.ServerFacade;
import model.*;
import websocket.ServerMessageObserver;
import websocket.WebSocketFacade;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;
import static ui.EscapeSequences.SET_TEXT_COLOR_RED;

public class GameClient implements ServerMessageObserver {
    private final ServerFacade server;
    private static GameClient instance ;
    private static HelperMethods helperMethods = HelperMethods.getInstance();
    private static Board board ;
    private static ChessBoard printBoard ;
    private static ChessGame currGame ;
    private static Integer currGameID ;
    private final WebSocketFacade ws ;
    private String message ;


    public GameClient(ServerFacade inputServer, WebSocketFacade ws) {
        this.server = inputServer ;
        this.ws = ws;
    }


    public String listGames() throws Exception {
        try {
            System.out.println("Let's get you those games!");
            GamesList gamesList = server.listCall(server.getAuthToken()) ;
            int counter = 1 ; // use game number and not game name for hashmap in server facade
            for(Game game: gamesList.games()){
                System.out.println(counter);
                counter++ ;
                System.out.println("Game Name: "+ game.gameName()) ;
                System.out.println("White Player: "+ game.whiteUsername()) ;
                System.out.println("Black Player: "+ game.blackUsername()) ;// This has to be the wrong way to do this?
            }
            return String.format("Please join or observe a specific game to see its current board. What's your next move? :)");
        } catch (Exception ex) {
            throw new Exception();
        }

    }

    public String createGame() throws Exception {
        try {
            System.out.println("What do you want to name this new game?");
            Scanner scanner = new Scanner(System.in);
            CreateGameName gameName = new CreateGameName(scanner.next()) ;
            server.createGameCall(gameName) ;
            return String.format("You have created the new Game: %s .  ", gameName.gameName());
        } catch (Exception ex) {
            throw new Exception();
        }
    }
    public String play() throws Exception {
        try {
            System.out.println("Please provide the game list number for the game you want to join?");
            printPrompt();
            Scanner scanner = new Scanner(System.in);
            int gameNum = Integer.parseInt(scanner.next()) ;
            int gameID = server.getGameNumList().get(gameNum) ;
            currGameID = gameID ;
            System.out.println("What team color do you want to be? (Black or White)");
            printPrompt();
            scanner = new Scanner(System.in);
            String color = scanner.next();
            JoinData joinData = new JoinData(helperMethods.colorVerificationHelp(color), gameID) ;
            server.joinGameCall(joinData) ;
            board = Board.getInstance(color);
            board.main(null);
            return String.format("Congrats! You are now apart of game # %s !", gameNum);
        } catch (Exception ex) {
            throw new Exception();
        }


    }
    public String observeGame() throws Exception {
        try { // should always give White prespective
            System.out.println("Please provide the game list number for the game you want to observe?");
            printPrompt();
            Scanner scanner = new Scanner(System.in);
            int gameNum = Integer.parseInt(scanner.next()) ;
            currGameID = server.getGameNumList().get(gameNum) ;
            board = Board.getInstance("white");
            board.main(null);
            return String.format("Congrats! You are now apart of game # %s !", gameNum);
        } catch (Exception ex) {
            throw new Exception();
        }
    }
    public String makeMove(AuthData authData) throws Exception {
        try { // should always give White prespective
            System.out.println("Start position:");
            printPrompt();
            ChessPosition startPosition = helperMethods.positionGetter(new Scanner(System.in)) ;
            System.out.println("End position:");
            printPrompt();
            ChessPosition endPosition = helperMethods.positionGetter(new Scanner(System.in)) ;
            ChessMove move = new ChessMove(startPosition, endPosition, null) ;
            ws.makeMove(authData.authToken(), currGameID, move);
            return message ; // what String should I return ?
        } catch (Exception ex) {
            throw new Exception();
        }
    }

    public String redraw() throws Exception {
        Board.main(printBoard);
        return "Here's your board!" ;
    }
    public String leave(AuthData authData) throws Exception {
        ws.leave(authData.authToken(), currGameID);
        return message ;
    }
    public String resign(AuthData authData) throws Exception {
        ws.resign(authData.authToken(), currGameID);
        return message ;
    }

    public String highlight() throws Exception {
        System.out.println("Piece position:");
        printPrompt();
        ChessPosition startPosition = helperMethods.positionGetter(new Scanner(System.in)) ;
        Collection<ChessMove> highlightMoves = currGame.validMoves(startPosition) ;
        Board.highlight(currGame, highlightMoves);
        return "Here are your available moves!" ;
    }


    public static GameClient getInstance(ServerFacade server, WebSocketFacade ws){
        if(instance == null){
            return instance = new GameClient(server, ws) ;
        }
        return instance ;
    }
    private void printPrompt() {
        System.out.print(">>> " + SET_TEXT_COLOR_GREEN); // should I keep the RESET thing?
    }

    @Override
    public void notify(ServerMessage serverMessage) {
        // check for which message it is "load game" "error" "ect."
        if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME){
            Board.main(null); //
            LoadGameMessage loadGameMessage = (LoadGameMessage) serverMessage;
            currGame = loadGameMessage.getGame() ;
            printBoard = currGame.getBoard() ;
            message = loadGameMessage.getMessage() ;
        } else if (serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.NOTIFICATION) {
            NotificationMessage noteMessage = (NotificationMessage) serverMessage;
            message = noteMessage.getNotificationMessage() ;
        }
        else if(serverMessage.getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            ErrorMessage errorMessage = (ErrorMessage) serverMessage;
            message = errorMessage.getErrorMessage() ;
        }
        printPrompt();
    }
}
