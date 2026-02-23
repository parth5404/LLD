package models.pieces;

import enums.Color;
import enums.PieceType;
import stratergy.Horizontal;

public class Rook extends Piece {
    public Rook(Color color) {
        super(color);
        stratergyList.add(new Horizontal());
    }

    @Override
    public PieceType getType() {
        return PieceType.ROOK;
    }
}
