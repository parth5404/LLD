package stratergy;

import enums.Color;
import models.Action;
import models.Board;
import models.Cell;
import models.Move;
import models.pieces.Piece;

import java.util.List;
import java.util.Optional;

public class PawnM implements IMoveStratergy {
    @Override
    public boolean canMove(Board board, Cell src, Cell dest) {
        Piece pc = src.getPiece();
        int mvs = pc.getNumOfmoves();
        int rowDiff = dest.getRow() - src.getRow();
        int colDiff = dest.getCol() - src.getCol();

        if (Math.abs(colDiff) > 1 || Math.abs(rowDiff) > 2)
            return false;

        boolean fwd = pc.getColor() == Color.WHITE;
        int direction = fwd ? 1 : -1;

        if (Math.abs(colDiff) == 1 && rowDiff == direction
                && dest.getPiece() != null
                && dest.getPiece().getColor() != pc.getColor())
            return true;

        if (colDiff != 0 || dest.getPiece() != null)
            return false;
        if (rowDiff == direction)
            return true;
        if (mvs == 0 && rowDiff == 2 * direction) {
            Cell mid = board.getCells()[src.getRow() + direction][src.getCol()];
            return mid.getPiece() == null;
        }
        return false;
    }

    @Override
    public Optional<Move> move(Board board, Cell src, Cell dest) {
        if (!canMove(board, src, dest)) {
            return Optional.empty();
        }
        Action at;
        Piece pc = src.getPiece();
        pc.incrementMoves();
        if (dest.getCol() != src.getCol()) {
            at = new Action(pc, pc.getNumOfmoves(), src, dest, dest.getPiece());
        } else {
            at = new Action(pc, pc.getNumOfmoves(), src, dest, null);
        }
        dest.setPiece(pc);
        src.setPiece(null);
        return Optional.of(new Move(List.of(at)));
    }
}
