package chess;

import java.util.Collection;

public class QueenMovesCaculator implements PieceMovesCaculator{
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove>  OverallPossibilities = new BishopMovesCaculator().calculateMoves(board, startPosition) ;
        Collection<ChessMove> RooKPossibilities = new RookMovesCaculator().calculateMoves(board, startPosition) ;
        OverallPossibilities.addAll(RooKPossibilities) ;
        return OverallPossibilities ;
    }
}
