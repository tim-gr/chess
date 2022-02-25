package model.intern.chessboard;

import model.intern.chessmove.Move;
import model.intern.chessmove.MovePath;
import model.intern.chessmove.MovePathCreator;
import model.intern.chessmove.MoveValidationResult;
import model.intern.chesspieces.King;
import model.common.Coordinates;
import model.common.EnumChessColor;
import model.intern.chesspieces.ChessPiece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

/**
 * A chess field on a chess board.
 */
public class ChessField extends Observable {

    private final Coordinates coordinates;
    private ChessPiece piece;
    private final ChessBoard board;

    public ChessField(int x, int y, ChessBoard board) {
        this.coordinates = new Coordinates(x, y);
        this.board = board;
    }

    /**
     * Sets the given piece on this chess field.
     * All observers of this field are notified about this to be able to react to the piece change on this field.
     */
    public void setPiece(ChessPiece piece) {
        this.piece = piece;
        if (piece instanceof King) {
            this.board.changeFieldOfKing(this);
        }
        this.setChanged();
        notifyObservers(this);
    }

    /**
     * Returns all possible fields, where the piece on this field can go to.
     */
    public List<Coordinates> findPossibleNewFields() {
        List<ChessField> result = new ArrayList<>();

        if (!this.hasPiece()) {
            return Collections.emptyList();
        }

        List<MovePath> movePaths = MovePathCreator.getInstance().findAllMovePaths(this.board, this, true);
        for (MovePath movePath : movePaths) {
            for (ChessField possibleField : movePath.getFieldsOnPath()) {
                if (!possibleField.hasPiece() || possibleField.getPiece().getColor() != this.getPiece().getColor()) {
                    Move move = new Move(this, possibleField);
                    MoveValidationResult moveValidation = this.getPiece().isMoveValid(movePath.getDirection(), move, this.board);
                    boolean possibleFieldThreatened = !possibleField.findThreateningMoveDirections(this.piece.getColor()).isEmpty();
                    if (moveValidation.isMoveValid() && (!possibleFieldThreatened || this.getPiece().canMoveToThreatenedField())) {
                        result.add(possibleField);
                    }
                }
            }
        }

        return result.stream()
                .map(ChessField::getCoordinates)
                .collect(Collectors.toList());
    }

    /**
     * Returns the coordinates of all fields that this field. This means that there is an opponent piece
     * on the returned coordinates which is able to reach this field with one move.
     */
    public List<Coordinates> findThreateningFields() {
        List<MovePath> threateningMovePaths = this.findThreateningMoveDirections(this.board.getActiveColor());
        return threateningMovePaths.stream()
                .map(threateningMovePath -> threateningMovePath.getLastFieldOfPath().getCoordinates())
                .collect(Collectors.toList());
    }

    /**
     * Returns all MovePaths that threaten this field. This means that there is an opponent piece
     * at the end of the MovePath which is able to reach this field with one move.
     */
    public List<MovePath> findThreateningMoveDirections(EnumChessColor activeColor) {
        return findThreateningMoveDirections(activeColor, true);
    }

    private List<MovePath> findThreateningMoveDirections(EnumChessColor activeColor, boolean checkOwnProtection) {
        List<MovePath> result = new ArrayList<>();

        List<MovePath> movePaths = MovePathCreator.getInstance().findAllMovePaths(this.board, this, false);
        for (MovePath movePath : movePaths) {
            ChessField lastFieldOfPath = movePath.getLastFieldOfPath();
            if (lastFieldOfPath != null && lastFieldOfPath.hasPiece() && lastFieldOfPath.getPiece().getColor() != activeColor) {
                Move move = new Move(lastFieldOfPath, this);
                MoveValidationResult attackValidation = lastFieldOfPath.getPiece().isMoveValid(movePath.getDirection(), move, this.board);
                // The attack move has to be valid in any case AND
                // the opponent piece must either be able to go on a protected field (so not being a King) OR
                // the field has to be not protected at all.
                if (attackValidation.isMoveValid() &&
                        (lastFieldOfPath.getPiece().canMoveToThreatenedField()
                                || !isCurrentFieldProtectedByOwnPiece(activeColor, checkOwnProtection))) {
                    result.add(movePath);
                }
            }
        }

        return result;
    }

    /**
     * Returns always false, if checkOwnProtection is given as false.
     * If checkOwnProtection is true, it returns whether this field is protected by a piece of the active color.
     * This field can never be protected by the piece that is currently on it.
     */
    private boolean isCurrentFieldProtectedByOwnPiece(EnumChessColor activeColor, boolean checkOwnProtection) {
        return !checkOwnProtection
                || findThreateningMoveDirections(activeColor.getOtherColor(), false).isEmpty();
    }

    /**
     * Returns the coordinates of this field.
     */
    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    /**
     * Returns the piece that is currently on this field.
     */
    public ChessPiece getPiece() {
        return this.piece;
    }

    /**
     * Returns whether there is currently a piece on this field.
     */
    public boolean hasPiece() {
        return this.piece != null;
    }

    /**
     * Removes the current piece from this field.
     * This method can also be called, when there is no piece to remove.
     */
    public void removePiece() {
        this.piece = null;
    }

}
