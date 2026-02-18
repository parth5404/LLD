package models.pieces;

import enums.Color;
import enums.PieceType;

public class Queen extends Piece {
    public Queen(Color color){
        super(color);
        stratergyList.add();
    }
    @Override
    PieceType getType() {
        return PieceType.QUEEN;
    }
}
