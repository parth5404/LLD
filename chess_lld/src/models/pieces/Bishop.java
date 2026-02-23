package models.pieces;

import enums.Color;
import enums.PieceType;
import stratergy.Vertical;

public class Bishop extends Piece {
    public Bishop(Color color) {
        super(color);
        stratergyList.add(new Vertical());
    }

    @Override
    public PieceType getType() {
        return PieceType.BISHOP;
    }
}
