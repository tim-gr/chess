package model.extern;

import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumChessPiece;

/**
 * Information about an update of a field regarding its piece.
 */
public class ExtFieldUpdate {

    private final Coordinates fieldCoordinates;
    private final EnumChessPiece fieldChessPieceType;
    private final EnumChessColor fieldChessPieceColor;

    ExtFieldUpdate(Coordinates coordinates, EnumChessPiece fieldChessPieceType, EnumChessColor fieldChessPieceColor) {
        this.fieldCoordinates = coordinates;
        this.fieldChessPieceType = fieldChessPieceType;
        this.fieldChessPieceColor = fieldChessPieceColor;
    }

    public Coordinates getFieldCoordinates() {
        return fieldCoordinates;
    }

    public EnumChessPiece getFieldChessPieceType() {
        return fieldChessPieceType;
    }

    public EnumChessColor getFieldChessPieceColor() {
        return fieldChessPieceColor;
    }

}
