package stratergy;

import models.Action;
import models.Board;
import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.List;
import java.util.Optional;

public class Vertical implements IMoveStratergy {
    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {
        Piece pc = src.getPiece();
        int step = dest.getRow() - src.getRow();
        int i = src.getRow();
        while (i != dest.getRow()) {
            if (board.getCells()[i][src.getCol()].getPiece() != null) return false;
            i += step;
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
