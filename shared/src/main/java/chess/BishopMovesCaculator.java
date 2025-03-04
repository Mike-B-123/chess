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
