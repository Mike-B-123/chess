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
    private static ServerFacade server = null;
    public String serverUrl;
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int[] rowHeaders = {8, 7, 6, 5, 4, 3, 2, 1};
    private static String squareColor = SET_BG_COLOR_WHITE;
    private static String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static String teamColor = null; // how do I fix the static aspect?
    private static HashMap<ChessPiece.PieceType, String> pieceMap ;

    public Board(String teamColor, String serverUrl) {
        this.teamColor = teamColor;
        server = new ServerFacade(serverUrl);
        setPieceMap();
    }
    // Padded characters.
    private static final String EMPTY = "   ";


    public static void main(String[] args) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(ERASE_SCREEN);

        drawHeaders(out);
        drawChessBoard(out);
        drawFooter(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        if (teamColor.equalsIgnoreCase("white")) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = 8; boardCol != 0; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }
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
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(character);
    }

    private static void drawChessBoard(PrintStream out) {
            int inverseRow = 7 ;
        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {
            out.print(SET_BG_COLOR_DARK_GREY);
            if(teamColor.equalsIgnoreCase("white")){
            out.print(rowHeaders[boardRow]);
            }
            else{
                out.print(rowHeaders[inverseRow]);
                -- inverseRow;
            }
            drawRowOfSquares(out, boardRow);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(SET_BG_COLOR_BLACK);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out,Integer positionRow) {
        int leftInt = 0;
        int rightInt = 7;
        int positionCol = 0 ;
        if (teamColor.equalsIgnoreCase("black")) {
            leftInt = 7;
            rightInt = 0;
        }
        for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
            out.print(rowHeaders[leftInt]);
            if (teamColor.equalsIgnoreCase("black")) {
                leftInt--;
            } else {
                leftInt++;
            }
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                out.print(squareColor);
                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                    out.print(EMPTY.repeat(prefixLength));
                    printPiece(positionRow, positionCol, out);
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }
                if (boardCol < BOARD_SIZE_IN_SQUARES - 1) {
                    // change square color and increase column number
                    positionCol ++ ;
                    changeSquareColor();
                }
                if (teamColor.equalsIgnoreCase("black")) {
                    leftInt++;
                } else {
                    leftInt--;
                }
            }
            out.print(SET_BG_COLOR_DARK_GREY) ;
            out.print(rowHeaders[rightInt]);
            out.print(SET_BG_COLOR_BLACK) ;
            out.println();
        }
    }

    private static void drawFooter(PrintStream out) {
        out.print(SET_BG_COLOR_DARK_GREY);
        if (teamColor.equalsIgnoreCase("black")) {
            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        } else {
            for (int boardCol = 8; boardCol != 0; --boardCol) {
                drawHeader(out, headers[boardCol]);
            }
        }
        out.println();
    }
    private static void printPiece(int positionRow, int positionCol, PrintStream out){
        ChessPiece piece = server.getCurrentChessBoard().getPiece(new ChessPosition(positionRow, positionCol)) ;
        if(piece.getTeamColor().equals(ChessGame.TeamColor.BLACK)){
            out.print(SET_TEXT_COLOR_BLUE);
            out.print(pieceMap.get(piece.getPieceType()));
        }
        else{
            out.print(SET_TEXT_COLOR_RED);
            out.print(pieceMap.get(piece.getPieceType()));
        }
    }

    private static void changeSquareColor(){
        if(squareColor.equalsIgnoreCase("black")){
            squareColor = "white" ;
        }
        else{
            squareColor = "black";
        }
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
