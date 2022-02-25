package model.intern.chessmove;

import static model.intern.chessmove.EnumValidationResult.*;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.*;
import model.common.EnumChessColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestPawnValidateMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Nested
    class TestPawnValidMoves {

        @Test
        public void testPawnWhiteOneStep() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnBlackOneStep() {
            ChessField source = chessBoard.getField(0, 6);
            ChessField target = chessBoard.getField(0, 5);
            source.setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnTwoSteps() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnDiagonalOnFieldWithOpponent() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            target.setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnEnPassant() {
            ChessField source = chessBoard.getField(0, 4);
            ChessField target = chessBoard.getField(1, 5);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            // Simulate opponent's pawn movement of two steps forward from the initial pawn position IN THE LAST MOVE.
            ChessField otherPawnInitialField = chessBoard.getField(1, 6);
            otherPawnInitialField.setPiece(new Pawn(EnumChessColor.BLACK));
            ChessField otherPawnCurrentField = chessBoard.getField(1, 4);
            otherPawnCurrentField.setPiece(new Pawn(EnumChessColor.BLACK));
            chessBoard.addMoveToHistory(new Move(otherPawnInitialField, otherPawnCurrentField));
            otherPawnInitialField.setPiece(null);

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnPromotion() {
            ChessField source = chessBoard.getField(0, 6);
            ChessField target = chessBoard.getField(0, 7);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID_WITH_SUB_MOVE, validationResult.getValidationResult());
            Assertions.assertNotNull(validationResult.getSubMove());
            Assertions.assertEquals(target, validationResult.getSubMove().getFieldTarget());
            Assertions.assertTrue(validationResult.getSubMove().getPieceSource() instanceof Queen);
        }
    }

    @Nested
    class TestPawnInvalidMoves {

        @Test
        public void testPawnTwoStepsButMovedBefore() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            source.getPiece().registerExecutedMove();

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnThreeSteps() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 4);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnOneStepPieceOnTarget() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            target.setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnDiagonalOnEmptyField() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnDiagonalOnFieldWithOwnPiece() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            target.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnTwoStepsOverAnotherPiece() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            chessBoard.getField(0, 2).setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnJump() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnTwoStepsForwardButDiagonal() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(2, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnStepBack() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 0);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }

        @Test
        public void testPawnNoEnPassant() {
            ChessField source = chessBoard.getField(0, 4);
            ChessField target = chessBoard.getField(1, 5);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            // Simulate opponent's pawn movement of one step forward in the last move, so NOT two steps from the initial pawn position.
            ChessField otherPawnInitialField = chessBoard.getField(1, 5);
            otherPawnInitialField.setPiece(new Pawn(EnumChessColor.BLACK));
            ChessField otherPawnCurrentField = chessBoard.getField(1, 4);
            otherPawnCurrentField.setPiece(new Pawn(EnumChessColor.BLACK));
            chessBoard.addMoveToHistory(new Move(otherPawnInitialField, otherPawnCurrentField));
            otherPawnInitialField.setPiece(null);

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(INVALID, validationResult.getValidationResult());
        }
    }

}
