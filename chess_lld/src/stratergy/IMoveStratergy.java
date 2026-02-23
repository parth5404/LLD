package stratergy;

import models.Board;
import models.Cell;
import models.Move;

import java.util.Optional;

public interface IMoveStratergy {
    public boolean canMove(Board board, Cell src, Cell dest);
    public Optional<Move> move(Board board,Cell src, Cell dest);
}
