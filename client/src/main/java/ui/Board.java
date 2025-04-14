package ui;
import chess.*;
import serverfacade.ServerFacade;
import websocket.messages.NotificationMessage;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
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
    private static ChessGame.TeamColor teamColor ; // how do I fix the static aspect?
    private static HashMap<ChessPiece.PieceType, String> pieceMap = new HashMap<>();
    private static Board instance ;
    private static ChessBoard currentBoard ;
    private static Collection<ChessPosition> highlightPositions ;
    private static Boolean highlighting = Boolean.FALSE ;

    public Board() {
        server = new ServerFacade(8080);
    }
    // Padded characters.
    private static final String EMPTY = "   ";

    public static void main(ChessBoard chessBoard, ChessGame.TeamColor inputColor) {
        currentBoard = chessBoard ;
        teamColor = inputColor;
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        setPieceMap();
        out.print(ERASE_SCREEN);
        out.println();

        drawHeaders(out);
        drawChessBoard(out);
        drawHeaders(out);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    private static void drawHeaders(PrintStream out) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(" ");
        if (teamColor == ChessGame.TeamColor.WHITE) {
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
        int iterator = -1;
        int row = 7 ;
        int col = 0 ;
        if (teamColor == ChessGame.TeamColor.BLACK) {
            leftInt = 7;
            rightInt = 7;
            row = 0 ;
            col = 7 ;
            iterator = 1 ;
        }

        while(row < 8 && row  >=0){
            int squareRow = row ;
            row = row + iterator ;
            out.print(SET_TEXT_COLOR_WHITE);
            out.print(SET_BG_COLOR_LIGHT_GREY);
            out.print(ROW_HEADERS[leftInt]);
            leftInt = leftIntMath(leftInt);
            int positionCol = 0 ;
            if (teamColor == ChessGame.TeamColor.BLACK){
                col = 7;
            }
            else{
                col = 0 ;
            }
            while(col < 8 && col  >=0) {
                int boardCol = col ;
                col = col - iterator ;
                if(highlighting) {
                    ChessPosition testPos = new ChessPosition(squareRow, boardCol);
                    if (highlightPositions.contains(testPos)) {
                        out.print(SET_BG_COLOR_GREEN);
                    }
                }
                else{
                  out.print(squareColor);
                }
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;
                    out.print(EMPTY.repeat(prefixLength));
                    ChessPosition position = new ChessPosition(squareRow + 1, boardCol + 1) ;
                    ChessPiece piece = currentBoard.getPiece(position) ;
                    if(piece.getPieceType() == ChessPiece.PieceType.PAWN && boardCol == 7 || boardCol == 0){

                    }
                if (piece != null) {
                    pieceHelper(piece, out);
                }
                else{
                    out.print(" ");
                }
                    out.print(EMPTY.repeat(suffixLength));

                if (positionCol < BOARD_SIZE_IN_SQUARES - 1) {
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


    private static void printUPPiece(int positionRow , int positionCol, PrintStream out) {
        positionCol += 1;
        positionRow += 1;
        ChessPiece piece = currentBoard.getPiece(new ChessPosition(positionRow, positionCol));
        pieceHelper(piece, out);
    }
    private static void printPiece(int positionRow , int positionCol, PrintStream out) {
        ChessPiece piece = currentBoard.getPiece(new ChessPosition(positionRow, positionCol));
        pieceHelper(piece, out);
    }
    private static void pieceHelper(ChessPiece piece, PrintStream out){
            if (piece.getTeamColor().equals(ChessGame.TeamColor.WHITE)) {
                out.print(SET_TEXT_COLOR_RED);
                out.print(pieceMap.get(piece.getPieceType()));
            } else {
                out.print(SET_TEXT_COLOR_BLUE);
                out.print(pieceMap.get(piece.getPieceType()));
        }
    }
    private static void printDownPiece(int positionRow , int positionCol, PrintStream out) {
        positionCol += 1;
        positionRow += 1;
        int trueCol = 9 - positionCol;
        int trueRow = 9 - positionRow ;
        ChessPiece piece = currentBoard.getPiece(new ChessPosition(trueRow, trueCol));
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
        if(teamColor == ChessGame.TeamColor.WHITE){
            leftInt ++ ;
        }
        else{
           leftInt -- ;
        }
        return leftInt ;
    }

    private static int rightIntMath(int rightInt){
        if(teamColor == ChessGame.TeamColor.WHITE){
            rightInt ++ ;
        }
        else{
            rightInt -- ;
        }
        return rightInt ;
    }

    public static void highlight(ChessGame game , Collection<ChessMove> validMoves) {
        highlighting = Boolean.TRUE ;
        for(ChessMove move: validMoves){
            highlightPositions.add(move.getEndPosition()) ;
        }
        main(game.getBoard(), game.getTeamTurn()) ;
        highlighting = Boolean.FALSE ;
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
            return instance = new Board() ;
    }
}
