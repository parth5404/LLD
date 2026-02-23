package stratergy;

import models.Action;
import models.Board;
import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.List;
import java.util.Optional;

public class KingM implements IMoveStratergy {
    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {
        Piece pc = src.getPiece();
        int rowDiff = Math.abs(src.getRow() - dest.getRow());
        int colDiff = Math.abs(src.getCol() - dest.getCol());
        if (rowDiff > 1 || colDiff > 1) return false;
        if (dest.getPiece() != null && dest.getPiece().getColor() == pc.getColor()) return false;

        return true;
    }

    @Override
    public Optional<Move> move(Board board, Cell src, Cell dest) {
        if (!canMove(board, src, dest)) {
            return Optional.empty();
        }
        Action at;
        Piece pc = src.getPiece();
        pc.incrementMoves();
        if (dest.getPiece() != null && dest.getPiece().getColor() != pc.getColor()) {
            at = new Action(pc, pc.getNumOfmoves(), src, dest, dest.getPiece());
        } else {
            at = new Action(pc, pc.getNumOfmoves(), src, dest, null);
        }
        dest.setPiece(pc);
        src.setPiece(null);
        return Optional.of(new Move(List.of(at)));
    }
}
