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
            int currentRow = startPosition.getRow();
            int currentCol = startPosition.getColumn();
            while (true) {
                currentRow += number[0];
                currentCol += number[1];
                if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                    break;
                }
                ChessPosition currentPosition = new ChessPosition(currentRow, currentCol);
                if (board.getPiece(currentPosition) != null) {
                    if(board.getPiece(currentPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                        ChessMove singleMove = new ChessMove(startPosition, currentPosition, null);
                        movePossibilities.add(singleMove);}
                    break;
                }

                ChessMove singleMove = new ChessMove(startPosition, currentPosition, null);
                movePossibilities.add(singleMove);
            }
        }
        return movePossibilities ;
    }
}

