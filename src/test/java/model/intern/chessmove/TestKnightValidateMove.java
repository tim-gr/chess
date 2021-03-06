package model.intern.chessmove;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.Knight;
import model.common.EnumChessColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static model.intern.chessmove.EnumValidationResult.*;
import static model.intern.chessmove.EnumValidationResult.INVALID;

public class TestKnightValidateMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Nested
    class TestKnightValidMoves {

        @Test
        public void testKnightNormalJump() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(2, 2);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID, validationResult.getValidationResult());
        }
    }

    @Nested
    class TestKnightInvalidMoves {

        @Test
        public void testKnightLinear() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testKnightDiagonal() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(2, 1);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testKnightTooLongL() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(2, 3);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

    }

}
