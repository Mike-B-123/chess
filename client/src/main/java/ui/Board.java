package ui;
import ServerFacade.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;
public class Board {
    // Board dimensions.
    private static ServerFacade server = new ServerFacade("http://localhost:8080");
    public String serverUrl;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int[] rowHeaders = {8, 7, 6, 5, 4, 3, 2, 1};
    private static String squareColor = SET_BG_COLOR_WHITE;
    private static String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static String teamColor = "white"; // how do I fix the static aspect?
    private static HashMap<ChessPiece.PieceType, String> pieceMap = new HashMap<>();

    public Board(String teamColor, String serverUrl) {
        this.teamColor = teamColor;
        server = new ServerFacade(serverUrl);
    }
    // Padded characters.
    private static final String EMPTY = "   ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        setPieceMap();
        out.print(ERASE_SCREEN);
        if(teamColor.equalsIgnoreCase("black")){
            squareColor = SET_BG_COLOR_BLACK ;
        }

        drawHeaders(out);
        drawChessBoard(out);
        drawFooter(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print("1");
        if (teamColor.equalsIgnoreCase("white")) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = 7; boardCol != -1; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }


    private static void drawHeader(PrintStream out, String headerText) {
        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
        out.print(EMPTY.repeat(prefixLength));
        printHeaderText(out, headerText);
        out.print(EMPTY.repeat(suffixLength));
    }

    private static void printHeaderText(PrintStream out, String character) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(character);
    }


    private static void drawChessBoard(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        int rightInt = 7;
        int leftInt = 0;
        if (teamColor.equalsIgnoreCase("black")) {
            leftInt = 7;
            rightInt = 0;
        }
        for (int squareRow = 0; squareRow < 8; ++squareRow) {
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(rowHeaders[leftInt]);
            leftInt = leftIntMath(leftInt);
            int positionCol = 0 ;
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                out.print(squareColor);
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                    out.print(EMPTY.repeat(prefixLength));
                    printPiece(squareRow, positionCol, out);
                    out.print(EMPTY.repeat(suffixLength));

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // change square color and increase column number
                    positionCol += 1 ;
                    changeSquareColor();
                }
            }
            out.print(SET_BG_COLOR_LIGHT_GREY) ;
            out.print(SET_TEXT_COLOR_WHITE) ;
            out.print(rowHeaders[rightInt]);
            rightInt = rightIntMath(rightInt) ;
            out.print(SET_BG_COLOR_BLACK) ;
            out.println();
        }
    }

    private static void drawFooter(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(SET_TEXT_COLOR_LIGHT_GREY);
        out.print("1");
        if (teamColor.equalsIgnoreCase("black")) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = 7; boardCol != -1; --boardCol) {

                drawHeader(out, headers[boardCol]);
            }
        }
        out.print(SET_BG_COLOR_BLACK);
        out.println();
    }
    private static void printPiece(int positionRow , int positionCol, PrintStream out) {
        positionCol += 1;
        positionRow += 1;
        ChessPiece piece = server.getCurrentChessBoard().getPiece(new ChessPosition(positionRow, positionCol));
        if (piece != null) {
            if (piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)) {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(pieceMap.get(piece.getPieceType()));
            } else {
                out.print(SET_TEXT_COLOR_RED);
                out.print(pieceMap.get(piece.getPieceType()));
            }
        } else {
            out.print(" ");
        }
    }

    private static void changeSquareColor(){
        if(squareColor == SET_BG_COLOR_BLACK){
            squareColor = SET_BG_COLOR_WHITE ;
        }
        else{
            squareColor = SET_BG_COLOR_BLACK;
        }
    }

    private static int leftIntMath(int leftInt){
        if(teamColor.equalsIgnoreCase("white")){
            leftInt ++ ;
        }
        else{
           leftInt -- ;
        }
        return leftInt ;
    }

    private static int rightIntMath(int rightInt){
        if(teamColor.equalsIgnoreCase("white")){
            rightInt -- ;
        }
        else{
            rightInt ++ ;
        }
        return rightInt ;
    }

    public static void setPieceMap(){
        pieceMap.put(ChessPiece.PieceType.KING, KING);
        pieceMap.put(ChessPiece.PieceType.QUEEN, QUEEN);
        pieceMap.put(ChessPiece.PieceType.BISHOP, BISHOP);
        pieceMap.put(ChessPiece.PieceType.KNIGHT, KNIGHT);
        pieceMap.put(ChessPiece.PieceType.ROOK, ROOK);
        pieceMap.put(ChessPiece.PieceType.PAWN, PAWN);
    }
}
