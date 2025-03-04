package chess;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List ;

public class KnightMovesCaculator implements PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> movePossibilities = new ArrayList<>();
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        int[][] numbers = {{row + 2, col + 1}, {row + 1, col + 2}, {row - 1, col + 2},
                {row-2, col+1}, {row-2, col-1}, {row-1,col-2}, {row+1,col-2}, {row+2,col-1}} ;
        for (int[] number : numbers){
            ChessPosition currentPosition = new ChessPosition(number[0], number[1]) ;
            if(currentPosition.getRow() > 8 || currentPosition.getColumn() > 8 ||
            currentPosition.getRow() < 1 || currentPosition.getColumn() < 1){
                continue ;
            }
            if(board.getPiece(currentPosition) != null
                    && board.getPiece(currentPosition).getTeamColor()
                    == board.getPiece(startPosition).getTeamColor()){
                continue ;
            }
            ChessMove singleMove = new ChessMove(startPosition, currentPosition, null) ;
            movePossibilities.add(singleMove) ;
        }
        return movePossibilities;
    }

}
