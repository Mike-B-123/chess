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
    private ChessBoard currentBoard;



    public ChessGame() {
        this.team = TeamColor.WHITE;
        this.currentBoard = new ChessBoard();
        currentBoard.resetBoard();
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
                ", currentBoard=" + currentBoard +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return team == chessGame.team && Objects.equals(currentBoard, chessGame.currentBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, currentBoard);
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
        if (currentBoard.getPiece(startPosition) == null) {
            return null;
        }
        ChessPiece piece = currentBoard.getPiece(startPosition);
        Collection<ChessMove> validMoves = new ArrayList<>();
        for (ChessMove move : piece.pieceMoves(currentBoard, startPosition)) {
            ChessBoard hypotheticalBoard = new ChessBoard(currentBoard);
            hypotheticalBoard.executeMove(move);
            if (isInCheck(currentBoard.getPiece(startPosition).getTeamColor(), hypotheticalBoard) == false) {
                validMoves.add(move);
            }
            if (piece.getPieceType() == ChessPiece.PieceType.KING && kingIsInCheck(piece.getTeamColor()) == false) {
                validMoves.addAll(castle(piece.getTeamColor()));
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
        ChessPiece piece = currentBoard.getPiece(move.getStartPosition());
        if (piece.getTeamColor() != getTeamTurn()) {
            throw new InvalidMoveException("Invalid Move");
        }
        if (validMoves(move.getStartPosition()).contains(move)) {
            if(currentBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING){
                currentBoard.getPiece(move.getStartPosition()).setKingPreviousMove();
                int startColumn = move.getStartPosition().getColumn() ;
                int endColumn = move.getEndPosition().getColumn() ;
                if(startColumn - endColumn == 2 || move.getStartPosition().getColumn() - move.getEndPosition().getColumn() == -2 ){
                    ChessMove rookMove ;
                    if(move.getEndPosition().getColumn() == 7) {
                        ChessPosition startingMove = new ChessPosition(move.getStartPosition().getRow(), 8) ;
                                rookMove = new ChessMove(startingMove, new ChessPosition(move.getEndPosition().getRow(), 6), null);
                    }
                        else{
                        ChessPosition startingMove = new ChessPosition(move.getStartPosition().getRow(), 1) ;
                            rookMove = new ChessMove(startingMove, new ChessPosition(move.getEndPosition().getRow(), 4), null);
                        }
                    currentBoard.getPiece(rookMove.getStartPosition()).setRook();
                    currentBoard.executeMove(rookMove);
                }
            }
            if(currentBoard.getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.ROOK){
                currentBoard.getPiece(move.getStartPosition()).setRook();
            }
            currentBoard.executeMove(move);
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
                if (piece == null || piece.getTeamColor() == teamColor) {
                    continue;
                }
                for (ChessMove move : piece.pieceMoves(hypotheticalBoard, myPosition)) {
                    if (Objects.equals(move.getEndPosition(), kingPosition)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, currentBoard);
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
                ChessPiece piece = currentBoard.getPiece(myPosition);
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue ;
                }
                for (ChessMove move : piece.pieceMoves(currentBoard, myPosition)) {
                        ChessBoard hypotheticalBoard = new ChessBoard(currentBoard);
                        hypotheticalBoard.executeMove(move);
                        if (isInCheck(piece.getTeamColor(), hypotheticalBoard) == false) {
                            endMoves.add(move);
                        }
                    }
            }
        }
        return endMoves.isEmpty();
    }


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
            for (int colomn = 1; colomn < 9; colomn++) {
                ChessPosition currentPosition = new ChessPosition(row, colomn);
                ChessPiece piece = currentBoard.getPiece(currentPosition);
                if (piece == null || piece.getTeamColor() != teamColor) {
                    continue ;
                }
                    for (ChessMove move : piece.pieceMoves(currentBoard, currentPosition)) {
                        ChessBoard tempBoard = new ChessBoard(currentBoard);
                        tempBoard.executeMove(move);
                        if (!isInCheck(piece.getTeamColor(), tempBoard)) {
                            endMoves.add(move);
                        }
                    }
            }
        }
        return endMoves.isEmpty() && !isInCheck(teamColor, currentBoard);
    }



    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.currentBoard = board;
        for (int row = 1; row < 9; row++) {
            for (int col = 1; col < 9; col++) {
                ChessPosition myPosition = new ChessPosition(row, col);
                ChessPiece piece = currentBoard.getPiece(myPosition);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    currentBoard.updateKingPosition(piece.getTeamColor(), myPosition);
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
        return currentBoard;
    }

    public Collection<ChessMove> castle(TeamColor teamColor) {
        Collection<ChessMove> possibilities = new ArrayList<>();
        ChessPosition kingPosition = currentBoard.getKingPosition(teamColor) ;
        if (teamColor == TeamColor.BLACK) {
            if (checkRookSpot(8, 8) == true && checkingTwoSpaces(new ChessPosition(8, 7), new ChessPosition(8, 6))) {
                if (currentBoard.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 8, 7));
                }
            }
            if (checkRookSpot(8, 1) == true && checkingThreeSpaces(new ChessPosition(8, 2), new ChessPosition(8, 3), new ChessPosition(8, 4))) {
                if (currentBoard.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 8, 3));
                }
            }
            return possibilities;
        } else {
            if (checkRookSpot(1, 1) == true && checkingThreeSpaces(new ChessPosition(1, 2), new ChessPosition(1, 3), new ChessPosition(1, 4))) {
                if (currentBoard.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 1, 3));
                }
            }
            if (checkRookSpot(1, 8) == true && checkingTwoSpaces(new ChessPosition(1, 7), new ChessPosition(1, 6))) {
                if (currentBoard.getPiece(kingPosition).getKingPreviousMove() == false) {
                    possibilities.addAll(castleMove(teamColor, 1, 7));
                }
            }
            return possibilities ;

        }
    }

    public boolean checkMoveLoop(TeamColor teamColor, int row, int col) {
        ChessPosition startPosition = currentBoard.getKingPosition(teamColor);
        ChessPosition myPosition = new ChessPosition(row, col);
        ChessPiece piece = currentBoard.getPiece(startPosition);
        if (piece != null && piece.getTeamColor() == teamColor) {
            ChessMove kingMove = new ChessMove(startPosition, myPosition, null) ;
            ChessBoard hypotheticalBoard = new ChessBoard(currentBoard);
            hypotheticalBoard.executeMove(kingMove);
            if (isInCheck(teamColor, hypotheticalBoard) == true) {
                return true;
            }
        }
        return false;
    }

    public boolean checkRookSpot(int row, int col) {
        ChessPosition checkingPosition = new ChessPosition(row, col);
        ChessPiece piece = currentBoard.getPiece(checkingPosition) ;
        if (piece != null && piece.getPieceType() == ChessPiece.PieceType.ROOK && currentBoard.getPiece(checkingPosition).getRook() == false) {
            return true;
        }
        return false;
    }
    public Collection<ChessMove> castleMove(TeamColor teamColor, int kingRow, int kingCol){
        Collection<ChessMove> possibilities = new ArrayList<>();
        ChessPosition kingEnd = new ChessPosition(kingRow, kingCol);
        ChessMove kingMove = new ChessMove(currentBoard.getKingPosition(teamColor), kingEnd, null);
        possibilities.add(kingMove) ;
        return possibilities ;
    }

    public boolean checkingTwoSpaces(ChessPosition onePosition, ChessPosition twoPosition){
        return currentBoard.getPiece(onePosition) == null && currentBoard.getPiece(twoPosition) == null;
    }
    public boolean checkingThreeSpaces(ChessPosition onePosition, ChessPosition twoPosition, ChessPosition threePosition){
        return currentBoard.getPiece(onePosition) == null && currentBoard.getPiece(twoPosition) == null && currentBoard.getPiece(threePosition) == null;
    }
    public boolean kingIsInCheck(TeamColor teamColor) {
        int row = currentBoard.getKingPosition(teamColor).getRow();
        int col = currentBoard.getKingPosition(teamColor).getColumn();
        int[][] kingOne = {{row, col + 1}, {row, col + 2}};
        int[][] kingTwo = {{row, col - 1}, {row, col - 2}};
        for (int i = 0; i < kingOne.length; i++) {
            if(kingOne[i][0] < 1 || kingOne[i][1] < 1){
                continue ;
            }
            if (checkMoveLoop(teamColor, kingOne[i][0], kingOne[i][1]))
                return true;
        }
        for (int i = 0; i < kingTwo.length; i++) {
            if(kingTwo[i][0] < 1 || kingTwo[i][1] < 1){
                continue ;
            }
            if (checkMoveLoop(teamColor, kingTwo[i][0], kingTwo[i][1]))
                return true;
        }
        return false ;
    }
}


