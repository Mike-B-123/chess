package ui;
import serverfacade.ServerFacade;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;

import static ui.EscapeSequences.*;
public class Board {
    // Board dimensions.
    private static ServerFacade server = new ServerFacade(8080);
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int[] ROW_HEADERS = {8, 7, 6, 5, 4, 3, 2, 1};
    private static String squareColor = SET_BG_COLOR_WHITE;
    private static String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static String teamColor ; // how do I fix the static aspect?
    private static HashMap<ChessPiece.PieceType, String> pieceMap = new HashMap<>();
    private static Board instance ;

    public Board(String teamColor) {
        this.teamColor = teamColor;
        server = new ServerFacade(8080);
    }
    // Padded characters.
    private static final String EMPTY = "   ";

    public static void main() {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        setPieceMap();
        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" ");
        if (teamColor.toLowerCase().contentEquals("white")) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = 7; boardCol != -1; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" ");
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
        int rightInt = 0;
        int leftInt = 0;
        if (teamColor.toLowerCase().contentEquals("black")) {
            leftInt = 7;
            rightInt = 7;
        }
        for (int squareRow = 0; squareRow < 8; ++squareRow) {
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(ROW_HEADERS[leftInt]);
            leftInt = leftIntMath(leftInt);
            int positionCol = 0 ;
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                out.print(squareColor);
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                    out.print(EMPTY.repeat(prefixLength));
                    if(teamColor.toLowerCase().contentEquals("white")){
                    printWhitePiece(squareRow, positionCol, out);
                    }
                    else{
                        printBlackPiece(squareRow, positionCol, out);
                    }
                    out.print(EMPTY.repeat(suffixLength));

                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // change square color and increase column number
                    positionCol += 1 ;
                    changeSquareColor();
                }
            }
            out.print(SET_BG_COLOR_LIGHT_GREY) ;
            out.print(SET_TEXT_COLOR_WHITE) ;
            out.print(ROW_HEADERS[rightInt]);
            rightInt = rightIntMath(rightInt) ;
            out.print(SET_BG_COLOR_BLACK) ;
            out.println();
        }
    }


    private static void printWhitePiece(int positionRow , int positionCol, PrintStream out) {
        positionCol += 1;
        positionRow += 1;
        ChessPiece piece = server.getCurrentChessBoard().getPiece(new ChessPosition(positionRow, positionCol));
        pieceHelper(piece, out);
    }
    private static void pieceHelper(ChessPiece piece, PrintStream out){
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
    private static void printBlackPiece(int positionRow , int positionCol, PrintStream out) {
        positionCol += 1;
        positionRow += 1;
        int trueCol = 9 - positionCol;
        int trueRow = 9 - positionRow ;
        ChessPiece piece = server.getCurrentChessBoard().getPiece(new ChessPosition(trueRow, trueCol));
        pieceHelper(piece, out);
    }

    private static void changeSquareColor(){
        if(Objects.equals(squareColor, SET_BG_COLOR_BLACK)){
            squareColor = SET_BG_COLOR_WHITE ;
        }
        else{
            squareColor = SET_BG_COLOR_BLACK;
        }
    }

    private static int leftIntMath(int leftInt){
        if(teamColor.toLowerCase().contentEquals("white")){
            leftInt ++ ;
        }
        else{
           leftInt -- ;
        }
        return leftInt ;
    }

    private static int rightIntMath(int rightInt){
        if(teamColor.toLowerCase().contentEquals("white")){
            rightInt ++ ;
        }
        else{
            rightInt -- ;
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
    public static Board getInstance(String color){
            return instance = new Board(color) ;
    }
}
