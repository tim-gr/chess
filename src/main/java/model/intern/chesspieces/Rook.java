package model.intern.chesspieces;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.Move;
import model.intern.chessboard.move.MoveValidationResult;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumMovePath;

public class Rook extends ChessPiece {

    public Rook(EnumChessColor color) {
        super(color);
    }

    @Override
    public MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board) {
        return new MoveValidationResult(movePath == EnumMovePath.LINEAR);
    }

    @Override
    public boolean canMoveToThreatenedField() {
        return true;
    }

    @Override
    public String toString() {
        return "R" + this.getColor();
    }

}
