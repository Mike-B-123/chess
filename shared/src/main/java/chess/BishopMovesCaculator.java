package chess;

import java.util.ArrayList;
import java.util.Collection;

public class BishopMovesCaculator implements PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        int[][] spots = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};
        Collection<ChessMove> movePossibilities = new ArrayList<>();
        for(int [] ints : spots) {
            int positionRow = startPosition.getRow();
            int positionColumn = startPosition.getColumn();
            while (true) {
                positionRow += ints[0];
                positionColumn += ints[1];
                if (positionRow > 8 || positionColumn > 8 || positionRow < 1 || positionColumn < 1) {
                    break;
                }
                ChessPosition currentPosition = new ChessPosition(positionRow, positionColumn);
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
