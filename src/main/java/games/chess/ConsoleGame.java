package games.chess;

import games.chess.model.*;
import games.chess.model.piece.Piece;

import java.io.IOException;
import java.util.Scanner;

public class ConsoleGame {
    
    private final Game game;
    private Move[] legalMoves;
    private final Scanner inputScanner;
    
    
    public static void main(String[] args) throws IOException, InvalidFENFileException, InterruptedException {
        ConsoleGame consoleGame = new ConsoleGame();
        consoleGame.run();
//        Parser parser = new Parser();
//        String resourcesDir = "src/main/resources/chess/positions/";
//        Game initial = new Game();
//        Game scandinavian = parser.parseFromFENFile(resourcesDir + "scandinavian.fen");
//        printGame(initial);
//        Thread.sleep(5000);
//        for (Move move : getScholarsMateMoveSequence(initial)) {
//            initial.makeMove(move);
//            printGame(initial);
//            Thread.sleep(5000);
//        }
        
//        String input = "";
//        while (!input.equals("exit")) {
//            System.out.println("Type a command:");
//            input = new String(System.in.readAllBytes());
//        }
        
    }
    
    public ConsoleGame() {
        game = new Game();
        inputScanner = new Scanner(System.in);
    }
    
    public void run() {
        do {
            legalMoves = game.getLegalMoves();
            printGame();
            String input = inputScanner.nextLine().trim();
//            String input = new String(System.console().r0eadPassword());
            int moveChoice = Integer.parseInt(input);
            Move selected = legalMoves[moveChoice];
            game.makeMove(selected);
        } while (legalMoves.length > 0);
    }
    
    private void printGame() {
        StringBuilder builder = new StringBuilder();
        appendBoardToStringBuilder(builder);
        appendMoveListToStringBuilder(builder);
        System.out.print(builder.append("\r"));
    }    
    
    private void appendBoardToStringBuilder(StringBuilder builder) {
        String horizontalLine = String.format("%-150s", "+---+---+---+---+---+---+---+---+");
        builder.append(" ").append(horizontalLine);
        for (int rankIndex = 7; rankIndex >= 0; rankIndex--) {
            StringBuilder line = new StringBuilder();
            line.append("| ");
            for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
                Piece piece = game.getPieceAt(new Square(fileIndex, rankIndex));
                if (piece == null) {
                    line.append(" ");
                } else {
                    line.append(piece.getFENAbbrevName());
                }
                line.append(" | ");
            }
            builder.append(String.format("%-150s", line.toString()));
            builder.append(horizontalLine);
        }
    }
    
    private void appendMoveListToStringBuilder(StringBuilder builder) {
        String toMove = game.getActivePlayer().isWhite() ? "White to move:" : "Black to move:";
        builder.append(String.format("%-150s", toMove));
        for (int i = 0; i < legalMoves.length; i++) {
            builder.append(String.format("%2d. ", i));
            builder.append(String.format("%-5s ", legalMoves[i].getCanonicalName()));
        }
    }
    
    public static Move[] getScholarsMateMoveSequence(Game game) {
        return new Move[] {
                new Move(game.getPieceAt(new Square("e2")), new Square("e4")),
                new Move(game.getPieceAt(new Square("e7")), new Square("e5")),
                new Move(game.getPieceAt(new Square("f1")), new Square("c4")),
                new Move(game.getPieceAt(new Square("b8")), new Square("c6")),
                new Move(game.getPieceAt(new Square("d1")), new Square("h5")),
                new Move(game.getPieceAt(new Square("g8")), new Square("f6")),
//                new Move(game.getPieceAt(new Square("d1")), new S8quare("f7"), true),
        };
    }

}