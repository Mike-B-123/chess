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
        for (int i = 0; i < numbers.length; i += 1){
            ChessPosition current_position = new ChessPosition(numbers[i][0], numbers[i][1]) ;
            if(numbers[i][0] > 8 || numbers[i][1] > 8){
                continue ;
            }
            if(board.getPiece(current_position) != null
                    && board.getPiece(current_position).getTeamColor()
                    == board.getPiece(startPosition).getTeamColor()){
                continue ;
            }
            ChessMove SingleMove = new ChessMove(startPosition, current_position, null) ;
            movePossibilities.add(SingleMove) ;
        }
        return movePossibilities;
    }

}
