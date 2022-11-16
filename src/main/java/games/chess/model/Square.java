package games.chess.model;

import java.util.regex.Pattern;

/**
 * Represents a square on the board.
 */
public class Square {

    static Pattern pattern = Pattern.compile("^[a-h][1-8]$");

    private final int fileIndex;
    private final int rankIndex;

    public Square(int fileIndex, int rankIndex) {
        this.fileIndex = fileIndex;
        this.rankIndex = rankIndex;
    }

    public Square(String squareName) {
        this.fileIndex = Square.parseFileIndex(squareName);
        this.rankIndex = Square.parseRankIndex(squareName);
    }

    public static boolean canParse(String squareName) {
        return pattern.matcher(squareName).matches();
    }

    public static int parseFileIndex(String squareName) {
        if (!canParse(squareName)) {
            return -1;
        }
        return (int) squareName.charAt(0) - (int) 'a';
    }

    public static int parseRankIndex(String squareName) {
        if (!canParse(squareName)) {
            return -1;
        }
        return Character.getNumericValue(squareName.charAt(1)) - 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Square square = (Square) o;
        return fileIndex == square.fileIndex && rankIndex == square.rankIndex;
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public int getRankIndex() {
        return rankIndex;
    }

    public String getFileName() {
        return "" + (char) (fileIndex + (int) 'a');
    }

    public String getRankName() {
        return "" + (rankIndex + 1);
    }

    /**
     * @return The canonical Square representation (e.g. e4 or f7)
     */
    public String getName() {
        return getFileName() + getRankName();
    }

    /**
     * Since it is possible to create a Square with indexes off the board,
     * use this method to determine if the Square designates an actual board location.
     *
     * @return whether the fileIndex and rankIndex point to a square on the board.
     */
    public boolean isOnBoard() {
        return fileIndex >= 0 && fileIndex < 8 && rankIndex >= 0 && rankIndex < 8;
    }
}
