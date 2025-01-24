package chess;


import java.util.Collection;

public interface PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) ;
}
