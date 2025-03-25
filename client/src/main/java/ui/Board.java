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
        private static final int[] rowHeaders = {8,7,6,5,4,3,2,1} ;
        private static String squareColor = SET_BG_COLOR_WHITE ;
        private static String[] headers = { "a", "b", "c", "d", "e", "f", "g", "h" };

        // Padded characters.
        private static final String EMPTY = "   ";
// How should I set the position of each piece?

        private static Random rand = new Random();


        public static void main(String[] args) {
            var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

            out.print(ERASE_SCREEN);

            drawHeaders(out);

            drawChessBoard(out);

            out.print(SET_BG_COLOR_BLACK);
            out.print(SET_TEXT_COLOR_WHITE);
        }

        private static void drawHeaders(PrintStream out) {

            setBlack(out);

            for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                drawHeader(out, headers[boardCol]);
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
            setBlack(out);
        }

        private static void drawChessBoard(PrintStream out) {

            for (int boardRow = 0; boardRow < BOARD_SIZE_IN_SQUARES; ++boardRow) {

                drawRowOfSquares(out);

                if (boardRow < BOARD_SIZE_IN_SQUARES - 1) {
                    setBlack(out);
                }
            }
        }

        private static void drawRowOfSquares(PrintStream out) {

            for (int squareRow = 0; squareRow < SQUARE_SIZE_IN_PADDED_CHARS; ++squareRow) {
                out.print(rowHeaders[squareRow]) ;
                for (int boardCol = 0; boardCol < BOARD_SIZE_IN_SQUARES; ++boardCol) {
                    out.print(squareColor);
                    if(Objects.equals(squareColor, SET_BG_COLOR_WHITE)){
                        squareColor = SET_BG_COLOR_BLACK ;
                    }
                    else{
                        squareColor = SET_BG_COLOR_WHITE ;
                    }

                    if (squareRow == SQUARE_SIZE_IN_PADDED_CHARS / 2) {
                        int prefixLength = SQUARE_SIZE_IN_PADDED_CHARS / 2;
                        int suffixLength = SQUARE_SIZE_IN_PADDED_CHARS - prefixLength - 1;

                        out.print(EMPTY.repeat(prefixLength));
                        printPlayer(out, rand.nextBoolean() ? X : O);
                        out.print(EMPTY.repeat(suffixLength));
                    }
                    else {
                        out.print(EMPTY.repeat(SQUARE_SIZE_IN_PADDED_CHARS));
                    }


                    setBlack(out);
                }

                out.println();
            }
        }


        private static void setWhite(PrintStream out) {
            out.print(SET_BG_COLOR_WHITE);
        }

        private static void setRed(PrintStream out) {
            out.print(SET_BG_COLOR_RED);
            out.print(SET_TEXT_COLOR_RED);
        }

        private static void setBlack(PrintStream out) {
            SET_BG_COLOR_BLACK;
        }

        private static void printWhitePlayer(PrintStream out, String player) {
            out.print(SET_BG_COLOR_WHITE);
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
