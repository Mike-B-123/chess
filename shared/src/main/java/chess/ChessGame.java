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
    private ChessMove new_move ;
    public ChessGame() {

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
        Collection<ChessMove> validMoves ;
        for(ChessMove move : piece.pieceMoves(current_board, startPosition)){
            //if(move.) I want to basically check if this move causes check
        }
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // do I need to do an "if" statment that checks for null?
        this.new_move = move ;
        if
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        int row = 0 ;
        while(row < 9){
            int col = 1 ;
            row ++ ;
            while(col < 9){
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = current_board.getPiece(myPosition) ;
                if(piece.getTeamColor() == teamColor){
                    col ++ ;
                    continue ;
                }
                for(ChessMove move : piece.pieceMoves(current_board, myPosition)){
                   // if(move.getEndPosition() == king's position){
                        // return true ;
                    // do I need to create a king position variable?
                }
                col ++ ;
        }
        }
        return false ;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
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
