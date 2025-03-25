package ui;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

import static ui.EscapeSequences.*;
public class Board {
    // Board dimensions.
    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static final int SQUARE_SIZE_IN_PADDED_CHARS = 3;
    private static final int[] rowHeaders = {8, 7, 6, 5, 4, 3, 2, 1};
    private static String squareColor = SET_BG_COLOR_WHITE;
    private static String[] headers = {"a", "b", "c", "d", "e", "f", "g", "h"};
    private static String teamColor = null; // how do I fix the static aspect?

    public Board(String teamColor) {
        this.teamColor = teamColor;
    }

    // Padded characters.
    private static final String EMPTY = "   ";
// How should I set the position of each piece? like is the intial set or can i get intial positions from the server?

    private static Random rand = new Random();


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
        out.print(EMPTY.repeat(suffixLength)); // what is suffix length and prefix length?
    }

    private static void printHeaderText(PrintStream out, String character) {
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
        out.print(character);
    }

    private static void drawChessBoard(PrintStream out) {

        for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

            drawRowOfSquares(out);

            if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                out.print(SET_BG_COLOR_BLACK);
            }
        }
    }

    private static void drawRowOfSquares(PrintStream out) {
        int leftInt = 0;
        int rightInt = 8;
        if (teamColor.equalsIgnoreCase("black")) {
            leftInt = 8;
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
                if (Objects.equals(squareColor, SET_BG_COLOR_WHITE)) {
                    squareColor = SET_BG_COLOR_BLACK;
                } else {
                    squareColor = SET_BG_COLOR_WHITE;
                }

                if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                    int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                    int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                    out.print(EMPTY.repeat(prefixLength));
                    printPlayer(out, rand.nextBoolean() ? X : O);
                    out.print(EMPTY.repeat(suffixLength));
                } else {
                    out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                }

                out.print(rowHeaders[rightInt]);
                if (teamColor.equalsIgnoreCase("black")) {
                    leftInt++;
                } else {
                    leftInt--;
                }
                setBlack(out);
            }

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


    private static void printWhitePlayer(PrintStream out, String player) {
        out.print(SET_TEXT_COLOR_RED);

        out.print(player);

        setWhite(out);
    }

    private static void printBlackPlayer(PrintStream out, String player) {
        out.print(SET_BG_COLOR_WHITE);
        out.print(SET_TEXT_COLOR_BLUE);

        out.print(player);

        setWhite(out);
    }
}
