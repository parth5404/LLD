package stratergy;

import models.Action;
import models.Board;
import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.List;
import java.util.Optional;

public class Lshape implements IMoveStratergy {
    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {
        Piece pc = src.getPiece();
        int stepC = Math.abs(dest.getCol() - src.getCol());
        int stepR = Math.abs(dest.getRow() - src.getRow());
        if (!((stepC == 2 && stepR == 1) || (stepC == 1 && stepR == 2))) {
            return false;
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
