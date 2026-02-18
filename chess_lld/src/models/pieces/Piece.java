package models.pieces;

import enums.Color;
import enums.PieceType;
import models.Move;
import stratergy.IMoveStratergy;

import java.util.ArrayList;
import java.util.List;

public abstract class Piece {
    protected int numOfmoves=0;
    protected final Color color;
    protected final List<IMoveStratergy>stratergyList=new ArrayList<>();

    public Piece(Color color){
        this.color=color;
    }
    abstract PieceType getType();
    public boolean isValidMove(){
        return true;
    }
    private Move move(){
        return null;
    }

}
