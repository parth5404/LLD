package models.pieces;

import enums.Color;
import enums.PieceType;
import stratergy.Horizontal;
import stratergy.Vertical;

public class Queen extends Piece {
    public Queen(Color color) {
        super(color);
        stratergyList.add(new Vertical());
        stratergyList.add(new Horizontal());
    }

    @Override
    public PieceType getType() {
        return PieceType.QUEEN;
    }
}
