package model.intern.chessmove;

import model.common.EnumChessColor;
import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.ChessPiece;
import model.intern.chesspieces.King;
import model.intern.chesspieces.Pawn;
import model.intern.chesspieces.Rook;
import model.intern.exceptions.ExcMoveAlreadyExecuted;
import model.intern.exceptions.ExcNoValidTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestExecuteMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Test
    public void testValidMove() throws ExcNoValidTarget {
        ChessField source = chessBoard.getField(1, 1);
        ChessField target = chessBoard.getField(1, 2);
        ChessPiece piece = new Pawn(EnumChessColor.WHITE);
        source.setPiece(piece);

        Move move = new Move(source, target);
        move.execute(chessBoard);

        assertNull(source.getPiece());
        assertEquals(piece, target.getPiece());
    }

    @Test
    public void testInvalidMove() {
        ChessField source = chessBoard.getField(1, 1);
        ChessField target = chessBoard.getField(1, 4);
        source.setPiece(new Pawn(EnumChessColor.WHITE));

        Move move = new Move(source, target);
        assertThrows(ExcNoValidTarget.class, () -> move.execute(this.chessBoard));
    }

    @Test
    public void testMoveWithSubMove() throws ExcNoValidTarget {
        ChessField source = chessBoard.getField(4, 0);
        ChessField target = chessBoard.getField(6, 0);
        ChessPiece pieceKing = new King(EnumChessColor.WHITE);
        source.setPiece(pieceKing);
        ChessField fieldRook = chessBoard.getField(7,0);
        ChessPiece pieceRook = new Rook(EnumChessColor.WHITE);
        fieldRook.setPiece(pieceRook);

        Move move = new Move(source, target);
        move.execute(chessBoard);

        assertEquals(pieceKing, target.getPiece());
        assertEquals(pieceRook, chessBoard.getField(5,0).getPiece());
        assertNull(source.getPiece());
        assertNull(fieldRook.getPiece());
    }

    @Test
    public void testExecuteMoveTwice() throws ExcNoValidTarget {
        ChessField source = chessBoard.getField(0, 1);
        ChessField target = chessBoard.getField(0, 2);
        source.setPiece(new Pawn(EnumChessColor.WHITE));

        Move move = new Move(source, target);
        move.execute(chessBoard);
        assertThrows(ExcMoveAlreadyExecuted.class, () -> move.execute(chessBoard));
    }

}
