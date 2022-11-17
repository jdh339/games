package games.chess.model;

public class InvalidFENFileException extends Exception {
    
    public InvalidFENFileException(String message) {
        super(message);
    }
}