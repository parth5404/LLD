package models.pieces;

import enums.Color;
import enums.PieceType;

public class Bishop extends Piece {
    public Bishop(Color color){
        super(color);
        stratergyList.add();
    }
    @Override
    PieceType getType() {
        return PieceType.BISHOP;
    }
}
