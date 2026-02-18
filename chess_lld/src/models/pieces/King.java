package models.pieces;

import enums.Color;
import enums.PieceType;

public class King extends Piece {
    public King(Color color){
        super(color);
        stratergyList.add();
    }
    @Override
    PieceType getType() {
        return PieceType.KING;
    }
}
