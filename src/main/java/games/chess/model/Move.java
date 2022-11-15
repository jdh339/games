package games.chess.model;

import games.chess.model.piece.Piece;

public class Move {

    private Piece mover;
    private final Square originSquare;
    private final Square destSquare;
    

    public Move(Piece mover, Square destSquare) {
        this.mover = mover;
        this.originSquare = mover.getSquare();
        this.destSquare =  destSquare;
    }
    
    public Square getOriginSquare() {
        return originSquare;
    }
    
    public Square getDestSquare() {
        return destSquare;
    }
    
    public Piece getMover() {
        return mover;
    }
}
