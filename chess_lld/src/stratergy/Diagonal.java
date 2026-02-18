package stratergy;

import models.Board;
import models.Cell;

public class Diagonal implements IMoveStratergy {
    private boolean find(Cell src,Cell dest,int fx,int fY){
        int sr=src.getRow();
        int sc=src.getCol();
        int dr=dest.getRow();
        int dc=dest.getCol();
        while(true){
            if(sr==dr && dr==dc)
        }
    }
    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {

    }
}
