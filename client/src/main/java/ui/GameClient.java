package ui;

import ServerFacade.ServerFacade;
import chess.ChessGame;
import model.CreateGameName;
import model.JoinData;
import model.User;
import ui.HelperMethods ;

import java.util.Scanner;

public class GameClient {
    private final ServerFacade server;
    public String serverUrl;
    private static GameClient instance ;


    public GameClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
    }

    public String listGames() throws Exception {
        try {
            System.out.println("Let's get you those games!");
            server.listCall(server.getAuthToken()) ;
            // how should I list the games?
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
            ChessGame.TeamColor teamColor = null;
            System.out.println("Please provide the game ID for the game you want to join?");
            Scanner scanner = new Scanner(System.in);
            int gameID = Integer.getInteger(scanner.next()); // will this work?
            System.out.println("What team color do you want to be? (Black or White)");
            scanner = new Scanner(System.in);
            String color = scanner.next();
            if(color.toLowerCase() == "black"){
                teamColor = ChessGame.TeamColor.BLACK ;
            }
            else if (color.toLowerCase() == "white") {
                teamColor = ChessGame.TeamColor.WHITE ;
            }
            JoinData joinData = new JoinData(teamColor, gameID) ;
            server.joinGameCall(joinData) ;
            return String.format("Congrats! You are now apart of the game!");
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
