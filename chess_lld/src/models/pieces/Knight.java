package models.pieces;

import enums.Color;
import enums.PieceType;

public class Knight extends Piece {
    public Knight(Color color){
        super(color);
        stratergyList.add();
    }
    @Override
    PieceType getType() {
        return PieceType.KNIGHT;
    }
}
