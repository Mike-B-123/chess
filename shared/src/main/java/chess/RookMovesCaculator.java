package chess;

import com.sun.source.tree.WhileLoopTree;

import java.util.Collection;

public class RookMovesCaculator implements PieceMovesCaculator {
    private int vertical_direction ;
    private int horizontal_direction ;
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        int count = 0 ;
        // create an array list with all possible directions ex: ( v = 1, h = 0; v=0, h=1; v= -1, h=0; v=-1, h=0)
        while(count != 4){
            int current_row = 0;
            int current_col = 0;
            while(true){
                current_col += horizontal_direction ;
                current_row += vertical_direction ;
                if(current_row > 8 || current_col > 8){
                    break ;
                }
                if(board[current_col][current_col] != null){
                    continue ;
                }
                Collection<ChessMove>.add(current_row, current_col) ;
            }
            }

        }

    }
}
