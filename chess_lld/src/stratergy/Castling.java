package stratergy;

import enums.Color;
import enums.PieceType;
import models.Action;
import models.Board;
import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.List;
import java.util.Optional;

public class Castling implements IMoveStratergy{
    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {
        Piece pc=src.getPiece();
        Color colour=pc.getColor();
        int colDiff=Math.abs(src.getCol()-dest.getCol());
        if(pc.getNumOfmoves()!=0 || colDiff!=2 || pc.getType() != PieceType.KING)return false;
        boolean side=Math.abs(dest.getCol()-7)>1;
        Piece rook=board.getCells()[src.getRow()][side?0:7].getPiece();
        if(rook ==null || rook.getNumOfmoves()!=0 || rook.getColor()!=pc.getColor() || rook.getType()!=PieceType.ROOK)return false;
        int step=side?-1:1;
        for(int i=src.getCol();i>=0||i<=7;i+=step){
            if(board.getCells()[src.getRow()][i].getPiece()!=null)return false;
        }
        return true;
    }

    @Override
    public Optional<Move> move(Board board, Cell src, Cell dest) {
        if(!canMove(board,src,dest)){
            return Optional.empty();
        }
        Piece Pking=src.getPiece();
        Pking.incrementMoves();;
        boolean side=Math.abs(dest.getCol()-7)>1;
        Piece Prook=board.getCells()[src.getRow()][side?0:7].getPiece();
        Prook.incrementMoves();
        Cell rookSrc=board.getCells()[src.getRow()][side?0:7];
        Cell rookDest=board.getCells()[src.getRow()][side?3:5];
        Action king=new Action(Pking,Pking.getNumOfmoves(),src,dest,null);
        Action rook=new Action(Prook, Prook.getNumOfmoves(),rookSrc,rookDest,null);
        src.setPiece(null);
        dest.setPiece(Pking);
        rookSrc.setPiece(null);
        rookDest.setPiece(Prook);
        return Optional.of(new Move(List.of(king, rook)));
    }
}
