package games.chess.model;

import games.chess.model.piece.Piece;

public class Game {

    private boolean whiteToMove;
    private Piece[][] board;
    private Player whitePlayer;
    private Player blackPlayer;
    private Square enPassantSquare;

    public Game() {
        whiteToMove = true;
        whitePlayer = new Player(true);
        blackPlayer = new Player(false);
        board = new Piece[8][8];
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
        this.whitePlayer = white;
        this.blackPlayer = black;
        board = new Piece[8][8];
        this.enPassantSquare = enPassantSquare;
        loadPiecePositionsToBoard(white);
        loadPiecePositionsToBoard(black);
    }


    public Piece getPieceAt(Square square) {
        if (!square.isOnBoard()) {
            return null;
        }
        return board[square.getFileIndex()][square.getRankIndex()];
    }

    public Player getNextPlayer() {
        return whiteToMove ? whitePlayer : blackPlayer;
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
     * This is the most important method of Game. Returns legal moves for the player to move.
     * This includes all moves their pieces are normally capable of, plus en passant and castling,
     * minus any that would leave the player in check or otherwise break the rules.
     * @return an array of Moves that can be taken in this position.
     */
    public Move[] getLegalMoves() {
        Move[] legalMoves = null;
        Move[] capableMoves = getNextPlayer().getCapableMoves(this);
        // TODO check legality
        return capableMoves;
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
            Piece captured = getPieceAt(move.getDestSquare());
            
            // In the special case of en passant, the captured piece is on a different
            // square than the "dest square".
            if (move.getDestSquare() == enPassantSquare) {
                captured = getEnPassantCapturablePiece();
            }
            captured.removeFromPlay();
        } else if (move.isPawnDoubleJump()) {
            // For a pawn double jump, set the en passant square to the square behind the pawn. 
            int rankModifier = move.getMover().isWhite() ? -1 : 1;
            int rankIndex = move.getDestSquare().getRankIndex() + rankModifier;
            enPassantSquare = new Square(move.getDestSquare().getFileIndex(), rankIndex);
        }
        getNextPlayer().makeMove(move);
        setPieceAt(move.getOriginSquare(), null);
        setPieceAt(move.getDestSquare(), move.getMover());
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
}