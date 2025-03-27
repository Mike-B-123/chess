package ui;

import chess.ChessGame;

import java.util.Scanner;

public class HelperMethods {
    private static HelperMethods instance ;
    public HelperMethods(){}
    public String[] varLoop(String[] variables, String[] outPrompts){
        for (int count = 0; count != outPrompts.length; count++) {
            System.out.println(outPrompts[count]);
            Scanner scanner = new Scanner(System.in);
            variables[count] = scanner.next() ; // {john password email}
        }
        return variables ;
    }
    public static HelperMethods getInstance(){
        if(instance == null){
            return instance = new HelperMethods() ;
        }
        return instance ;
    }

    public ChessGame.TeamColor colorVerificationHelp(String color){
        ChessGame.TeamColor teamColor = null;
        if(color.equalsIgnoreCase("black")){
            teamColor = ChessGame.TeamColor.BLACK ;
        }
        else if (color.equalsIgnoreCase("white")) {
            teamColor = ChessGame.TeamColor.WHITE ;
        }
        return teamColor ;
    }
}
