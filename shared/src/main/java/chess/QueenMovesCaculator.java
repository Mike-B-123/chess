package chess;

import java.util.Collection;

public class QueenMovesCaculator implements PieceMovesCaculator{
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove>  overallPossibilities = new BishopMovesCaculator().calculateMoves(board, startPosition) ;
        Collection<ChessMove> rooKPossibilities = new RookMovesCaculator().calculateMoves(board, startPosition) ;
        overallPossibilities.addAll(rooKPossibilities) ;
        return overallPossibilities ;
    }
}
