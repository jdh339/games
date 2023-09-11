package games.ai;

import games.common.InputPlayer;
import games.common.RegularGame;
import games.common.RegularMove;

public class SimpleTreeSearchAI implements InputPlayer {

    private SimpleTreeNode root;
    private int maxDepth = 4;

    public SimpleTreeSearchAI(RegularGame game) {
        root = new SimpleTreeNode(game);
    }


    @Override
    public String getDescription() {
        return "Basic tree search AI";
    }

    @Override
    public RegularMove selectMove(RegularGame game) {
        profileTreeSearch();
        RegularMove move = root.getBestMove();
        root = root.children.get(move);
        return move;
    }

    @Override
    public void notifyOpponentMoved(RegularMove move) {
        root = root.children.get(move);
    }

    private void profileTreeSearch() {
        int treeSize = 1;
        long searchStart = System.currentTimeMillis();
        for (int d = 0; d <= maxDepth; d++) {
            long start = System.currentTimeMillis();
            treeSize = 1 + root.buildDescendants(d);
            long duration = System.currentTimeMillis() - start;
            System.out.println("Reached depth: " + d +
                    " tree size: " + treeSize + " games " +
                    " elapsed: " + duration + "ms");
        }
        long searchDuration = System.currentTimeMillis() - searchStart;
        System.out.println("Total duration: " + searchDuration + "ms");
    }
}
