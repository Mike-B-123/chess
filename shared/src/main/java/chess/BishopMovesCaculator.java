package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCaculator implements PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        int[][] numbers = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        Collection<ChessMove> movePossibilities = new ArrayList<>();
        for(int [] number : numbers) {
            int currentRow = startPosition.getRow();
            int currentCol = startPosition.getColumn();
            while (true) {
                currentRow += number[0];
                currentCol += number[1];
                if (currentRow > 8 || currentCol > 8 || currentRow < 1 || currentCol < 1) {
                    break;
                }
                ChessPosition current_position = new ChessPosition(currentRow, currentCol);
                if (board.getPiece(current_position) != null) {
                    if(board.getPiece(current_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                        ChessMove singleMove = new ChessMove(startPosition, current_position, null);
                        movePossibilities.add(singleMove);}
                    break;
                }

                ChessMove singleMove = new ChessMove(startPosition, current_position, null);
                movePossibilities.add(singleMove);
            }
        }
        return movePossibilities ;
    }
}
