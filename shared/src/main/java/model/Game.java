package model;
import chess.ChessGame;

public record Game(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {
//    public Game setWhite(String newUsername){
//        return new Game(gameID, newUsername, blackUsername, gameName, game) ;
    public Game setBlack(String newUsername){
        return new Game(gameID, whiteUsername, newUsername, gameName, game) ;
    }
}
