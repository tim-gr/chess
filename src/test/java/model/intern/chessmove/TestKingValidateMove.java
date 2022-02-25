package model.intern.chessmove;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.King;
import model.intern.chesspieces.Knight;
import model.intern.chesspieces.Rook;
import model.common.Coordinates;
import model.common.EnumChessColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static model.intern.chessmove.EnumValidationResult.*;

public class TestKingValidateMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Nested
    class TestKingValidMoves {

        @Test
        public void testKingLinearHorizontalNegativeDelta() {
            ChessField source = chessBoard.getField(1, 1);
            ChessField target = chessBoard.getField(0, 1);
            source.setPiece(new King(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testKingLinearHorizontalPositiveDelta() {
            ChessField source = chessBoard.getField(1, 1);
            ChessField target = chessBoard.getField(2, 1);
            source.setPiece(new King(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testKingLinearVerticalNegativeDelta() {
            ChessField source = chessBoard.getField(1, 1);
            ChessField target = chessBoard.getField(1, 0);
            source.setPiece(new King(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

        @Test
        public void testKingLinearVerticalPositiveDelta() {
            ChessField source = chessBoard.getField(1, 1);
            ChessField target = chessBoard.getField(1, 2);
            source.setPiece(new King(EnumChessColor.WHITE));

            Move move = new Move(source, target);
            Assertions.assertEquals(VALID, move.validate(chessBoard).getValidationResult());
        }

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
    class TestKingInvalidMoves {

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
