package stratergy;

import models.Board;
import models.Cell;

public interface IMoveStratergy {
    public boolean canMove(Board board, Cell src, Cell dest);
}
