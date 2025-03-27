package ui;

import serverfacade.ServerFacade;
import chess.ChessGame;
import model.*;

import java.util.Scanner;

import static ui.EscapeSequences.SET_TEXT_COLOR_GREEN;

public class GameClient {
    private final ServerFacade server;
    private static GameClient instance ;
    private static HelperMethods helperMethods = HelperMethods.getInstance();
    private static Board board ;


    public GameClient(ServerFacade inputServer) {
        this.server = inputServer ;
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
            int gameID = server.getGameNumList().get(gameNum) ; // will this work?
            System.out.println("What team color do you want to be? (Black or White)");
            printPrompt();
            scanner = new Scanner(System.in);
            String color = scanner.next();
            JoinData joinData = new JoinData(helperMethods.colorVerificationHelp(color), gameID) ;
            server.joinGameCall(joinData) ;
            board = Board.getInstance("white");
            board.main();
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
            int gameID = server.getGameNumList().get(gameNum) ; // will this work?
            System.out.println("What team color do you want to observe?");
            printPrompt();
            scanner = new Scanner(System.in);
            JoinData joinData = new JoinData(ChessGame.TeamColor.WHITE, gameID) ;
            server.joinGameCall(joinData) ;
            board = Board.getInstance("white");
            board.main();
            return String.format("Congrats! You are now apart of game # %s !", gameNum);
        } catch (Exception ex) {
            throw new Exception();
        }
    }
    public static GameClient getInstance(ServerFacade server){
        if(instance == null){
            return instance = new GameClient(server) ;
        }
        return instance ;
    }
    private void printPrompt() {
        System.out.print(">>> " + SET_TEXT_COLOR_GREEN); // should I keep the RESET thing?
    }
}
