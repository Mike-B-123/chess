package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

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

    public ChessPosition positionGetter(Scanner scanner){
        String start = scanner.next() ;
        int col = start.charAt(0) - 96 ;
        int row = start.charAt(1) - 48 ;
        ChessPosition position = new ChessPosition(row, col) ;
        return position ;
    }
    public ChessPiece.PieceType promotionCaculator(String rawPiece){
        ChessPiece.PieceType piece = ChessPiece.PieceType.QUEEN ;
        if(rawPiece.equalsIgnoreCase("knight")){
            piece = ChessPiece.PieceType.KNIGHT ;
        } else if (rawPiece.equalsIgnoreCase("rook")) {
            piece = ChessPiece.PieceType.ROOK ;
        } else if (rawPiece.equalsIgnoreCase("bishop")) {
            piece = ChessPiece.PieceType.BISHOP ;
        }
        return piece ;
    }
}
