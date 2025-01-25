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
        int Color = 1 ;
        if(board.getPiece(startPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            Color = -1;
        }
        int[][] captures = {{row + Color*1, col + Color*1}, {row + Color*1, col - Color*1}};
        for (int[] capture : captures) {
            ChessPosition current_position = new ChessPosition(capture[0], capture[1]);
            if (capture[0] > 8 || capture[1] > 8 || capture[0] < 1 || capture[1] < 1) {
                continue;
            }
            if (board.getPiece(current_position) != null) {
                if (board.getPiece(current_position).getTeamColor() != board.getPiece(startPosition).getTeamColor()) {
                    ChessMove SingleMove = new ChessMove(startPosition, current_position, null);
                    capturePossibilities.add(SingleMove);
                }
            }
        }
        overallPossibilities.addAll(promotionHelper(capturePossibilities)) ;
        ChessPosition current_position = new ChessPosition(row + Color*1, col);
        if(board.getPiece(current_position) == null
                && current_position.getRow() < 9 && current_position.getColumn() < 9 && current_position.getRow() > 0 && current_position.getColumn() > 0) {
            ChessMove SingleMove = new ChessMove(startPosition, current_position, null);
            forwardPossibilities.add(SingleMove) ;
            current_position = new ChessPosition(row + Color*2, col);
            if(row == 2 && board.getPiece(current_position) == null && current_position.getRow() < 9 && current_position.getColumn() < 9 && current_position.getRow() > 0 && current_position.getColumn() > 0){
                SingleMove = new ChessMove(startPosition, current_position, null);
                forwardPossibilities.add(SingleMove) ;
            }
        }
        overallPossibilities.addAll(promotionHelper(forwardPossibilities)) ;
        return overallPossibilities ;
        }
    public Collection<ChessMove> promotionHelper(Collection<ChessMove> possibilities) {
        Collection<ChessMove> helperPossibilities = new ArrayList<>();
        for (ChessMove move : possibilities) {
            if(move.getEndPosition().getRow() == 8){
                ChessMove SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.QUEEN) ;
                helperPossibilities.add(SingleMove) ;
                SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.ROOK) ;
                helperPossibilities.add(SingleMove) ;
                SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.KNIGHT) ;
                helperPossibilities.add(SingleMove) ;
                SingleMove = new ChessMove(move.getStartPosition(), move.getEndPosition(), ChessPiece.PieceType.BISHOP) ;
                helperPossibilities.add(SingleMove) ;
            } // fix for Black
        }
        return helperPossibilities ;
    }
}

// can use ternary for Blakc and white variable using ?
// then compare row to start
