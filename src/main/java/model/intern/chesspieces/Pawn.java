package model.intern.chesspieces;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chessboard.Move;
import model.intern.chessboard.move.MoveValidationResult;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumMovePath;

public class Pawn extends ChessPiece {

    public Pawn(EnumChessColor color) {
        super(color);
    }

    @Override
    public MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board) {
        ChessField target = move.getFieldTarget();
        ChessField source = move.getFieldSource();
        int deltaX = target.getCoordinates().x() - source.getCoordinates().x();
        int deltaY = target.getCoordinates().y() - source.getCoordinates().y();

        boolean moveValid =
                movePath != EnumMovePath.JUMP
                && (isOneStepForward(deltaY) || (!this.hasMoved() && isTwoStepsForward(deltaY)))
                && (deltaX == 0 && target.getPiece() == null
                        || Math.abs(deltaX) == 1 && target.getPiece() != null && target.getPiece().getColor() != source.getPiece().getColor()
                        || Math.abs(deltaX) == 1 && isEnPassantPossible(board.getBoardState().getLastMove(), source));

        if (moveValid &&
                (this.getColor() == EnumChessColor.BLACK && target.getCoordinates().y() == 0)
                || (this.getColor() == EnumChessColor.WHITE && target.getCoordinates().y() == 7)) {
            Move subMove = new Move(target, new Queen(this.getColor()));
            return new MoveValidationResult(subMove);
        }

        return new MoveValidationResult(moveValid);
    }

    /**
     * Returns whether an "En passant" move is possible.
     *
     * "En passant" is a special play in chess. It is possible when the following conditions apply:
     * - In the move before the current one, the opponent moved a pawn from the initial position two steps forward.
     * - The own pawn stands now directly next to the opponent's pawn.
     */
    private boolean isEnPassantPossible(Move lastMove, ChessField source) {
        if (lastMove == null) {
            return false;
        }
        return lastMove.getPieceSource() instanceof Pawn
                && lastMove.getPieceSource().getColor() != source.getPiece().getColor()
                && Math.abs(lastMove.getFieldTarget().getCoordinates().y() - lastMove.getFieldSource().getCoordinates().y()) == 2
                && lastMove.getFieldTarget().getCoordinates().y() - source.getCoordinates().y() == 0
                && Math.abs(lastMove.getFieldTarget().getCoordinates().x() - source.getCoordinates().x()) == 1;
    }

    private boolean isTwoStepsForward(int deltaY) {
        if (this.getColor() == EnumChessColor.BLACK) {
            return deltaY == -2;
        } else {
            return deltaY == 2;
        }
    }

    private boolean isOneStepForward(int deltaY) {
        if (this.getColor() == EnumChessColor.BLACK) {
            return deltaY == -1;
        } else {
            return deltaY == 1;
        }
    }

    @Override
    public boolean canMoveToThreatenedField() {
        return true;
    }

    @Override
    public String toString() {
        return "P" + this.getColor();
    }

}