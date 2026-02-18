package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import models.pieces.Piece;

@AllArgsConstructor
@Getter
@Setter
public class Cell {
    private int row;
    private int col;
    private Piece piece;
}
