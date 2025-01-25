package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCaculator implements PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> capturePossibilities = new ArrayList<>();
        Collection<ChessMove> forwardPossibilities = new ArrayList<>();
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        int[][] captures = {{row + 1, col + 1}, {row + 1, col - 1}};
        int[][] forwards = {{row + 1, col}, {row + 2, col}};
        for (int[] capture : captures) {
            ChessPosition current_position = new ChessPosition(capture[0], capture[1]);
            if (capture[0] > 8 || capture[1] > 8 || capture[0] < 1 || capture[0] < 1) {
                continue;
            }
            if (board.getPiece(current_position) != null) {
                if (board.getPiece(current_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove SingleMove = new ChessMove(startPosition, current_position, null);
                    capturePossibilities.add(SingleMove);
                }
            }
        }
        for (int[] forward : forwards) {
            ChessPosition current_position = new ChessPosition(forward[0], forward[1]);
            ChessPosition backone_position = new ChessPosition(forward[0] - 1, forward[1]);
            if (forward[0] > 8 || forward[1] > 8 || forward[0] < 1 || forward[0] < 1) {
                continue;
            }
            if (board.getPiece(current_position) == null) {
                if (startPosition.getRow() == 2 && board.getPiece(backone_position) == null) {
                    ChessMove SingleMove = new ChessMove(startPosition, current_position, null);
                    forwardPossibilities.add(SingleMove);
                }

            }
        }
        return null ;
    }
    private class promotionHelper(Collection<ChessMove> possibilities){
        for (ChessMove move : possibilities) {
            if(move.get)
        }
    }
}
