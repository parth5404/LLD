package models.pieces;

import enums.Color;
import enums.PieceType;
import stratergy.Castling;
import stratergy.KingM;

public class King extends Piece {
    public King(Color color) {
        super(color);
        stratergyList.add(new Castling());
        stratergyList.add(new KingM());
    }

    @Override
    public PieceType getType() {
        return PieceType.KING;
    }
}
