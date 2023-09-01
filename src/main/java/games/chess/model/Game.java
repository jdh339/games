package games.chess.model;

import games.chess.model.piece.Piece;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;

public class Game {

    private boolean whiteToMove;
    private final Piece[][] board;
    private final Player whitePlayer;
    private final Player blackPlayer;
    private final Deque<Move> moveHistory;
    private Square enPassantSquare;
    
    
    public Game() {
        whiteToMove = true;
        board = new Piece[8][8];
        whitePlayer = new Player(true);
        blackPlayer = new Player(false);
        moveHistory = new ArrayDeque<>();
        enPassantSquare = null;
        loadPiecePositionsToBoard(whitePlayer);
        loadPiecePositionsToBoard(blackPlayer);
    }


    /**
     * Creates a Game from existing Player objects.
     * @param whiteToMove whether it's white's turn.
     * @param white the white Player, with all its pieces.
     * @param black the black Player, with all its pieces.
     */
    public Game(boolean whiteToMove, Player white, Player black, Square enPassantSquare) {
        this.whiteToMove = whiteToMove;
        this.board = new Piece[8][8];
        this.whitePlayer = white;
        this.blackPlayer = black;
        this.moveHistory = new ArrayDeque<>();
        this.enPassantSquare = enPassantSquare;
        loadPiecePositionsToBoard(white);
        loadPiecePositionsToBoard(black);
    }

    /**
     * Creates a deep copy of the given game, including copying Players and pieces. 
     * @param toCopy a Game to copy, so we can modify the new one without changing the original. 
     */
    private Game(Game toCopy) {
        this.whiteToMove = toCopy.whiteToMove;
        this.board = new Piece[8][8];
        this.whitePlayer = new Player(toCopy.whitePlayer);
        this.blackPlayer = new Player(toCopy.blackPlayer);
        this.moveHistory = new ArrayDeque<>(toCopy.moveHistory);
        this.enPassantSquare = toCopy.enPassantSquare;
        loadPiecePositionsToBoard(whitePlayer);
        loadPiecePositionsToBoard(blackPlayer);
    }

    public Piece getPieceAt(Square square) {
        if (!square.isOnBoard()) {
            return null;
        }
        return board[square.getFileIndex()][square.getRankIndex()];
    }
    
    public Piece getPieceAt(String squareName) {
        return getPieceAt(new Square(squareName));
    }

    public Player getActivePlayer() {
        return whiteToMove ? whitePlayer : blackPlayer;
    }
    
    public Player getInactivePlayer() {
        return whiteToMove ? blackPlayer : whitePlayer; 
    }

    public Player getWhitePlayer() {
        return whitePlayer;
    }
    
    public Player getBlackPlayer() {
        return blackPlayer;
    }
    
    public Square getEnPassantSquare() {
        return enPassantSquare;
    }

    public Piece getEnPassantCapturablePiece() {
        if (enPassantSquare == null) {
            return null;
        }
        
        // The "en passant square" is the square behind a capturable pawn.
        // Therefore, it is always on either the 3rd or 6th rank and the corresponding
        // pawn is always on either the 4th (index 3) or 5th (index 4) rank.
        int rankIndex = enPassantSquare.getRankIndex() == 2 ? 3 : 4;
        return getPieceAt(new Square(enPassantSquare.getFileIndex(), rankIndex));
    }

    /**
     * @return Whether the current player to move is in check.
     */
    public boolean isActivePlayerInCheck() {
        Square target = getActivePlayer().getKing().getSquare();
        Move[] attackingMoves = getInactivePlayer().getCapableMoves(this);
        return canHitSquare(attackingMoves, target);
    }

    /**
     * @return Whether the player whose turn it *isn't* is in check. This is an illegal position.
     */
    public boolean isInactivePlayerInCheck() {
        Square target = getInactivePlayer().getKing().getSquare();
        Move[] attackingMoves = getActivePlayer().getCapableMoves(this);
        return canHitSquare(attackingMoves, target);
    }

    /**
     * This is the most important method of Game. Returns legal moves for the player to move.
     * This includes all moves their pieces are normally capable of, plus en passant and castling,
     * minus any that would leave the player in check or otherwise break the rules.
     * @return an array of Moves that can be taken in this position.
     */
    public Move[] getLegalMoves() {
        Move[] capableMoves = getActivePlayer().getCapableMoves(this);
        ArrayList<Move> legalMoves = new ArrayList<>(capableMoves.length);
        for (Move move: capableMoves) {
            if (move.isCastle()) {
                // You can't castle out of check or "through" check.
                // Look at the inactive player's current moves to see whether they can hit
                // the King's current square or the square he passes "through".
                Move[] opponentMoves = getInactivePlayer().getCapableMoves(this);
                if (canHitSquare(opponentMoves, move.getOriginSquare())) {
                    continue;
                }
                
                int fileIndex = (move.getDestSquare().getFileIndex() + 4) / 2; // Average the files.
                Square intermediate = new Square(fileIndex, move.getOriginSquare().getRankIndex());
                if (canHitSquare(opponentMoves, intermediate)) {
                    continue;
                }
            }
            
            // Now check whether making the move would leave this player in check.
            // If so, it is an illegal move.
            Game cloneGame = new Game(this);
            Move cloneMove = new Move(move, cloneGame);
            cloneGame.makeMove(cloneMove);
//            if (cloneGame.isInactivePlayerInCheck()) {
//                continue;
//            }
            
            legalMoves.add(move);
        }
        return legalMoves.toArray(new Move[0]);
    }

    /**
     * Updates the game with the given move being taken.
     * Note: this does not call getLegalMoves() to check that the move is legal; it is
     * assumed that the caller has already done that.
     * 
     * In the future, we can cache the legal moves or flag them as "legal" to skip this assumption.
     * @param move a Move object returned from getLegalMoves.
     */
    public void makeMove(Move move) {
        if (move.isCapture()) {
            Piece captured = move.getCapturedPiece();
            captured.removeFromPlay();
            setPieceAt(captured.getSquare(), null);
        } 
        getActivePlayer().makeMove(move);
        setPieceAt(move.getOriginSquare(), null);
        setPieceAt(move.getDestSquare(), move.getMover());
        enPassantSquare = move.getEnPassantSquare();
        moveHistory.push(move);
        whiteToMove = !whiteToMove;
    }

    /**
     * @return Whether we have a move in the history that we can undo.
     */
    public boolean canUndoLastMove() {
        return !moveHistory.isEmpty();
    }

    /**
     * Restores this Game to the state it was before the last move occurred.
     * This includes returning to play any captured piece, and restoring
     * castle abilities.
     * 
     * If there is no move history that we can undo, this method does nothing.
     */
    public void undoLastMove() {
        if (!canUndoLastMove()) {
            return;
        }
        
        Move lastMove = moveHistory.pop();
        setPieceAt(lastMove.getOriginSquare(), lastMove.getMover());
        setPieceAt(lastMove.getDestSquare(), null);
        
        if (lastMove.isCapture()) {
            Piece captured = lastMove.getCapturedPiece();
            captured.returnToPlay();
            setPieceAt(captured.getSquare(), captured);
        }
        
        // Handle the en-passant square: peek at the preceding move (before the one 
        // we are undoing) to see if it is a pawn double jump. If so, set the en-passant square.
        // Note that FEN files don't have a complete move history, so undoing moves is limited.
        enPassantSquare = moveHistory.isEmpty() ? null : moveHistory.peek().getEnPassantSquare();
        whiteToMove = !whiteToMove;
    }
    
    
    
    private void loadPiecePositionsToBoard(Player player) {
        for (Piece piece : player.getPieces()) {
            setPieceAt(piece.getSquare(), piece);
        }
    }
    
    // Private helper function since external callers should use makeMove();
    private void setPieceAt(Square square, Piece piece) {
        board[square.getFileIndex()][square.getRankIndex()] = piece;
    }
    
    // Returns whether any of the capableMoves have <target> as their destination.
    // This indicates whether a king on <target> is currently checked.
    private boolean canHitSquare(Move[] capableMoves, Square target) {
        return Arrays.stream(capableMoves).anyMatch(move -> move.getDestSquare().equals(target));
    }
}