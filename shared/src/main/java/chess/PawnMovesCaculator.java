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
            ChessPosition current_position = new ChessPosition(capture[0], capture[1]);
            if (capture[0] > 8 || capture[1] > 8 || capture[0] < 1 || capture[1] < 1) {
                continue;
            }
            if (board.getPiece(current_position) != null) {
                if (board.getPiece(current_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove singleMove = new ChessMove(startPosition, current_position, null);
                    capturePossibilities.add(singleMove);
                }
            }
        }
        overallPossibilities.addAll(promotionHelper(capturePossibilities, end)) ;
        ChessPosition currentPosition = new ChessPosition(row + color*1, col);
        if(board.getPiece(currentPosition) == null
                && currentPosition.getRow() < 9 && currentPosition.getColumn() < 9 && currentPosition.getRow() > 0 && currentPosition.getColumn() > 0) {
            ChessMove SingleMove = new ChessMove(startPosition, currentPosition, null);
            forwardPossibilities.add(SingleMove) ;
            currentPosition = new ChessPosition(row + color*2, col);
            if(row == start && currentPosition.getRow() < 9 && currentPosition.getColumn() < 9 && currentPosition.getRow() > 0 && currentPosition.getColumn() > 0
            && board.getPiece(currentPosition) == null){
                SingleMove = new ChessMove(startPosition, currentPosition, null);
                forwardPossibilities.add(SingleMove) ;
            }
        }
        overallPossibilities.addAll(promotionHelper(forwardPossibilities, end)) ;
        return overallPossibilities ;
    }



    public Collection<ChessMove> promotionHelper(Collection<ChessMove> possibilities, int end) {
        Collection<ChessMove> helperPossibilities = new ArrayList<>();
        for (ChessMove move : possibilities) {
            if(move.getEndPosition().getRow() == end){
                ChessMove SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN) ;
                helperPossibilities.add(SingleMove) ;
                SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK) ;
                helperPossibilities.add(SingleMove) ;
                SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT) ;
                helperPossibilities.add(SingleMove) ;
                SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP) ;
                helperPossibilities.add(SingleMove) ;
            } else {
                helperPossibilities.add(move) ;
            }
        }
        return helperPossibilities ;
    }

    // public Collection<ChessMove> EnPas(Collection<ChessMove> possinilities, int end){}
}

//public Collection<ChessMove> Enpas(Collection<ChessMove> possibilities, int end){
//
//}

// can use ternary for Blakc and white variable using ?
// then compare row to start
