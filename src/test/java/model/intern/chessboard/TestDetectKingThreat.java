package model.intern.chessboard;

import model.common.EnumChessColor;
import model.common.EnumKingThreat;
import model.intern.chesspieces.Bishop;
import model.intern.chesspieces.King;
import model.intern.chesspieces.Pawn;
import model.intern.chesspieces.Rook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestDetectKingThreat {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Test
    public void testNoThreat() {
        ChessField fieldKing = chessBoard.getField(1,1);
        fieldKing.setPiece(new King(EnumChessColor.WHITE));

        EnumKingThreat kingThreat = chessBoard.detectKingThreat(fieldKing);

        assertEquals(EnumKingThreat.NO_THREAT, kingThreat);
    }

    @Test
    public void testCheckKingCanMoveAway() {
        ChessField fieldKing = chessBoard.getField(1,1);
        fieldKing.setPiece(new King(EnumChessColor.WHITE));
        chessBoard.getField(2,2).setPiece(new Pawn(EnumChessColor.BLACK));

        EnumKingThreat kingThreat = chessBoard.detectKingThreat(fieldKing);

        assertEquals(EnumKingThreat.CHECK, kingThreat);
    }

    @Test
    public void testCheckAttackingPieceCanBeTaken() {
        ChessField fieldKing = chessBoard.getField(0,0);
        fieldKing.setPiece(new King(EnumChessColor.WHITE));
        chessBoard.getField(0,1).setPiece(new Pawn(EnumChessColor.WHITE));
        chessBoard.getField(1,1).setPiece(new Pawn(EnumChessColor.WHITE));
        chessBoard.getField(2,1).setPiece(new Bishop(EnumChessColor.WHITE));
        chessBoard.getField(3,0).setPiece(new Rook(EnumChessColor.BLACK));

        EnumKingThreat kingThreat = chessBoard.detectKingThreat(fieldKing);

        assertEquals(EnumKingThreat.CHECK, kingThreat);
    }

    @Test
    public void testCheckOwnPieceCanStopCheck() {
        ChessField fieldKing = chessBoard.getField(0,0);
        fieldKing.setPiece(new King(EnumChessColor.WHITE));
        chessBoard.getField(0,1).setPiece(new Pawn(EnumChessColor.WHITE));
        chessBoard.getField(1,1).setPiece(new Pawn(EnumChessColor.WHITE));
        chessBoard.getField(2,1).setPiece(new Bishop(EnumChessColor.WHITE));
        chessBoard.getField(4,0).setPiece(new Rook(EnumChessColor.BLACK));

        EnumKingThreat kingThreat = chessBoard.detectKingThreat(fieldKing);

        assertEquals(EnumKingThreat.CHECK, kingThreat);
    }

    @Test
    public void testCheckmateInCorner() {
        ChessField fieldKing = chessBoard.getField(0,0);
        fieldKing.setPiece(new King(EnumChessColor.WHITE));
        chessBoard.getField(0,1).setPiece(new Pawn(EnumChessColor.WHITE));
        chessBoard.getField(1,1).setPiece(new Pawn(EnumChessColor.WHITE));
        chessBoard.getField(4,0).setPiece(new Rook(EnumChessColor.BLACK));

        System.out.println(chessBoard);

        EnumKingThreat kingThreat = chessBoard.detectKingThreat(fieldKing);

        assertEquals(EnumKingThreat.CHECKMATE, kingThreat);
    }

}
