package games.chess.model;

import games.chess.model.piece.*;

import java.util.ArrayList;
import java.util.Collection;

public class Player {

    private final boolean isWhite;
    boolean canCastleKingside = true;
    boolean canCastleQueenside = true;
    
    private final Piece[] pieces;

    public Player(boolean isWhite) {
        int pieceRankIndex = isWhite ? 0 : 7;
        int pawnRankIndex = isWhite ? 1 : 6;

        pieces = new Piece[16];
        // Add pawns, from a-h
        for (int fileIndex = 0; fileIndex < 8; fileIndex++) {
            pieces[fileIndex] = new Pawn(isWhite, new Square(fileIndex, pawnRankIndex));
        }

        // Other pieces are added outside-in: rooks, knights, bishops, queen and king.
        pieces[8] = new Rook(isWhite, new Square(0, pieceRankIndex));
        pieces[9] = new Rook(isWhite, new Square(7, pieceRankIndex));
        pieces[10] = new Knight(isWhite, new Square(1, pieceRankIndex));
        pieces[11] = new Knight(isWhite, new Square(6, pieceRankIndex));
        pieces[12] = new Bishop(isWhite, new Square(2, pieceRankIndex));
        pieces[13] = new Bishop(isWhite, new Square(5, pieceRankIndex));
        pieces[14] = new Queen(isWhite, new Square(3, pieceRankIndex));
        pieces[15] = new King(isWhite, new Square(4, pieceRankIndex));
        this.isWhite = isWhite;
    }

    /**
     * Create a player from a sorted Collection of Pieces.
     */
    public Player(boolean isWhite, Collection<Piece> parsedPieces) {
        this.isWhite = isWhite;
        this.pieces = parsedPieces.toArray(new Piece[0]);
    }

    public boolean isWhite() {
        return this.isWhite;
    }
    
    public King getKing() {
        return (King)pieces[pieces.length - 1];
    }

    public Piece[] getPieces() {
        return this.pieces.clone();
    }

    /**
     * Updates the player's pieces with the given move.
     * @param move a Move for the player to make.
     */
    public void makeMove(Move move) {
        Piece mover = move.getMover();
        if (isOppositeColor(mover)) {
            return;  // We were given a move for the wrong color piece; do nothing.
        }
        
        // If we are moving the king anywhere, then we can no longer castle (after this move).
        if (mover instanceof King) {
            canCastleQueenside = false;
            canCastleKingside = false;
        }
        
        // If we are moving a rook from one of the rook starting squares, we know
        // that we can no longer castle on that side.
        // Note: this does not handle all castle prohibitions, e.g. what if our rook
        // has been captured? Game will check for the other prohibitions.
        if (mover instanceof Rook) {
            int startingRankIndex = mover.isWhite() ? 0 : 7;
            Square aRookStartingSquare = new Square(0, startingRankIndex);
            Square hRookStartingSquare = new Square(7, startingRankIndex);
            if (mover.getSquare().equals(aRookStartingSquare)) {
                canCastleQueenside = false;
            } else if (mover.getSquare().equals(hRookStartingSquare)) {
                canCastleKingside = false;
            }
        }
        
        mover.makeMove(move);
    }

    /**
     * Returns all moves that the Player is capable of in this position.
     *
     * This includes all on-board squares that the pieces are capable of hitting,
     * minus any that are currently blocked by other pieces. It also includes "special"
     * moves: castling and en-passant.
     *
     * The returned array is expected to contain some "illegal" moves: in particular,
     * moves which result in the player being in check are not filtered out here.
     *
     * @param game Current game state, including the board positions of pieces.
     * @return a list of moves the player could make, if not worried about checks.
     */
    public Move[] getCapableMoves(Game game) {
        ArrayList<Move> moves = new ArrayList<>();
        for (Piece piece : pieces) {
            if (piece != null && piece.isInPlay()) {
                Square[][] capableSquares = piece.getCapableSquares();
                for (Square[] path : capableSquares) {
                    moves.addAll(getAllowedMovesAlongPath(game, piece, path));
                }
            }
        }
        // TODO - add special moves.
        moves.addAll(getPawnCaptures(game));
        moves.addAll(getEnPassantMoves(game));
        
        return moves.toArray(new Move[0]);
    }

    // Iterate along the given path of squares, checking whether there is a blocking piece there.
    protected ArrayList<Move> getAllowedMovesAlongPath(Game game, Piece mover, Square[] path) {
        ArrayList<Move> moves = new ArrayList<>(path.length);
        for (Square dest : path) {
            Piece occupier = game.getPieceAt(dest);
            if (occupier == null) {
                moves.add(new Move(mover, dest)); // Square is empty - valid move!
            } else {
                if (isOppositeColor(occupier)) {
                    if (!(mover instanceof Pawn)) { // Capture if the mover isn't a pawn.
                        moves.add(new Move(mover, dest, true));
                    }
                }
                break; // Break at the first occupied square.
            }
        }
        return moves;
    }

    protected ArrayList<Move> getPawnCaptures(Game game) {
        ArrayList<Move> pawnCaptures = new ArrayList<>();
        int rankModifier = isWhite ? 1 : -1;
        for (Piece mover: pieces) {
            if (mover instanceof Pawn) {
                Square s = mover.getSquare();
                Square left = new Square(s.getFileIndex() - 1,s.getRankIndex() + rankModifier);
                Square right = new Square(s.getFileIndex() + 1, s.getRankIndex() + rankModifier);

                Piece atLeft = game.getPieceAt(left);
                if (atLeft != null && isOppositeColor(atLeft)) {
                    pawnCaptures.add(new Move(mover, left, true));
                }

                Piece atRight = game.getPieceAt(right);
                if (atRight != null && isOppositeColor(atRight)) {
                    pawnCaptures.add(new Move(mover, right, true));
                }
            }
        }
        return pawnCaptures;
    }

    protected ArrayList<Move> getEnPassantMoves(Game game) {
        ArrayList<Move> enPassantMoves = new ArrayList<>(2);
        Piece capturablePiece = game.getEnPassantCapturablePiece();
        if (capturablePiece != null && isOppositeColor(capturablePiece)) {
            Square s = capturablePiece.getSquare();
            Square left = new Square(s.getFileIndex() - 1, s.getRankIndex());
            Square right = new Square(s.getFileIndex() + 1, s.getRankIndex());
            
            Piece atLeft = game.getPieceAt(left);
            if (atLeft instanceof Pawn && isSameColor(atLeft)) {
                Move move = new Move(atLeft, game.getEnPassantSquare(), true);
                enPassantMoves.add(move);
            }
            
            Piece atRight = game.getPieceAt(right);
            if (atRight instanceof Pawn && isSameColor(atRight)) {
                Move move = new Move(atRight, game.getEnPassantSquare(), true);
                enPassantMoves.add(move);
            }
        }
        return enPassantMoves;
    }
    
    // Returns whether the given piece belongs to the player.
    private boolean isSameColor(Piece piece) {
        return piece.isWhite() == this.isWhite;
    }
    
    // Returns whether the given piece belongs to the opponent.
    private boolean isOppositeColor(Piece piece) {
        return piece.isWhite() != this.isWhite;
    }
}