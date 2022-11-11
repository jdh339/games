package games.sudoku;

public class Solver {
    
    private int attempts;
    private long startTimestampMillis;
    private long endTimestampMillis;
    
    public Solver() {
        attempts = 0;
        startTimestampMillis = 0;
        endTimestampMillis = 0;
    }
    
    public int getAttempts() {
        return attempts;
    }
    
    public long getTimeToSolve() {
        return endTimestampMillis - startTimestampMillis;
    }
    
    public void printSolveStats() {
        System.out.printf(
            "Solved in %sms after %s brute force attempts.%n",
            getTimeToSolve(),
            getAttempts()
        );
    }
    
    public Sudoku solveRecursive(Sudoku sudoku) {
        attempts = 0;
        startTimestampMillis = System.currentTimeMillis();
        Sudoku solved = solveRecursiveInner(sudoku);
        endTimestampMillis = System.currentTimeMillis();
        return solved;
    }
    
    private Sudoku solveRecursiveInner(Sudoku sudoku) {
        if (!sudoku.isValid()) {
            return null;  // Can't solve this invalid puzzle.
        }
        
        if (sudoku.isComplete()) {
            return sudoku;  // Success!    
        }
        
        Position nextPosition = sudoku.getNextOpenPosition();
        for (int value = 1; value <= 9; value++) {
            attempts += 1;
            Sudoku nextAttempt = sudoku.cloneWithUpdate(nextPosition, value);
            Sudoku solved = solveRecursiveInner(nextAttempt);
            if (solved != null) {
                return solved;  // We found it! Return up the stack. 
            }
        }
        
        return null;  // Can't be solved, since all attempted values failed.
    }
}
