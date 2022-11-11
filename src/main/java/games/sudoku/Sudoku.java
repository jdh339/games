package games.sudoku;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Sudoku {

    int[][] grid;

    public Sudoku() {
        this.grid = new int[9][9];
    }

    public Sudoku(int[][] grid) {
        this.grid = grid;
    }

    /**
     * Creates a Sudoku by reading the given input file.
     * 
     * @param filePath Absolute or relative path to a txt file representing a puzzle.
     * @throws FileNotFoundException if the file does not exist.
     * @throws InvalidInputPuzzleException if the input file does not have 9 rows of 9 chars each.
     */
    public Sudoku(String filePath) throws FileNotFoundException, InvalidInputPuzzleException {
        this.grid = new int[9][9];
        
        File sudokuFile = new File(filePath);
        Scanner scanner = new Scanner(sudokuFile);
        int rowIndex = 0;
        while (scanner.hasNextLine()) {
            if (rowIndex == 9) {
                throw new InvalidInputPuzzleException("Input file has too many rows: " + filePath);
            }
            String row = scanner.nextLine();
            grid[rowIndex] = Sudoku.readRowFromString(row);
            rowIndex++;
        }
        if (rowIndex != 9) {
            throw new InvalidInputPuzzleException("Input file has too few rows: " + filePath);
        }
        
        scanner.close();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Sudoku sudoku = (Sudoku) o;
        
        if (grid.length != sudoku.grid.length) {
            return false;
        }
        for (int rowIndex = 0; rowIndex < grid.length; rowIndex++) {
            if (!Arrays.equals(grid[rowIndex], sudoku.grid[rowIndex])) {
                return  false;
            }
        } 
        return true;
    }

    public static int[] readRowFromString(String input) throws InvalidInputPuzzleException {
        if (input.length() != 9) {
            throw new InvalidInputPuzzleException("Input row is not 9 characters: " + input);
        }
        
        int[] row = new int[9];
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (Character.isDigit(c)) {
                row[i] = Character.getNumericValue(c);
            } else {
                row[i] = 0;
            }
        }
        return row;
    }

    /**
     * @return Whether the Puzzle has a digit in every place (regardless of whether it's correct).
     */
    public boolean isComplete() {
        for (int[] row : grid) {
            for (int value : row) {
                if (value == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isValid() {
        for (int i = 0; i < 9; i++) {
            if (!isValidRow(i)) {
                return false;
            }
            if (!isValidCol(i)) {
                return false;
            }
            if (!isValidBox(i)) {
                return false;
            }
        }
        return true;
    }

    public boolean isValidRow(int rowIndex) {
        return hasNoDuplicateDigits(grid[rowIndex]);
    }

    public boolean isValidCol(int colIndex) {
        int[] values = new int[9];
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            values[rowIndex] = grid[rowIndex][colIndex];
        }
        return hasNoDuplicateDigits(values);
    }

    public boolean isValidBox(int boxIndex) {
        int topRowIndex = (boxIndex / 3) * 3;
        int leftColIndex = (boxIndex % 3) * 3;
        int[] values = {
            grid[topRowIndex][leftColIndex],    
            grid[topRowIndex][leftColIndex + 1],    
            grid[topRowIndex][leftColIndex + 2],    
            grid[topRowIndex + 1][leftColIndex],    
            grid[topRowIndex + 1][leftColIndex + 1],    
            grid[topRowIndex + 1][leftColIndex + 2],    
            grid[topRowIndex + 2][leftColIndex],    
            grid[topRowIndex + 2][leftColIndex + 1],    
            grid[topRowIndex + 2][leftColIndex + 2],    
        };
        return hasNoDuplicateDigits(values);
    }

    public Position getNextOpenPosition() {
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            for (int colIndex = 0; colIndex < 9; colIndex++) {
                if (grid[rowIndex][colIndex] == 0) {
                    return new Position(rowIndex, colIndex);
                }
            }
        }
        return null;
    }

    public Sudoku cloneWithUpdate(Position position, int value) {
        int[][] newGrid = new int[9][];
        for (int rowIndex = 0; rowIndex < 9; rowIndex++) {
            newGrid[rowIndex] = grid[rowIndex].clone();
        }
        newGrid[position.rowIndex][position.colIndex] = value;
        return new Sudoku(newGrid);
    }

    private boolean hasNoDuplicateDigits(int[] values) {
        boolean[] seen = new boolean[10];
        for (int value : values) {
            if (value == 0) {
                continue;
            }
            
            if (seen[value]) {
                return false;
            }
            seen[value] = true;
        }
        return true;
    }
}
