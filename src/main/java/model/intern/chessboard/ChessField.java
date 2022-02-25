package model.intern.chessboard;

import model.intern.chessmove.Move;
import model.intern.chessmove.MovePath;
import model.intern.chessmove.MovePathCreator;
import model.intern.chessmove.MoveValidationResult;
import model.intern.chesspieces.King;
import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;
import model.intern.chesspieces.ChessPiece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.stream.Collectors;

public class ChessField extends Observable {

    private final Coordinates coordinates;
    private ChessPiece piece;
    private final ChessBoard board;

    public ChessField(int x, int y, ChessBoard board) {
        this.coordinates = new Coordinates(x, y);
        this.board = board;
    }

    public void setPiece(ChessPiece piece) {
        this.piece = piece;
        this.setChanged();
        if (piece instanceof King) {
            this.board.changeFieldOfKing(this);
        }
        notifyObservers(this);
    }

    public List<Coordinates> findPossibleNewFields() {
        List<ChessField> result = new ArrayList<>();

        if (!this.hasPiece()) {
            return Collections.emptyList();
        }

        List<MovePath> movePaths = MovePathCreator.getInstance().findAllMovePaths(board, this.coordinates, true);
        for (MovePath movePath : movePaths) {
            for (ChessField possibleField : movePath.getFieldsOnPath()) {
                if (!possibleField.hasPiece() || possibleField.getPiece().getColor() != this.getPiece().getColor()) {
                    Move move = new Move(this, possibleField);
                    MoveValidationResult moveValidation = this.getPiece().isMoveValid(movePath.getDirection(), move, board);
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

    public List<Coordinates> findThreateningFields() {
        List<MovePath> threateningMovePaths = this.findThreateningMoveDirections(this.board.getActiveColor());
        return threateningMovePaths.stream()
                .map(threateningMovePath -> threateningMovePath.getLastFieldOfPath().getCoordinates())
                .collect(Collectors.toList());
    }

    public List<MovePath> findThreateningMoveDirections(EnumChessColor activeColor) {
        return findThreateningMoveDirections(activeColor, true);
    }

    private List<MovePath> findThreateningMoveDirections(EnumChessColor activeColor, boolean checkOwnProtection) {
        List<MovePath> result = new ArrayList<>();

        List<MovePath> movePaths = MovePathCreator.getInstance().findAllMovePaths(board, this.coordinates, false);
        for (MovePath movePath : movePaths) {
            ChessField lastFieldOfPath = movePath.getLastFieldOfPath();
            if (lastFieldOfPath != null && lastFieldOfPath.hasPiece() && lastFieldOfPath.getPiece().getColor() != activeColor) {
                Move move = new Move(lastFieldOfPath, this);
                MoveValidationResult attackValidation = lastFieldOfPath.getPiece().isMoveValid(movePath.getDirection(), move, board);
                if (attackValidation.isMoveValid()) {
                    boolean currentFieldProtectedByOwnPiece = false;
                    if (checkOwnProtection) {
                        currentFieldProtectedByOwnPiece = findThreateningMoveDirections(activeColor.getOtherColor(), false).isEmpty();
                    }
                    if (lastFieldOfPath.getPiece().canMoveToThreatenedField() || !currentFieldProtectedByOwnPiece) {
                        result.add(movePath);
                    }
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
