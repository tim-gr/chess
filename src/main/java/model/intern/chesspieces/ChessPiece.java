package model.intern.chesspieces;

import model.common.EnumChessPiece;
import model.intern.chessboard.ChessBoard;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import model.common.EnumChessColor;
import model.intern.chessmove.EnumMovePath;

import java.util.List;

/**
 * A chess piece which can be on a field of a chess board.
 * Chess pieces can have the colors WHITE and BLACK.
 */
public abstract class ChessPiece {

    private final EnumChessColor color;
    private int numberOfMoves;

    public ChessPiece(EnumChessColor color) {
        this.color = color;
    }

    public EnumChessColor getColor() {
        return this.color;
    }

    /**
     * Register a movement of this piece.
     * This is important for castling and the first movement of a pawn.
     */
    public void registerExecutedMove() {
        this.numberOfMoves++;
    }

    /**
     * Unregister a movement of this piece.
     * This is important for castling and the first movement of a pawn.
     */
    public void revertMovement() {
        this.numberOfMoves--;
    }

    /**
     * Returns whether this piece has moved during the game.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasMoved() {
        return this.numberOfMoves > 0;
    }

    /**
     * Returns whether the given move is valid according to the rules for this piece.
     */
    public abstract MoveValidationResult isMoveValid(EnumMovePath movePath, Move move, ChessBoard board);

    /**
     * Returns whether this piece is allowed to go onto a threatened field.
     * The King is not allowed to do this.
     */
    public abstract boolean canMoveToThreatenedField();

    /**
     * Returns all valid ways of moving of this piece.
     */
    public abstract List<EnumMovePath> getValidMovePaths();

    /**
     * Returns the type of this piece.
     */
    public abstract EnumChessPiece getPieceType();

}
