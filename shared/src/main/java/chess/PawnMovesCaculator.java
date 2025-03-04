package chess;


import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCaculator implements PieceMovesCaculator {
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition startPosition) {
        Collection<ChessMove> capturePossibilities = new ArrayList<>();
        Collection<ChessMove> forwardPossibilities = new ArrayList<>();
        Collection<ChessMove> overallPossibilities = new ArrayList<>();
        int row = startPosition.getRow();
        int col = startPosition.getColumn();
        int color = 1 ;
        int start = 2 ;
        int end = 8 ;
        if(board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            color = -1;
            start = 7;
            end = 1;
        }
        int[][] captures = {{row + color*1, col + color*1}, {row + color*1, col - color*1}};
        for (int[] capture : captures) {
            ChessPosition currentPosition = new ChessPosition(capture[0], capture[1]);
            if (capture[0] > 8 || capture[1] > 8 || capture[0] < 1 || capture[1] < 1) {
                continue;
            }
            if (board.getPiece(currentPosition) != null) {
                if (board.getPiece(currentPosition).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove singleMove = new ChessMove(startPosition, currentPosition, null);
                    capturePossibilities.add(singleMove);
                }
            }
        }
        overallPossibilities.addAll(promotionHelper(capturePossibilities, end)) ;
        ChessPosition currentPosition = new ChessPosition(row + color*1, col);
        if(board.getPiece(currentPosition) == null
                && currentPosition.getRow() < 9 && currentPosition.getColumn() < 9 && currentPosition.getRow() > 0 && currentPosition.getColumn() > 0) {
            ChessMove singleMove = new ChessMove(startPosition, currentPosition, null);
            forwardPossibilities.add(singleMove) ;
            currentPosition = new ChessPosition(row + color*2, col);
            if(row == start && currentPosition.getRow() < 9 && currentPosition.getColumn() < 9 && currentPosition.getRow() > 0 && currentPosition.getColumn() > 0
            && board.getPiece(currentPosition) == null){
                singleMove = new ChessMove(startPosition, currentPosition, null);
                forwardPossibilities.add(singleMove) ;
            }
        }
        overallPossibilities.addAll(promotionHelper(forwardPossibilities, end)) ;
        return overallPossibilities ;
    }



    public Collection<ChessMove> promotionHelper(Collection<ChessMove> possibilities, int end) {
        Collection<ChessMove> helperPossibilities = new ArrayList<>();
        for (ChessMove move : possibilities) {
            if(move.getEndPosition().getRow() == end){
                ChessMove singleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN) ;
                helperPossibilities.add(singleMove) ;
                singleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK) ;
                helperPossibilities.add(singleMove) ;
                singleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT) ;
                helperPossibilities.add(singleMove) ;
                singleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP) ;
                helperPossibilities.add(singleMove) ;
            } else {
                helperPossibilities.add(move) ;
            }
        }
        return helperPossibilities ;
    }

}

