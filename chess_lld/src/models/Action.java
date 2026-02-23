package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import models.pieces.Piece;

@Getter
@AllArgsConstructor
public class Action {
    private final Piece piece;
    private final int mvNum;
    private final Cell from;
    private final Cell to;
    private final Piece pieceCaptured;
}
