package model;
import chess.ChessGame;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
// put black and white username as null when game is cretaed.
// then chcek if either player is null to see if you can join.