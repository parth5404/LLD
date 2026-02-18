package models.pieces;

import enums.Color;
import enums.PieceType;

public class Rook extends Piece {
    public Rook(Color color){
        super(color);
        stratergyList.add();
    }
    @Override
    PieceType getType() {
        return PieceType.ROOK;
    }
}
