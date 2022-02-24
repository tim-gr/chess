package model.intern.chesspieces;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.Move;
import model.intern.chessboard.move.MoveValidationResult;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumMovePath;

public abstract class ChessPiece {

    private final EnumChessColor color;
    private int numberOfMoves;

    public ChessPiece(EnumChessColor color) {
        this.color = color;
    }

    public EnumChessColor getColor() {
        return this.color;
    }

    public void registerExecutedMove() {
        this.numberOfMoves++;
    }

    public void revertMovement() {
        this.numberOfMoves--;
    }

    public boolean hasMoved() {
        return this.numberOfMoves > 0;
    }

    public abstract MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board);

    public abstract boolean canMoveToThreatenedField();

}
