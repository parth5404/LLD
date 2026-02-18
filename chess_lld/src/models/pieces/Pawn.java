package models.pieces;

import enums.Color;
import enums.PieceType;

public class Pawn extends Piece {
    public Pawn(Color color){
        super(color);
        stratergyList.add();
    }
    @Override
    PieceType getType() {
        return PieceType.PAWN;
    }
}
