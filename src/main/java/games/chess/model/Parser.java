package games.chess.model;

import games.chess.model.piece.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    
    public Game parseFromFENFile(String filePath)
            throws FileNotFoundException, InvalidFENFileException {
        String fenString = loadFENFile(filePath);
        Matcher matcher = matchFENString(fenString);
        if (!matcher.matches()) {
            throw new InvalidFENFileException("File at " + filePath + " cannot be parsed");
        }

        // We use a TreeSet to insert the pieces so they are sorted at the end of parsing.
        // See the compare() method of PieceComparator to see how the pieces are sorted.
        TreeSet<Piece> whitePieces = new TreeSet<>(new PieceComparator());
        TreeSet<Piece> blackPieces = new TreeSet<>(new PieceComparator());

        String[] rows = matcher.group("board").split("/");
        
        for (int i = 0; i < rows.length; i++) {
            String row = rows[i];
            int rankIndex = 7 - i; // Rank 8 is the first in a FEN file.
            int fileIndex = 0;

            for (char c: row.toCharArray()) {
                if (Character.isDigit(c)) {
                    fileIndex += Character.getNumericValue(c);
                    continue;
                }
                
                boolean isWhite = Character.isUpperCase(c);
                Square square = new Square(rankIndex, fileIndex);
                Piece piece = makeFromChar(c, isWhite, square);
                if (isWhite) {
                    whitePieces.add(piece);
                } else {
                    blackPieces.add(piece);
                }
                fileIndex += 1;
            }
            if (fileIndex != 8) {
                throw new InvalidFENFileException("Row %s is not 8 squares");
            }
        }
        Player whitePlayer = new Player(true, whitePieces);
        Player blackPlayer = new Player(false, blackPieces);
        boolean whiteToMove = matcher.group("playerToMove").equals("w");
        Square enPassantSquare = new Square(matcher.group("enPassant"));
        return new Game(whiteToMove, whitePlayer, blackPlayer, enPassantSquare);
    }
    
    private static final Pattern fenPattern = Pattern.compile(Parser.getFENRegex());
    
    protected static String getFENRegex() {
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
    
    private Piece makeFromChar(char c, boolean isWhite, Square square) {
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