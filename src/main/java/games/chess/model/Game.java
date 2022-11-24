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

    public Square getEnPassantSquare() {
        return enPassantSquare;
    }
    
    private void loadPiecePositionsToBoard(Player player) {
        for (Piece piece : player.getPieces()) {
            Square square = piece.getSquare();
            board[square.getFileIndex()][square.getRankIndex()] = piece;
        }
    }
}