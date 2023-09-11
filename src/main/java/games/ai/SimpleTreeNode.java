package games.ai;

import games.common.RegularGame;
import games.common.RegularMove;

import java.util.HashMap;
import java.util.Map;

public class SimpleTreeNode {

    RegularGame game;
    HashMap<RegularMove, SimpleTreeNode> children = null;
    double score = 0.0;

    public SimpleTreeNode(RegularGame game) {
        this.game = game;
    }


    public RegularMove getBestMove() {
        RegularMove bestMove = null;
        double bestScore = Double.NEGATIVE_INFINITY;
        for (Map.Entry<RegularMove, SimpleTreeNode> entry: children.entrySet()) {
            if (entry.getValue().score > bestScore) {
                bestMove = entry.getKey();
                bestScore = entry.getValue().score;
            }
        }
        return bestMove;
    }
    
    /**
     *
     * @param depth the number of layers in the tree to generate after `this`.
     * @return the number of descendants in the subtree (not counting `this).
     */
    public int buildDescendants(int depth) {
        if (game == null || game.isFinished() || depth < 1) {
            return 0;
        }

        // The first time buildDescendants() is called on this node,
        // (indicated by `children` being null, we should populate
        // the direct descendants for this game. On later calls, we can
        // skip this step.
        if (children == null) {
            children = new HashMap<>();
            for (RegularMove move : game.getLegalMoves()) {
                SimpleTreeNode child = new SimpleTreeNode(game.cloneWithUpdate(move));
                children.put(move, child);
            }
        }

        int descendants = children.size();
        if (depth > 1) {
            for (SimpleTreeNode child : children.values()) {
                descendants += child.buildDescendants(depth - 1);
            }
        }
        return descendants;
    }
}
