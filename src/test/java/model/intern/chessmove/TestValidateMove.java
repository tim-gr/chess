package model.intern.chessmove;

import static model.intern.chessmove.EnumValidationResult.*;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.*;
import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;
import model.intern.chessmove.Move;
import model.intern.chessmove.MoveValidationResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestValidateMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Nested
    class PawnTestValidMoves {

        @Test
        public void testPawnWhiteOneStep() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnBlackOneStep() {
            ChessField source = chessBoard.getField(0, 6);
            ChessField target = chessBoard.getField(0, 5);
            source.setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnTwoSteps() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnDiagonalOnFieldWithOpponent() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            target.setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
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
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnPromotion() {
            ChessField source = chessBoard.getField(0, 6);
            ChessField target = chessBoard.getField(0, 7);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult moveValidationResult = move.validate(chessBoard);
            Assertions.assertEquals(VALID_WITH_SUB_MOVE, moveValidationResult.getValidationResult());
            Assertions.assertNotNull(moveValidationResult.getSubMove());
            Assertions.assertEquals(target, moveValidationResult.getSubMove().getFieldTarget());
            Assertions.assertTrue(moveValidationResult.getSubMove().getPieceSource() instanceof Queen);
        }
    }

    @Nested
    class PawnTestInvalidMoves {

        @Test
        public void testPawnTwoStepsButMovedBefore() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            source.getPiece().registerExecutedMove();

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnThreeSteps() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 4);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnOneStepPieceOnTarget() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            target.setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnDiagonalOnEmptyField() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnDiagonalOnFieldWithOwnPiece() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            target.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnTwoStepsOverAnotherPiece() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));
            chessBoard.getField(0, 2).setPiece(new Pawn(EnumChessColor.BLACK));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnJump() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(1, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnTwoStepsForwardButDiagonal() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(2, 3);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testPawnStepBack() {
            ChessField source = chessBoard.getField(0, 1);
            ChessField target = chessBoard.getField(0, 0);
            source.setPiece(new Pawn(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
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
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }
    }

    @Nested
    class KnightTestValidMoves {

        @Test
        public void testKnightNormalJump() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(2, 2);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }
    }

    @Nested
    class KnightTestInvalidMoves {

        @Test
        public void testKnightLinear() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testKnightDiagonal() {
            ChessField source = chessBoard.getField(1, 0);
            ChessField target = chessBoard.getField(2, 1);
            source.setPiece(new Knight(EnumChessColor.WHITE));

            Move move = new Move(source, target);

            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }
    }

    @Nested
    class KingTestValidMoves {

        @Test
        public void testKingCastleRight() {
            ChessField source = chessBoard.getField(4, 0);
            ChessField target = chessBoard.getField(6, 0);
            source.setPiece(new King(EnumChessColor.WHITE));
            ChessField fieldRook = chessBoard.getField(7, 0);
            fieldRook.setPiece(new Rook(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID_WITH_SUB_MOVE, validationResult.getValidationResult());
            Assertions.assertNotNull(validationResult.getSubMove());
            Assertions.assertEquals(fieldRook.getCoordinates(), validationResult.getSubMove().getFieldSource().getCoordinates());
            Assertions.assertEquals(new Coordinates(5, 0), validationResult.getSubMove().getFieldTarget().getCoordinates());
        }

        @Test
        public void testKingCastleLeft() {
            ChessField source = chessBoard.getField(4, 0);
            ChessField target = chessBoard.getField(2, 0);
            source.setPiece(new King(EnumChessColor.WHITE));
            ChessField fieldRook = chessBoard.getField(0,0);
            fieldRook.setPiece(new Rook(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            MoveValidationResult validationResult = move.validate(chessBoard);

            Assertions.assertEquals(VALID_WITH_SUB_MOVE, validationResult.getValidationResult());
            Assertions.assertNotNull(validationResult.getSubMove());
            Assertions.assertEquals(fieldRook.getCoordinates(), validationResult.getSubMove().getFieldSource().getCoordinates());
            Assertions.assertEquals(new Coordinates(3, 0), validationResult.getSubMove().getFieldTarget().getCoordinates());
        }
    }

    @Nested
    class KingTestInvalidMoves {

        @Test
        public void testKingCastleKingHasMoved() {
            ChessField source = chessBoard.getField(4, 0);
            ChessField target = chessBoard.getField(6, 0);
            source.setPiece(new King(EnumChessColor.WHITE));
            source.getPiece().registerExecutedMove();
            ChessField fieldRook = chessBoard.getField(7, 0);
            fieldRook.setPiece(new Rook(EnumChessColor.WHITE));

            Move move = new Move(source, target);

            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testKingCastleRookHasMoved() {
            ChessField source = chessBoard.getField(4, 0);
            ChessField target = chessBoard.getField(6, 0);
            source.setPiece(new King(EnumChessColor.WHITE));
            ChessField fieldRook = chessBoard.getField(7, 0);
            fieldRook.setPiece(new Rook(EnumChessColor.WHITE));
            fieldRook.getPiece().registerExecutedMove();

            Move move = new Move(source, target);

            Assertions.assertEquals(INVALID, move.validate(chessBoard).getValidationResult());
        }
    }

}
