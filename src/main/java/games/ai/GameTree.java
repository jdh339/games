package games.ai;

public class GameTree {

    /**
     * Basic algorithm to search and find optimal moves for a Game.
     *
     * Given a Game, an Evaluator function capable of scoring, and a max depth,
     * we should construct a tree whose root is that game.
     *
     * For each level up to max depth, build a tree by calling game.getLegalMoves(),
     * and for each of the results create a new node whose game is the result of calling
     * game.cloneWithUpdate(move). Call the Evaluator on each game.
     *
     * and if the result
     * is a win,
     *
     * We can then bubble up
     *
     *
     *
     */

    GameTreeNode root;


}
