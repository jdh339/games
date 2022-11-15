package games.chess.model;

import java.util.regex.Pattern;

/**
 * Represents a square on the board.
 */
public class Square {
    
    static Pattern pattern = Pattern.compile("^[a-h][1-8]$");
    
    int fileIndex;
    int rankIndex;
    
    public Square(int fileIndex, int rankIndex) {
        this.fileIndex = fileIndex;
        this.rankIndex = rankIndex;
    }
    
    public Square(String squareName) {
        
    }
    
    public static boolean canParse(String squareName) {
        return pattern.matcher(squareName).matches();
    }
    
    public static int parseFileIndex(String squareName) {
        if (!canParse(squareName)) {
            return -1;
        }
        return (int)squareName.charAt(0) - (int)'a';
    }
    
    public static int parseRankIndex(String squareName) {
        if (!canParse(squareName)) {
            return -1;
        }
        return Character.getNumericValue(squareName.charAt(1)) - 1;
    }

    /**
     * @return The canonical Square representation (e.g. e4 or f7)
     */
    public String getName() {
        return "" + (char)(fileIndex + (int)'a') + (rankIndex + 1);
    }
}
