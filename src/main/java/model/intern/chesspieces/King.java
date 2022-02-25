package model.intern.chesspieces;

import model.common.EnumChessPiece;
import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import model.common.Coordinates;
import model.common.EnumChessColor;
import model.intern.chessmove.EnumMovePath;

import java.util.Arrays;
import java.util.List;

public class King extends ChessPiece {

    public King(EnumChessColor color) {
        super(color);
    }

    @Override
    public MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board) {
        Coordinates source = move.getFieldSource().getCoordinates();
        Coordinates target = move.getFieldTarget().getCoordinates();
        int deltaX = target.x() - source.x();
        int deltaY = target.y() - source.y();

        // Castle validation
        if (Math.abs(deltaX) == 2 && movePath == EnumMovePath.LINEAR && !this.hasMoved()) {
            ChessField fieldRook = target.x() == 6 ? board.getField(7, source.y()) : board.getField(0, source.y());
            if (fieldRook.hasPiece() && !fieldRook.getPiece().hasMoved()) {
                ChessField newFieldRook = target.x() == 6 ? board.getField(5, source.y()) : board.getField(3, source.y());
                Move subMove = new Move(fieldRook, newFieldRook);
                return new MoveValidationResult(subMove);
            }
        }

        // Normal validation
        return new MoveValidationResult(
                movePath != EnumMovePath.JUMP
                && (deltaX == 0 || deltaX == 1)
                && (deltaY == 0 || deltaY == 1));
    }

    @Override
    public boolean canMoveToThreatenedField() {
        return false;
    }

    @Override
    public List<EnumMovePath> getValidMovePaths() {
        return Arrays.asList(EnumMovePath.LINEAR, EnumMovePath.DIAGONAL);
    }

    @Override
    public EnumChessPiece getPieceType() {
        return EnumChessPiece.KING;
    }

    @Override
    public String toString() {
        return "K" + this.getColor();
    }

}
