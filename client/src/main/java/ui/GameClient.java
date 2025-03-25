package ui;

import ServerFacade.ServerFacade;
import chess.ChessGame;
import model.*;
import ui.HelperMethods ;

import java.util.Scanner;

public class GameClient {
    private final ServerFacade server;
    public String serverUrl;
    private static GameClient instance ;
    private static HelperMethods helperMethods = HelperMethods.getInstance();


    public GameClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String listGames() throws Exception {
        try {
            System.out.println("Let's get you those games!");
            GamesList gamesList= server.listCall(server.getAuthToken()) ;
            for(Game game: gamesList.games()){
                System.out.println("Game Name"+ game.gameName()) ;
                System.out.println("GameID"+ game.gameID()) ;
                System.out.println("White Player"+ game.whiteUsername()) ;
                System.out.println("Black Player"+ game.blackUsername()) ;
                System.out.println("Please join or observe a specific game to see its current board. What's your next move? :)") ;// This has to be the wrong way to do this?
            }
            return String.format("What's your next 'move'? ;) ");
        } catch (Exception ex) {
            throw new Exception();
        }

    }

    public String createGame() throws Exception {
        try {
            System.out.println("What do you want to name this new game?");
            Scanner scanner = new Scanner(System.in); // this is an input stream and can be read from, and can take in a ton of different things like files
            CreateGameName gameName = new CreateGameName(scanner.next()) ;
            server.createGameCall(gameName) ;
            return String.format("You have created the new Game ' %s. ' ", gameName);
        } catch (Exception ex) {
            throw new Exception();
        }
    }
    public String play() throws Exception {
        try {
            System.out.println("Please provide the game name for the game you want to join?");
            Scanner scanner = new Scanner(System.in);
            String gameName = scanner.next() ;
            int gameID = server.getGameNameList().get(gameName) ; // will this work?
            System.out.println("What team color do you want to be? (Black or White)");
            scanner = new Scanner(System.in);
            String color = scanner.next();
            JoinData joinData = new JoinData(helperMethods.colorVerificationHelp(color), gameID) ;
            server.joinGameCall(joinData) ;
            return String.format("Congrats! You are now apart of %s !", gameName);
        } catch (Exception ex) {
            throw new Exception();
        }


    }
    public String observeGame() throws Exception {
        try {
            System.out.println("Please provide the game name for the game you want to observe?");
            Scanner scanner = new Scanner(System.in);
            String gameName = scanner.next() ;
            int gameID = server.getGameNameList().get(gameName) ;
            System.out.println("What team color do you want to observe? (Black or White)");
            scanner = new Scanner(System.in);
            String color = scanner.next();
            JoinData joinData = new JoinData(helperMethods.colorVerificationHelp(color) , gameID) ;
            server.joinGameCall(joinData) ;
            return String.format("Congrats! You are now observing %s !", gameName);
        } catch (Exception ex) {
            throw new Exception();
        }
    }
    public static GameClient getInstance(){
        if(instance == null){
            return instance = new GameClient(instance.serverUrl) ;
        }
        return instance ;
    }
}
