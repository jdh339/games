package games.connect4;

import games.common.InputPlayer;
import games.ai.RandomMoverAI;

import java.util.Scanner;

public class ConsoleGame extends games.common.ConsoleGame {

    public static void main(String[] args) {
        System.out.println("Starting a new game of Connect 4!");

        int selected = getPlayers();
        if (selected == 1) {
            onePlayerGame(new RandomMoverAI());
        } else {
            twoPlayerGame();
        }


        Game game = new Game();
        String result = game.getResult();
        while (result == null) {
            printGame(game);
            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                quit();
                return;
            }

            int entered = Integer.parseInt(input);
            if (entered < 1 || entered > 7) {
                System.out.println("Invalid move entered. Type a number between 1 and 7.");
                continue;
            }

            Move[] legalMoves = game.getLegalMoves();
            Move selected = null;
            for (Move legalMove: legalMoves) {
                if (legalMove.column() == entered - 1) {
                    selected = legalMove;
                    break;
                }
            }
            if (selected == null) {
                System.out.println("Cannot play in column " + input + " because it is full.");
                System.out.println("Enter a different number between 1 and 7.");
                continue;
            }

            game.makeMove(selected);
            result = game.getResult();
        }

        System.out.println(result + " wins!!");
        System.out.println("Play again? [y/n]");
        String input = scanner.nextLine().trim();
        if (input.equalsIgnoreCase("y")) {
            main(args);
        } else {
            quit();
        }
    }

    private static void quit() {
        System.out.println("Bye!");
    }

    private static int getPlayers() {
        String builder = """
                Select who you would like to play:
                  0. No AI (a Player vs. Player game)
                  1. Random AI vs Player
                """;
        System.out.println(builder);
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static void printGame(Game game) {
        StringBuilder builder = new StringBuilder();
        for (int rowId = Game.COL_HEIGHT - 1; rowId >= 0; rowId--) {
            builder.append("|");
            for (int colId = 0; colId < Game.NUM_COLS; colId++) {
                builder.append(game.getCharAt(colId, rowId));
                builder.append("|");
            }
            builder.append("\n");
        }
        builder.append("|1|2|3|4|5|6|7|\n");
        builder.append(game.getPlayerToMove());
        builder.append(" to move. Type [1-7] to select a column or 'q' to exit.\n> ");
        System.out.print(builder);
    }


    private static void runGame(InputPlayer player1, InputPlayer player2) {

    }

    private static void twoPlayerGame() {

    }
}
