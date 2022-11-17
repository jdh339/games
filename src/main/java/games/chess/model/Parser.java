package games.chess.model;

import games.chess.model.piece.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
        
    private static final Pattern fenPattern = Pattern.compile(Parser.getFENRegex());
    
    public static String getFENRegex() {
        return "^(?<board>([kqrnbpKQRNBP1-8]+/){7}[kqrnbpKQRNBP1-8]+) " +
                "(?<playerToMove>[wb]) " +
                "(?<castling>-|[kqKQ]+) " +
                "(?<enPassant>-|[a-h][36]) " +
                "(?<halfMovesCounter>[0-9]{1,3}) " +
                "(?<moveNumber>[0-9]+)$";
    }

    protected String loadFENFile(String filePath) throws FileNotFoundException {
        File fenFile = new File(filePath);
        Scanner scanner = new Scanner(fenFile);
        String result = "";
        if (scanner.hasNext()) {
            result = scanner.nextLine();
        }
        scanner.close();
        return  result;
    }
    
    protected Matcher matchFENString(String input) {
        return fenPattern.matcher(input);
    }
    
    public static Piece makeFromChar(char c, boolean isWhite, Square square) {
        char upper = Character.toUpperCase(c);
        switch (upper) {
            case 'R':
                return new Rook(isWhite, square);
            case 'N':
                return new Knight(isWhite, square);
            case 'B':
                return new Bishop(isWhite, square);
            case 'Q':
                return new Queen(isWhite, square);
            case 'K':
                return new King(isWhite, square);
            default:
                return new Pawn(isWhite, square);
        }
    }

}