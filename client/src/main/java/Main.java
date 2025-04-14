import chess.*;
import ui.REPL;

public class Main {
    private static REPL repl ;

    public static void main(String[] args) throws Exception {
        repl = new REPL() ;
        try {
            var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            System.out.println("♕ 240 Chess Client: " + piece);

            // can I remove the line above?

            var serverUrl = "http://localhost:8080";
            if (args.length == 1) {
                serverUrl = args[0];
            }

            repl.run();
        }
        catch (Exception ex){
            System.out.print("\n");
            repl.run() ;
        }
    }
}