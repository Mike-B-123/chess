package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    // I used the below implementation from the class instruction video, should I be doing it in a different way to be academically honest?
    private ChessPiece[][] squares ;
    private ChessPosition whiteKingPosition ;
    private ChessPosition blackKingPosition ;

    public ChessBoard() {
        squares = new ChessPiece[8][8];
    }


    public ChessBoard(ChessBoard newBoard){
        this.squares = Arrays.copyOf(newBoard.squares, 8) ;
        for(int i = 0; i < newBoard.squares.length; i++){
            squares[i] = Arrays.copyOf(newBoard.squares[i], 8) ;
        }
        whiteKingPosition =  newBoard.whiteKingPosition ;
        blackKingPosition = newBoard.blackKingPosition ;
    }


    public void updateKingPosition(ChessGame.TeamColor teamColor, ChessPosition endPosition){
        if(teamColor == teamColor.BLACK){
            blackKingPosition = endPosition ;
        }
        else {
            whiteKingPosition = endPosition ;
        }
    }



    public void executeMove(ChessMove move){
        ChessGame.TeamColor color = getPiece(move.getStartPosition()).getTeamColor() ;
        if(getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.KING) {
            updateKingPosition(color, move.getEndPosition());
        }
        if(getPiece(move.getStartPosition()).getPieceType() == ChessPiece.PieceType.PAWN){
            ChessPiece pawn = new ChessPiece(color, move.getPromotionPiece()) ;
            if(pawn.getPieceType() == null){
                pawn = new ChessPiece(color, ChessPiece.PieceType.PAWN) ;
            }
            addPiece(move.getEndPosition(), pawn);
            addPiece(move.getStartPosition(), null);
        }
        else{
            addPiece(move.getEndPosition(), getPiece(move.getStartPosition()));
            addPiece(move.getStartPosition(), null);
        }
    }

    public ChessPosition getKingPosition(ChessGame.TeamColor teamColor){
        if(teamColor == teamColor.BLACK){
            return blackKingPosition ;
        }
        else {
            return whiteKingPosition ;
        }
    }

    /** // private CheesePiece[][] squares = new ChessPiece [8][8];
     * //private Chess Piece [][] squares = new ChessPiece [8][8];
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {

        squares[position.getRow() - 1][position.getColumn() - 1] = piece ;
    }


    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1] ;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares[0][0] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK) ;
        squares[0][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK) ;
        squares[0][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT) ;
        squares[0][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT) ;
        squares[0][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP) ;
        squares[0][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP) ;
        squares[0][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING) ;
        squares[0][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN) ;
        int count = 0 ;
        while(count != 8){
            squares[1][count] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN) ;
            count ++ ;
        }
        squares[7][0] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK) ;
        squares[7][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK) ;
        squares[7][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT) ;
        squares[7][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT) ;
        squares[7][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP) ;
        squares[7][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP) ;
        squares[7][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING) ;
        squares[7][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN) ;

        count = 0 ;
        while(count != 8){
            squares[6][count] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN) ;
            count ++ ;
        }

        whiteKingPosition = new ChessPosition(1, 5) ;
        blackKingPosition = new ChessPosition(8, 5) ;


    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                ", whiteKingPosition=" + whiteKingPosition +
                ", blackKingPosition=" + blackKingPosition +
                '}';
    }


    // This is where we establish here each pice should start like in a normal game of chess
    //
}
