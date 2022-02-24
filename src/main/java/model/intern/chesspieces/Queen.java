package model.intern.chesspieces;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.Move;
import model.intern.chessboard.move.MoveValidationResult;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumMovePath;

public class Queen extends ChessPiece {

    public Queen(EnumChessColor color) {
        super(color);
    }

    @Override
    public MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board) {
        return new MoveValidationResult(movePath != EnumMovePath.JUMP);
    }

    @Override
    public boolean canMoveToThreatenedField() {
        return true;
    }

    @Override
    public String toString() {
        return "Q" + this.getColor();
    }

}