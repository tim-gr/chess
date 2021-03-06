package model.intern.chesspieces;

import model.common.EnumChessPiece;
import model.intern.chessboard.ChessBoard;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import model.common.EnumChessColor;
import model.intern.chessmove.EnumMovePath;

import java.util.Collections;
import java.util.List;

public class Knight extends ChessPiece {

    public Knight(EnumChessColor color) {
        super(color);
    }

    @Override
    public MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board) {
        return new MoveValidationResult(movePath == EnumMovePath.JUMP);
    }

    @Override
    public boolean canMoveToThreatenedField() {
        return true;
    }

    @Override
    public List<EnumMovePath> getValidMovePaths() {
        return Collections.singletonList(EnumMovePath.JUMP);
    }

    @Override
    public EnumChessPiece getPieceType() {
        return EnumChessPiece.KNIGHT;
    }

    @Override
    public String toString() {
        return "H" + this.getColor();
    }

}
