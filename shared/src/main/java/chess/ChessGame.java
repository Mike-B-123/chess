package chess;
import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor team ;
    private ChessBoard current_board ;

    public ChessGame() {
            this.team = TeamColor.WHITE ;
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team ;
    }



    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team ;
    }


    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = current_board.getPiece(startPosition) ;
        Collection<ChessMove> validMoves = new ArrayList<> () ;
        for(ChessMove move : piece.pieceMoves(current_board, startPosition)){
            ChessBoard hypotheticalBoard = new ChessBoard(current_board) ;
            hypotheticalBoard.makeMove(move);
            if(isInCheck(current_board.getPiece(startPosition).getTeamColor(), hypotheticalBoard) == false){
                validMoves.add(move) ;
            }
        }
        if(validMoves.isEmpty() == true){
            return null ;
        }
        return validMoves ;
    }



    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(validMoves(move.getStartPosition()) == null){
            throw new InvalidMoveException("Invalid Move");
        }
        ChessPiece piece = current_board.getPiece(move.getStartPosition()) ;
        if(piece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("Invalid Move");
        }
        if (validMoves(move.getStartPosition()).contains(move)) {
           current_board.makeMove(move);
            if(team == TeamColor.BLACK)
                setTeamTurn(TeamColor.WHITE);
            else{
                setTeamTurn(TeamColor.BLACK);
            }
        } else {
            throw new InvalidMoveException("Invalid Move"); // double check this is properly formatted
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor, ChessBoard hypthoeticalBoard) {
        ChessPosition kingPosition = hypthoeticalBoard.getKingPosition(teamColor) ;
        for(int row = 1; row < 9; row ++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = hypthoeticalBoard.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    for (ChessMove move : piece.pieceMoves(hypthoeticalBoard, myPosition)) {
                        if (move.getEndPosition() == kingPosition) {
                            return true; // double check logic
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor){
        return isInCheck(teamColor, current_board) ;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    // fix this logic
    public boolean isInCheckmate(TeamColor teamColor, ChessBoard hypthoeticalBoard) {
        hypthoeticalBoard.getKingPosition(teamColor) ;
        Collection<ChessMove> endMoves = new ArrayList<> () ;
        for(int row = 1; row < 9; row ++) {
            for(int col = 1; col < 9; col ++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = hypthoeticalBoard.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    if(validMoves(myPosition) != null)
                        endMoves.addAll(validMoves(myPosition));
                    }
                }
            }
        return endMoves.isEmpty();
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheckmate(teamColor, current_board) ;
    }
//         if(validMoves(kingPosition) == null && isInCheck(teamColor, current_board) == true){
//             return true ;
//         }
//            // should I be entering the king's postion or another piece's position
            // return false if valid moves contains anything?

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor, ChessBoard hypthoeticalBoard) {
        hypthoeticalBoard.getKingPosition(teamColor);
        Collection<ChessMove> endMoves = new ArrayList<> () ;
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = hypthoeticalBoard.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    if(validMoves(myPosition) != null){
                        endMoves.addAll(validMoves(myPosition));
                }
            }
                if (piece.getTeamColor() == teamColor) {
                    if(validMoves(myPosition) != null)
                        endMoves.addAll(validMoves(myPosition));
                }
            }
        }
        if(endMoves.isEmpty() && isInCheck(teamColor, hypthoeticalBoard) == false){
            return true ;
        }
        return false ;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        return isInStalemate(teamColor, current_board) ;
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.current_board = board ;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return current_board ;
    }
}
