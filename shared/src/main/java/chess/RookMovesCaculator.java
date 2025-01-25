package chess;

import com.sun.source.tree.WhileLoopTree;

import java.util.ArrayList;
import java.util.Collection;

public class RookMovesCaculator implements PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        int[][] numbers = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
        Collection<ChessMove> movePossibilities = new ArrayList<>();
        // create an array list with all possible directions ex: ( v = 1, h = 0; v=0, h=1; v= -1, h=0; v=-1, h=0)
        for(int [] number : numbers) {
            int current_row = startPosition.getRow();
            int current_col = startPosition.getColumn();
            while (true) {
                current_row += number[0];
                current_col += number[1];
                if (current_row > 8 || current_col > 8 || current_row < 1 || current_col < 1) {
                    break;
                }
                ChessPosition current_position = new ChessPosition(current_row, current_col);
                if (board.getPiece(current_position) != null) {
                    if(board.getPiece(current_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                        ChessMove SingleMove = new ChessMove(startPosition, current_position, null);
                        movePossibilities.add(SingleMove);}
                    break;
                }

                ChessMove SingleMove = new ChessMove(startPosition, current_position, null);

                movePossibilities.add(SingleMove);
            }
        }
        return movePossibilities ;
    }
}

