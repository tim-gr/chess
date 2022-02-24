package model.intern.chessboard;

import org.junit.jupiter.api.Test;

public class TestPrintBoard {

    @Test
    public void testPrintBoard() {
        ChessBoard chessBoard = new ChessBoard();
        chessBoard.initChessPieces();
        System.out.println(chessBoard);
        // No assert - only for manually testing ChessBoard.toString() and that this implementation throws no exception.
    }

}
