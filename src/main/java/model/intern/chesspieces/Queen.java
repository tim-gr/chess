package model.intern.chesspieces;

import model.intern.common.EnumChessPiece;
import model.intern.chessboard.ChessBoard;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import model.intern.common.EnumChessColor;
import model.intern.chessmove.EnumMovePath;

import java.util.Arrays;
import java.util.List;

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
    public List<EnumMovePath> getValidMovePaths() {
        return Arrays.asList(EnumMovePath.LINEAR, EnumMovePath.DIAGONAL);
    }

    @Override
    public EnumChessPiece getPieceType() {
        return EnumChessPiece.QUEEN;
    }

    @Override
    public String toString() {
        return "Q" + this.getColor();
    }

}
