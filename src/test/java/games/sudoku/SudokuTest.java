package games.sudoku;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class SudokuTest {

    String puzzlesDirPath = "src/main/resources/sudoku/puzzles/";
    Sudoku emptyPuzzle;
    Sudoku puzzle1;
    Sudoku puzzle2;
    Sudoku puzzle3;
    Sudoku invalidPuzzle1;
    Sudoku solvedPuzzle1;
    Sudoku solvedPuzzle2;
    Sudoku solvedPuzzle3;

    @BeforeEach
    void setUp() {
        emptyPuzzle = new Sudoku();
        try {
            puzzle1 = new Sudoku(puzzlesDirPath + "puzzle1.txt");
            puzzle2 = new Sudoku(puzzlesDirPath + "puzzle2.txt");
            puzzle3 = new Sudoku(puzzlesDirPath + "puzzle3.txt");
            invalidPuzzle1 = new Sudoku(puzzlesDirPath + "invalid_puzzle1.txt");
            solvedPuzzle1 = new Sudoku(puzzlesDirPath + "solved_puzzle1.txt");
            solvedPuzzle2 = new Sudoku(puzzlesDirPath + "solved_puzzle2.txt");
            solvedPuzzle3 = new Sudoku(puzzlesDirPath + "solved_puzzle3.txt");
        } catch (FileNotFoundException e) {
            System.out.println("Working Directory = " + System.getProperty("user.dir"));
            e.printStackTrace();
            fail("Failed during test setup due to a puzzle file not found.");
        } catch (InvalidInputPuzzleException e) {
            e.printStackTrace();
            fail("Failed during test setup due to a InvalidInputPuzzleException.");
        }
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void constructSudokuFromFileThrowsFileNotFound() {
        assertThrows(FileNotFoundException.class, () -> new Sudoku("not_found.txt"));
    }
    
    @Test
    void constructSudokuFromFileThrowsInvalidInputPuzzleForTooManyRows() {
        String tooManyRowsFile = "src/main/resources/sudoku/puzzles/too_many_rows.txt";
        assertThrows(InvalidInputPuzzleException.class, () -> new Sudoku(tooManyRowsFile));
    }

    
    @Test
    void constructSudokuFromFileThrowsInvalidInputPuzzleForTooFewRows() {
        String tooFewRowsFile = "src/main/resources/sudoku/puzzles/too_few_rows.txt";
        assertThrows(InvalidInputPuzzleException.class, () -> new Sudoku(tooFewRowsFile));
    }

    @Test
    void readRowFromStringThrowsForTooShortRowLength() {
        String input = "1234";
        assertThrows(InvalidInputPuzzleException.class, () -> Sudoku.readRowFromString(input));
    }

    @Test
    void readRowFromStringThrowsForTooLongRowLength() {
        String input = "825746931999999";
        assertThrows(InvalidInputPuzzleException.class, () -> Sudoku.readRowFromString(input));
    }

    @Test
    void readRowFromStringWorksForCompleteRow() {
        String input = "825746931";
        int[] expected = {8, 2, 5, 7, 4, 6, 9, 3, 1};
        try {
            assertArrayEquals(expected, Sudoku.readRowFromString(input));
        } catch (InvalidInputPuzzleException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void readRowFromStringWorksForIncompleteRow() {
        String input = "8   46 3 ";
        int[] expected = {8, 0, 0, 0, 4, 6, 0, 3, 0};
        try {
            assertArrayEquals(expected, Sudoku.readRowFromString(input));
        } catch (InvalidInputPuzzleException e) {
            fail(e.getMessage());
        }
    }

    @Test
    void isCompleteReturnsFalseForEmptyPuzzle() {
        assertFalse(emptyPuzzle.isComplete());
    }

    @Test
    void isCompleteReturnsFalseForPartialPuzzles() {
        assertFalse(puzzle1.isComplete());
        assertFalse(puzzle2.isComplete());
        assertFalse(puzzle3.isComplete());
    }

    @Test
    void isCompleteReturnsTrueForSolvedPuzzles() {
        assertTrue(solvedPuzzle1.isComplete());
        assertTrue(solvedPuzzle2.isComplete());
        assertTrue(solvedPuzzle3.isComplete());
    }

    @Test
    void isCompleteReturnsTrueForInvalidSolvedPuzzle() {
        assertTrue(invalidPuzzle1.isComplete());
    }

    @Test
    void isValidReturnsTrueForEmptyPuzzle() {
        assertTrue(emptyPuzzle.isValid());
    }

    @Test
    void isValidReturnsTrueForValidIncompletePuzzles() {
        assertTrue(puzzle1.isValid());
        assertTrue(puzzle2.isValid());
        assertTrue(puzzle3.isValid());
    }

    @Test
    void isValidReturnsTrueForValidCompletePuzzles() {
        assertTrue(solvedPuzzle1.isValid());
        assertTrue(solvedPuzzle2.isValid());
        assertTrue(solvedPuzzle3.isValid());
    }

    @Test
    void isValidReturnsFalseForInvalidPuzzle() {
        assertFalse(invalidPuzzle1.isValid());
    }
    
    @Test
    void isValidRowReturnsTrueForEmptyRow() {
        assertTrue(emptyPuzzle.isValidRow(4));
    }

    @Test
    void isValidRowReturnsTrueForRowOfSolvedPuzzle() {
        assertTrue(solvedPuzzle1.isValidRow(6));
    }

    @Test
    void isValidRowReturnsTrueForValidRowOfInvalidPuzzle() {
        assertTrue(invalidPuzzle1.isValidRow(6));
    }

    @Test
    void isValidRowReturnsFalseForInvalidRowOfInvalidPuzzle() {
        assertFalse(invalidPuzzle1.isValidRow(7));
    }

    @Test
    void isValidColReturnsTrueForEmptyCol(){
        assertTrue(emptyPuzzle.isValidCol(4));
    }

    @Test
    void isValidColReturnsTrueForColOfSolvedPuzzle() {
        assertTrue(solvedPuzzle1.isValidCol(6));
    }

    @Test
    void isValidColReturnsTrueForValidColOfInvalidPuzzle() {
        assertTrue(invalidPuzzle1.isValidCol(6));
    }

    @Test
    void isValidColReturnsFalseForInvalidColOfInvalidPuzzle() {
        assertFalse(invalidPuzzle1.isValidCol(7));
    }

    @Test
    void isValidBoxReturnsTrueForEmptyBox(){
        assertTrue(emptyPuzzle.isValidBox(4));
    }

    @Test
    void isValidBoxReturnsTrueForBoxOfSolvedPuzzle() {
        assertTrue(solvedPuzzle1.isValidBox(6));
    }

    @Test
    void isValidBoxReturnsTrueForValidBoxOfInvalidPuzzle() {
        assertTrue(invalidPuzzle1.isValidBox(7));
    }

    @Test
    void isValidBoxReturnsFalseForInvalidBoxOfInvalidPuzzle() {
        assertFalse(invalidPuzzle1.isValidBox(8));
    }

    @Test
    void getNextOpenPositionReturnsNullIfNoOpenPositions() {
        assertNull(solvedPuzzle1.getNextOpenPosition());
    }

    @Test
    void getNextOpenPositionReturnsFirstOpenPosition() {
        Position expected = new Position(0, 0);
        assertEquals(expected, puzzle1.getNextOpenPosition());
    }
    
    @Test
    void cloneWithUpdate() {
        Position toChange = new Position(0, 0);
        Position expectedNextOpen = new Position(0, 2);
        Sudoku updatedPuzzle = puzzle1.cloneWithUpdate(toChange, 6);
        
        assertNotNull(updatedPuzzle);
        assertNotEquals(puzzle1, updatedPuzzle);
        assertEquals(expectedNextOpen, updatedPuzzle.getNextOpenPosition());
        assertTrue(puzzle1.isValid());
        assertTrue(updatedPuzzle.isValidRow(0));
        assertTrue(updatedPuzzle.isValidBox(0));
        assertFalse(updatedPuzzle.isValidCol(0));
    }
}