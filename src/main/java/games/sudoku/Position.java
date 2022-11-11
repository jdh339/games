package games.sudoku;

public class Position {
    public int rowIndex;
    public int colIndex;

    public Position(int rowIndex, int colIndex) {
        this.rowIndex = rowIndex;
        this.colIndex = colIndex;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Position position = (Position) o;
        return rowIndex == position.rowIndex && colIndex == position.colIndex;
    }
}
