package models.pieces;

import enums.Color;
import enums.PieceType;
import lombok.Getter;
import models.Board;
import models.Cell;
import models.Move;
import stratergy.IMoveStratergy;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class Piece {
    protected int numOfmoves=0;
    protected final Color color;
    protected final List<IMoveStratergy>stratergyList=new ArrayList<>();

    public Piece(Color color){
        this.color=color;
    }
    public abstract PieceType getType();
    public void incrementMoves(){
        numOfmoves++;
    }
    public boolean isValidMove(Cell from, Cell to, Board board){
        for(IMoveStratergy strat:stratergyList){
            if(strat.canMove(board,from,to))return true;
        }
        return false;
    }
    private Optional<Move> move(Board board,Cell from, Cell to){
        for(IMoveStratergy strat:stratergyList){
            Optional<Move> mv=strat.move(board, from, to);
            if(mv.isPresent()){
                return mv;
            }
        }
        return null;
    }

}
