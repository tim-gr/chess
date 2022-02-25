package model.intern.chessmove;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.King;
import model.intern.chesspieces.Rook;
import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static model.intern.chessmove.EnumValidationResult.INVALID;
import static model.intern.chessmove.EnumValidationResult.VALID_WITH_SUB_MOVE;

public class TestKingValidateMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Nested
    class TestKingValidMoves {

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
