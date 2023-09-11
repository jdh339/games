package games.common;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public abstract class ConsoleGame {

    RegularGame game = null;
    Scanner inputScanner = new Scanner(System.in);

    protected abstract RegularGame makeGame();

    protected abstract InputPlayer[] getPlayerTypesAvailable();

    protected void run() {

    }

    /**
     * Prompts the user for an Integer input and validates their choice.
     * @param min the minimum acceptable value.
     * @param max the maximum acceptable value (exclusive).
     * @return the integer that was provided.
     */
    protected int getIntegerInput(int min, int max) {
        String input = inputScanner.next();

        return 0;
    }

    protected static InputPlayer getPlayerType(boolean isFirst) {


        return null;
    }
}
