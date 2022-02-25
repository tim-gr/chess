package model.intern.chessmove;

import model.common.EnumChessColor;
import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.chesspieces.*;
import model.intern.exceptions.ExcNoValidTarget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestRevertMove {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Test
    public void testRevertMove() throws ExcNoValidTarget {
        ChessField source = chessBoard.getField(1, 1);
        ChessField target = chessBoard.getField(1, 2);
        ChessPiece piece = new Pawn(EnumChessColor.WHITE);
        source.setPiece(piece);

        Move move = new Move(source, target);
        move.execute(chessBoard);

        assertNull(source.getPiece());
        assertEquals(piece, target.getPiece());

        move.revertMove();

        assertEquals(piece, source.getPiece());
        assertNull(target.getPiece());
    }

    @Test
    public void testRevertMoveWithAttack() throws ExcNoValidTarget {
        ChessField source = chessBoard.getField(1, 1);
        ChessField target = chessBoard.getField(2, 2);
        ChessPiece piece = new Pawn(EnumChessColor.WHITE);
        source.setPiece(piece);
        ChessPiece pieceOpponent = new Pawn(EnumChessColor.BLACK);
        target.setPiece(pieceOpponent);

        Move move = new Move(source, target);
        move.execute(chessBoard);

        assertNull(source.getPiece());
        assertEquals(piece, target.getPiece());

        move.revertMove();

        assertEquals(piece, source.getPiece());
        assertEquals(pieceOpponent, target.getPiece());
    }

    @Test
    public void testRevertCastling() throws ExcNoValidTarget {
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

        move.revertMove();

        assertEquals(pieceKing, source.getPiece());
        assertEquals(pieceRook, fieldRook.getPiece());
        assertNull(target.getPiece());
        assertNull(chessBoard.getField(5,0).getPiece());
    }

    @Test
    public void testRevertPawnPromotion() throws ExcNoValidTarget {
        ChessField source = chessBoard.getField(0, 6);
        ChessField target = chessBoard.getField(0, 7);
        ChessPiece piece = new Pawn(EnumChessColor.WHITE);
        source.setPiece(piece);

        Move move = new Move(source, target);
        move.execute(chessBoard);

        assertNull(source.getPiece());
        assertTrue(target.getPiece() instanceof Queen);

        move.revertMove();

        assertEquals(piece, source.getPiece());
        assertNull(target.getPiece());
    }

}
