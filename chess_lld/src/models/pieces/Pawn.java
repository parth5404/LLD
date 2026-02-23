package models.pieces;

import enums.Color;
import enums.PieceType;
import stratergy.PawnM;

public class Pawn extends Piece {
    public Pawn(Color color) {
        super(color);
        stratergyList.add(new PawnM());
    }

    @Override
    public PieceType getType() {
        return PieceType.PAWN;
    }
}
