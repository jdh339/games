package games.sudoku;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    Solver solver;
    String puzzlesDirPath = "src/main/resources/sudoku/puzzles/";
    Sudoku puzzle1;
    Sudoku puzzle2;
    Sudoku puzzle3;
    Sudoku invalidPuzzle1;
    Sudoku solvedPuzzle1;
    Sudoku solvedPuzzle2;
    Sudoku solvedPuzzle3;
    
    @BeforeEach
    void setUp() {
        solver = new Solver();
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

    @Test
    void solveRecursiveReturnsNullForInvalidPuzzle() {
        assertNull(solver.solveRecursive(invalidPuzzle1));
    }

    @Test
    void solveRecursiveReturnsImmediatelyForSolvedPuzzle() {
        Sudoku result = solver.solveRecursive(solvedPuzzle1);
        assertEquals(0, solver.getAttempts());
        assertEquals(solvedPuzzle1, result);
    }
    
    @Test
    void solveRecursivePuzzle1() {
        System.out.println("Solving Puzzle 1...");
        Sudoku result = solver.solveRecursive(puzzle1);
        assertEquals(solvedPuzzle1, result);
        solver.printSolveStats();
    }
    
    @Test
    void solveRecursivePuzzle2() {
        System.out.println("Solving Puzzle 2...");
        Sudoku result = solver.solveRecursive(puzzle2);
        assertEquals(solvedPuzzle2, result);
        solver.printSolveStats();
    }
    
    @Test
    void solveRecursivePuzzle3() {
        System.out.println("Solving Puzzle 3...");
        Sudoku result = solver.solveRecursive(puzzle3);
        assertEquals(solvedPuzzle3, result);
        solver.printSolveStats();
    }
}