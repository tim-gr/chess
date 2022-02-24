package model.intern.chessboard;

import model.intern.chessboard.move.MoveValidationResult;
import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;
import model.intern.chesspieces.ChessPiece;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ChessField extends Observable {

    private final Coordinates coordinates;
    private ChessPiece piece;

    public ChessField(int x, int y) {
        this.coordinates = new Coordinates(x, y);
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
        this.setChanged();
        notifyObservers(this);
    }

    public List<ChessField> findPossibleNewFields(ChessBoard board) {
        List<ChessField> result = new ArrayList<>();

        if (!this.hasPiece()) {
            return result;
        }

        List<MovePath> movePaths = MovePathCreator.getInstance().findAllMovePaths(board, this.coordinates);
        for (MovePath movePath : movePaths) {
            for (ChessField possibleField : movePath.getFieldsOnPath()) {
                if (!possibleField.hasPiece() || possibleField.getPiece().getColor() != this.getPiece().getColor()) {
                    Move move = new Move(this, possibleField);
                    MoveValidationResult moveValidation = this.getPiece().isMoveValid(movePath.getDirection(), move, board);
                    boolean possibleFieldThreatened = !possibleField.findThreateningMoveDirections(board, this.piece.getColor()).isEmpty();
                    if (moveValidation.isMoveValid() && (!possibleFieldThreatened || this.getPiece().canMoveToThreatenedField())) {
                        result.add(possibleField);
                    }
                }
            }
        }

        return result;
    }

    public List<MovePath> findThreateningMoveDirections(ChessBoard board, EnumChessColor activeColor) {
        return findThreateningMoveDirections(board, activeColor, true);
    }

    private List<MovePath> findThreateningMoveDirections(ChessBoard board, EnumChessColor activeColor, boolean checkOwnProtection) {
        List<MovePath> result = new ArrayList<>();

        List<MovePath> movePaths = MovePathCreator.getInstance().findAllMovePaths(board, this.coordinates);
        for (MovePath movePath : movePaths) {
            ChessField lastFieldOfPath = movePath.getLastFieldOfPath();
            if (lastFieldOfPath != null && lastFieldOfPath.hasPiece() && lastFieldOfPath.getPiece().getColor() != activeColor) {
                Move move = new Move(lastFieldOfPath, this);
                MoveValidationResult attackValidation = lastFieldOfPath.getPiece().isMoveValid(movePath.getDirection(), move, board);
                boolean currentFieldProtectedByOwnPiece = false;
                if (checkOwnProtection) {
                    currentFieldProtectedByOwnPiece = findThreateningMoveDirections(board, activeColor.getOtherColor(), false).isEmpty();
                }
                if (attackValidation.isMoveValid() && (lastFieldOfPath.getPiece().canMoveToThreatenedField() || !currentFieldProtectedByOwnPiece)) {
                    result.add(movePath);
                }
            }
        }

        return result;
    }

    public Coordinates getCoordinates() {
        return this.coordinates;
    }

    public ChessPiece getPiece() {
        return this.piece;
    }

    public boolean hasPiece() {
        return this.piece != null;
    }

    public void removePiece() {
        this.piece = null;
    }

}
