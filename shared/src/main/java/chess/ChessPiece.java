package chess;
// learn.cs240.click
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private PieceType type ;
    private final ChessGame.TeamColor pieceColor ;
    private boolean rookPreviousMove = false ;
    private boolean kingPreviousMove = false;// This is correct just not used yet

    // call the constructor with new Piece (WHITE, King)
// Instructor recommends creating a piece moves caculator class that then has sub classes for moves instead of storing those in the ChessPiece class
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type ;
        this.pieceColor = pieceColor ;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", pieceColor=" + pieceColor +
                '}';
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor(){
        return pieceColor ;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    public void setRook() {
        rookPreviousMove = true ;
    }
    public boolean getRook(){
        return rookPreviousMove ;
    }
    public void setKingPreviousMove() {
        kingPreviousMove = true ;
    }
    public boolean getKingPreviousMove(){
        return kingPreviousMove ;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> movePossibilities = new ArrayList<> () ;
        if(type == PieceType.KING) {
           movePossibilities = new KingMovesCaculator().calculateMoves(board, myPosition) ;
        }
        else if (type == PieceType.QUEEN) {
             movePossibilities = new QueenMovesCaculator().calculateMoves(board, myPosition) ;
        }
        else if (type == PieceType.BISHOP) {
            movePossibilities = new BishopMovesCaculator().calculateMoves(board, myPosition) ;
        }
        else if (type == PieceType.KNIGHT) {
            movePossibilities = new KnightMovesCaculator().calculateMoves(board, myPosition) ;
        }
        else if (type == PieceType.ROOK) {
            movePossibilities = new RookMovesCaculator().calculateMoves(board, myPosition) ;
        }
        else if (type == PieceType.PAWN) {
            movePossibilities = new PawnMovesCaculator().calculateMoves(board, myPosition) ;
        }


return movePossibilities ;
        // do we check the board at the given position for the type or is the type given?

        // this allows pieces to move and need to implement this
    }

}
// variables should be private and if needed in other files create a getter that returns the variable

// Note: video mentioned a two string mentioned on the chess move class. What is this?
// Two string is useful for debug. so you know did and return and should return.
// only worry about start position and returning possible move areas. NOt if a move is valid.