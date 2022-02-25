package model.intern.chesspieces;

import model.intern.chessboard.ChessBoard;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import model.intern.common.EnumChessColor;
import model.intern.chessmove.EnumMovePath;

public class Bishop extends ChessPiece {

    public Bishop(EnumChessColor color) {
        super(color);
    }

    @Override
    public MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board) {
        return new MoveValidationResult(movePath == EnumMovePath.DIAGONAL);
    }

    @Override
    public boolean canMoveToThreatenedField() {
        return true;
    }

    @Override
    public String toString() {
        return "B" + this.getColor();
    }

}
