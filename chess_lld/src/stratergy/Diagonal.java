package stratergy;

import models.Action;
import models.Board;
import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.List;
import java.util.Optional;

public class Diagonal implements IMoveStratergy {

    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {
        Piece pc = src.getPiece();
        int srcR = src.getRow();
        int destR = dest.getRow();
        int srcC = src.getCol();
        int destC = dest.getCol();
        if (Math.abs(srcR - destR) != Math.abs(srcC - destC)) return false;
        int rowS = Integer.signum(destR - srcR);
        int colS = Integer.signum(destC - srcC);
        int i = srcR + rowS;
        int j = srcC + colS;
        while (i != destR && j != destC) {
            if (board.getCells()[i][j].getPiece() != null) return false;
            i += rowS;
            j += colS;
        }
        if (dest.getPiece() != null &&
                dest.getPiece().getColor() == pc.getColor()) {
            return false;
        }
        return true;
    }

    @Override
    public Optional<Move> move(Board board, Cell src, Cell dest) {
        if (!canMove(board, src, dest)) {
            return Optional.empty();
        }
        Piece pc = src.getPiece();
        pc.incrementMoves();
        Action at;
        if (dest.getPiece() != null && pc.getColor() != dest.getPiece().getColor()) {
            at = new Action(pc, pc.getNumOfmoves(), src, dest, dest.getPiece());
        } else {
            at = new Action(pc, pc.getNumOfmoves(), src, dest, null);
        }
        src.setPiece(null);
        dest.setPiece(pc);
        return Optional.of(new Move(List.of(at)));
    }
}
