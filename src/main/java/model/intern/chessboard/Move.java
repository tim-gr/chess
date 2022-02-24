package model.intern.chessboard;

import model.intern.chessboard.move.MoveValidationResult;
import model.intern.common.EnumMovePath;
import model.intern.chesspieces.ChessPiece;
import model.intern.exceptions.ExcNoValidTarget;

public class Move {

    private final ChessField fieldSource;
    private final ChessField fieldTarget;
    private final ChessPiece pieceSource;
    private final ChessPiece pieceTarget;

    private MoveValidationResult moveValidationResult;

    public Move(ChessField fieldSource, ChessField fieldTarget) {
        this.fieldSource = fieldSource;
        this.fieldTarget = fieldTarget;
        this.pieceSource = fieldSource.getPiece();
        this.pieceTarget = fieldTarget.getPiece();
    }

    public Move(ChessField fieldTarget, ChessPiece piece) {
        this.fieldSource = null;
        this.fieldTarget = fieldTarget;
        this.pieceSource = piece;
        this.pieceTarget = null;
    }

    public ChessField getFieldSource() {
        return this.fieldSource;
    }

    public ChessField getFieldTarget() {
        return this.fieldTarget;
    }

    public ChessPiece getPieceSource() {
        return this.pieceSource;
    }

    public ChessPiece getPieceTarget() {
        return this.pieceTarget;
    }

    public void execute(ChessBoard board) throws ExcNoValidTarget {
        this.moveValidationResult = validate(board);

        switch (moveValidationResult.getValidationResult()) {
            case INVALID:
                throw new ExcNoValidTarget();
            case VALID:
                movePieces();
                break;
            case VALID_WITH_SUB_MOVE:
                movePieces();
                moveValidationResult.getSubMove().movePieces();
                break;
        }
    }

    private void movePieces() {
        fieldSource.removePiece();
        fieldTarget.setPiece(fieldSource.getPiece());
        fieldSource.getPiece().registerExecutedMove();
    }

    MoveValidationResult validate(ChessBoard board) {
        if (pieceTarget != null && pieceSource.getColor() == pieceTarget.getColor()) {
            return new MoveValidationResult(false);
        }

        int deltaX = fieldTarget.getCoordinates().x() - fieldSource.getCoordinates().x();
        int deltaY = fieldTarget.getCoordinates().y() - fieldSource.getCoordinates().y();

        MovePath movePath;

        if (deltaX != 0 && Math.abs(deltaX) == Math.abs(deltaY)) {
            // Diagonal movement
            movePath = MovePathCreator.getInstance().createMovePathStraight(
                    board, fieldSource.getCoordinates(), deltaX, deltaY, EnumMovePath.DIAGONAL);

        } else if (deltaX == 0 ^ deltaY == 0) {
            // Linear movement
            movePath = MovePathCreator.getInstance().createMovePathStraight(
                    board, fieldSource.getCoordinates(), deltaX, deltaY, EnumMovePath.LINEAR);

        } else if (Math.abs(deltaX) == 1 && Math.abs(deltaY) == 2 || Math.abs(deltaX) == 2 && Math.abs(deltaY) == 1) {
            // Jump movement
            movePath = MovePathCreator.getInstance().createMovePathJump(
                    board, fieldSource.getCoordinates(), deltaX, deltaY);

        } else {
            // Illegal movement
            return new MoveValidationResult(false);
        }

        boolean targetFieldReachable = movePath.getFieldsOnPath().contains(fieldTarget);

        if (targetFieldReachable) {
            return pieceSource.isMoveValid(movePath.getDirection(), this, board);
        }

        return new MoveValidationResult(false);
    }

    public void revertMove() {
        if (moveValidationResult.getSubMove() != null) {
            moveValidationResult.getSubMove().revertMove();
        }
        fieldSource.setPiece(pieceSource);
        fieldTarget.setPiece(pieceTarget);
        pieceSource.revertMovement();
    }

}
