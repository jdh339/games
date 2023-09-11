package games.ai;

import games.common.RegularGame;
import games.common.RegularMove;

import java.util.HashMap;

public class GameTreeNode {

    RegularGame game;
    GameTreeNode parent;
    private final HashMap<RegularMove, RegularGame> children;
    int score;

    public GameTreeNode(RegularGame game, GameTreeNode parent) {
        this.game = game;
        this.parent = parent;
        children = new HashMap<>();
        score = 0;
    }

    public void createChildren() {
        for (RegularMove move: game.getLegalMoves()) {
            RegularGame child = game.cloneWithUpdate(move);
            children.put(move, child);
        }
    }

}
