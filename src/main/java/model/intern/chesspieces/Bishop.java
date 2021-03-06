package model.intern.chesspieces;

import model.common.EnumChessPiece;
import model.intern.chessboard.ChessBoard;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import model.common.EnumChessColor;
import model.intern.chessmove.EnumMovePath;

import java.util.Collections;
import java.util.List;

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
    public List<EnumMovePath> getValidMovePaths() {
        return Collections.singletonList(EnumMovePath.DIAGONAL);
    }

    @Override
    public EnumChessPiece getPieceType() {
        return EnumChessPiece.BISHOP;
    }

    @Override
    public String toString() {
        return "B" + this.getColor();
    }

}
