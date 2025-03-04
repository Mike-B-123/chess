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
                ChessPosition myCurrentPosition = new ChessPosition(positionRow, positionColumn);
                if (board.getPiece(myCurrentPosition) != null) {
                    if(board.getPiece(myCurrentPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()){
                        ChessMove oneSingleMove = new ChessMove(startPosition, myCurrentPosition, null);
                        movePossibilities.add(oneSingleMove);}
                    break;
                }

                ChessMove oneSingleMove = new ChessMove(startPosition, myCurrentPosition, null);
                movePossibilities.add(oneSingleMove);
            }
        }
        return movePossibilities ;
    }
}
