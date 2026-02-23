package models.pieces;

import enums.Color;
import enums.PieceType;
import stratergy.Lshape;

public class Knight extends Piece {
    public Knight(Color color) {
        super(color);
        stratergyList.add(new Lshape());
    }

    @Override
    public PieceType getType() {
        return PieceType.KNIGHT;
    }
}
