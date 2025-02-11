package chess;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessGame.TeamColor team;
    private ChessBoard current_board;



    public ChessGame() {
        this.team = TeamColor.WHITE;
        this.current_board = new ChessBoard();
        current_board.resetBoard();
    }


    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }


    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "team=" + team +
                ", current_board=" + current_board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(current_board, chessGame.current_board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, current_board);
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
        if (current_board.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = current_board.getPiece(startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : piece.pieceMoves(current_board, startPosition)) {
            ChessBoard hypotheticalBoard = new ChessBoard(current_board);
            hypotheticalBoard.executeMove(move);
            if (isInCheck(current_board.getPiece(startPosition).getTeamColor(), hypotheticalBoard) == false) {
                validMoves.add(move);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING && kingIsInCheck(piece.getTeamColor()) == false) {
                validMoves.addAll(Castle(piece.getTeamColor()));
            }
        }
        return validMoves;
    }


    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if (validMoves(move.getStartPosition()) == null) {
            throw new InvalidMoveException("Invalid Move");
        }
        ChessPiece piece = current_board.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid Move");
        }
        if (validMoves(move.getStartPosition()).contains(move)) {
            if(current_board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING){
                current_board.getPiece(move.getStartPosition()).setKingPreviousMove();
                if(move.getStartPosition().getColumn() - move.getEndPosition().getColumn() == 2 || move.getStartPosition().getColumn() - move.getEndPosition().getColumn() == -2 ){
                    ChessMove rookMove ;
                    if(move.getEndPosition().getColumn() == 7)
                        rookMove = new ChessMove(new ChessPosition(move.getStartPosition().getRow(), 8), new ChessPosition(move.getEndPosition().getRow(), 6), null);
                    else{
                            rookMove = new ChessMove(new ChessPosition(move.getStartPosition().getRow(), 1), new ChessPosition(move.getEndPosition().getRow(), 4), null);
                        }
                    current_board.getPiece(rookMove.getStartPosition()).setRook();
                    current_board.executeMove(rookMove);
                }
            }
            if(current_board.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK){
                current_board.getPiece(move.getStartPosition()).setRook();
            }
            current_board.executeMove(move);
            if (team == TeamColor.BLACK)
                setTeamTurn(TeamColor.WHITE);
            else {
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
    public boolean isInCheck(TeamColor teamColor, ChessBoard hypotheticalBoard) {
        ChessPosition kingPosition = hypotheticalBoard.getKingPosition(teamColor);
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = hypotheticalBoard.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() != teamColor) {
                    for (ChessMove move : piece.pieceMoves(hypotheticalBoard, myPosition)) {
                        if (Objects.equals(move.getEndPosition(), kingPosition)) {
                            return true; // double check logic
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, current_board);
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    // fix this logic
    public boolean isInCheckmate(TeamColor teamColor) {
        Collection<ChessMove> endMoves = new ArrayList<>();
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = current_board.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    for (ChessMove move : piece.pieceMoves(current_board, myPosition)) {
                        ChessBoard hypotheticalBoard = new ChessBoard(current_board);
                        hypotheticalBoard.executeMove(move);

                        if (isInCheck(piece.getTeamColor(), hypotheticalBoard) == false) {
                            endMoves.add(move);
                        }
                    }
                }
            }
        }
        return endMoves.isEmpty();
    }

//    public boolean isInCheckmate(TeamColor teamColor) {
//        return isInCheckmate(teamColor, current_board) ;
//    }
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
    public boolean isInStalemate(TeamColor teamColor) {
        Collection<ChessMove> endMoves = new ArrayList<>();
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = current_board.getPiece(myPosition);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    for (ChessMove move : piece.pieceMoves(current_board, myPosition)) {
                        ChessBoard hypotheticalBoard = new ChessBoard(current_board);
                        hypotheticalBoard.executeMove(move);
                        if (isInCheck(piece.getTeamColor(), hypotheticalBoard) == false) {
                            endMoves.add(move);
                        }
                    }
                }
            }
        }
        return endMoves.isEmpty() && !isInCheck(teamColor, current_board);
    }


//    public boolean isInStalemate(TeamColor teamColor) {
//        return isInStalemate(teamColor, current_board) ;
//    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.current_board = board;
        // this.team = TeamColor.WHITE  maybe have to set board
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = current_board.getPiece(myPosition);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    current_board.updateKingPosition(piece.getTeamColor(), myPosition);
                }
            }
        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return current_board;
    }

    public Collection<ChessMove> Castle(TeamColor teamColor) {
        Collection<ChessMove> possibilities = new ArrayList<>();
        ChessPosition kingPosition = current_board.getKingPosition(teamColor) ;
        if (teamColor == TeamColor.BLACK) {
            if (checkRookSpot(8, 8) == true && checkingTwoSpaces(new ChessPosition(8, 7), new ChessPosition(8, 6))) {
                if (current_board.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 8, 7));
                }
            }
            if (checkRookSpot(8, 1) == true && checkingThreeSpaces(new ChessPosition(8, 2), new ChessPosition(8, 3), new ChessPosition(8, 4))) {
                if (current_board.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 8, 3));
                }
            }
            return possibilities;
        } else {
            if (checkRookSpot(1, 1) == true && checkingThreeSpaces(new ChessPosition(1, 2), new ChessPosition(1, 3), new ChessPosition(1, 4))) {
                if (current_board.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 1, 3));
                }
            }
            if (checkRookSpot(1, 8) == true && checkingTwoSpaces(new ChessPosition(1, 7), new ChessPosition(1, 6))) {
                if (current_board.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 1, 7));
                }
            }
            return possibilities ;

        }
    }

    public boolean checkMoveLoop(TeamColor teamColor, int row, int col) {
        ChessPosition startPosition = current_board.getKingPosition(teamColor);
        ChessPosition myPosition = new ChessPosition(row, col);
        ChessPiece piece = current_board.getPiece(startPosition);
        if (piece != null && piece.getTeamColor() == teamColor) {
            ChessMove kingMove = new ChessMove(startPosition, myPosition, null) ;
            ChessBoard hypotheticalBoard = new ChessBoard(current_board);
            hypotheticalBoard.executeMove(kingMove);
            if (isInCheck(teamColor, hypotheticalBoard) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean checkRookSpot(int row, int col) {
        ChessPosition checkingPosition = new ChessPosition(row, col);
        ChessPiece piece = current_board.getPiece(checkingPosition) ;
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.ROOK && current_board.getPiece(checkingPosition).getRook() == false) {
            return true;
        }
        return false;
    }
    public Collection<ChessMove> castleMove(TeamColor teamColor, int kingRow, int kingCol){
        Collection<ChessMove> possibilities = new ArrayList<>();
        ChessPosition kingEnd = new ChessPosition(kingRow, kingCol);
        ChessMove kingMove = new ChessMove(current_board.getKingPosition(teamColor), kingEnd, null);
        possibilities.add(kingMove) ;
        return possibilities ;
    }

    public boolean checkingTwoSpaces(ChessPosition onePosition, ChessPosition twoPosition){
        return current_board.getPiece(onePosition) == null && current_board.getPiece(twoPosition) == null;
    }
    public boolean checkingThreeSpaces(ChessPosition onePosition, ChessPosition twoPosition, ChessPosition threePosition){
        return current_board.getPiece(onePosition) == null && current_board.getPiece(twoPosition) == null && current_board.getPiece(threePosition) == null;
    }
    public boolean kingIsInCheck(TeamColor teamColor) {
        int row = current_board.getKingPosition(teamColor).getRow();
        int col = current_board.getKingPosition(teamColor).getColumn();
        int[][] KingOne = {{row, col + 1}, {row, col + 2}};
        int[][] KingTwo = {{row, col - 1}, {row, col - 2}};
        for (int i = 0; i < KingOne.length; i++) {
            if(KingOne[i][0] < 1 || KingOne[i][1] < 1){
                continue ;
            }
            if (checkMoveLoop(teamColor, KingOne[i][0], KingOne[i][1]))
                return true;
        }
        for (int i = 0; i < KingTwo.length; i++) {
            if(KingTwo[i][0] < 1 || KingTwo[i][1] < 1){
                continue ;
            }
            if (checkMoveLoop(teamColor, KingTwo[i][0], KingTwo[i][1]))
                return true;
        }
        return false ;
    }
}


