package model.intern.chessmove;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.ChessPiece;
import model.intern.exceptions.ExcMoveAlreadyExecuted;
import model.intern.exceptions.ExcNoValidTarget;

/**
 * A chess move from one chess field to another.
 * A move can be valid or invalid.
 * If the move is valid, it is possible that a second move (sub move) is necessary, for example when castling.
 */
public class Move {

    private final ChessField fieldSource;
    private final ChessField fieldTarget;
    private final ChessPiece pieceSource;
    private final ChessPiece pieceTarget;

    private MoveValidationResult moveValidationResult;

    /**
     * Standard move from fieldSource to fieldTarget
     */
    public Move(ChessField fieldSource, ChessField fieldTarget) {
        this.fieldSource = fieldSource;
        this.fieldTarget = fieldTarget;
        this.pieceSource = fieldSource.getPiece();
        this.pieceTarget = fieldTarget.getPiece();
    }

    /**
     * Special move for setting piece on fieldTarget without having a source field.
     * Only use it for implementing pawn promotion!
     */
    public Move(ChessField fieldTarget, ChessPiece piece) {
        this.fieldSource = fieldTarget; // On purpose, as sourceField == targetField for a pawn promotion
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

    /**
     * Execute this move (having a permanent effect on the given chess board).
     */
    public void execute(ChessBoard board) throws ExcNoValidTarget {

        if (this.moveValidationResult != null) {
            throw new ExcMoveAlreadyExecuted();
        }

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
        fieldTarget.setPiece(pieceSource);
        pieceSource.registerExecutedMove();
    }

    /**
     * Validate this move according to chess move rules.
     */
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
        if (moveValidationResult != null && moveValidationResult.getSubMove() != null) {
            moveValidationResult.getSubMove().revertMove();
        }
        fieldSource.setPiece(pieceSource);
        fieldTarget.setPiece(pieceTarget);
        pieceSource.revertMovement();
    }

}
