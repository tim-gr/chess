package model.intern.chessboard;

import model.intern.chesspieces.*;
import model.common.Coordinates;
import model.common.EnumChessColor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;

public class TestFindPossibleNewFields {

    private ChessBoard chessBoard;

    @BeforeEach
    public void initTest() {
        this.chessBoard = new ChessBoard();
    }

    @Test
    public void testRookBlockedByOwnPieces() {
        ChessField field = this.chessBoard.getField(2,2);
        field.setPiece(new Rook(EnumChessColor.BLACK));
        this.chessBoard.getField(5,2).setPiece(new Queen(EnumChessColor.BLACK));
        this.chessBoard.getField(2,3).setPiece(new Bishop(EnumChessColor.BLACK));

        List<Coordinates> possibleNewFields = field.findPossibleNewFields();

        List<Coordinates> expectedList = new ArrayList<>();
        // Horizontal movement (blocked by queen)
        expectedList.add(new Coordinates(0,2));
        expectedList.add(new Coordinates(1,2));
        expectedList.add(new Coordinates(3,2));
        expectedList.add(new Coordinates(4,2));
        // Vertical movement (blocked by bishop)
        expectedList.add(new Coordinates(2,0));
        expectedList.add(new Coordinates(2,1));

        Assertions.assertEquals(expectedList.size(), possibleNewFields.size());
        assertThat(possibleNewFields, containsInAnyOrder(expectedList.toArray()));
    }

    @Test
    public void testRookBlockedByOtherPieces() {
        ChessField field = this.chessBoard.getField(1,1);
        field.setPiece(new Rook(EnumChessColor.BLACK));
        this.chessBoard.getField(3,1).setPiece(new Queen(EnumChessColor.WHITE));
        this.chessBoard.getField(1,3).setPiece(new Bishop(EnumChessColor.WHITE));

        List<Coordinates> possibleNewFields = field.findPossibleNewFields();

        List<Coordinates> expectedList = new ArrayList<>();
        // Horizontal movement (blocked by queen)
        expectedList.add(new Coordinates(0,1));
        expectedList.add(new Coordinates(2,1));
        expectedList.add(new Coordinates(3,1));
        // Vertical movement (blocked by bishop)
        expectedList.add(new Coordinates(1,0));
        expectedList.add(new Coordinates(1,2));
        expectedList.add(new Coordinates(1,3));

        Assertions.assertEquals(expectedList.size(), possibleNewFields.size());
        assertThat(possibleNewFields, containsInAnyOrder(expectedList.toArray()));
    }

    @Test
    public void testKingNoOtherPiecesMiddleOfBoard() {
        ChessField field = this.chessBoard.getField(4, 4);
        field.setPiece(new King(EnumChessColor.BLACK));

        List<Coordinates> possibleNewFields = field.findPossibleNewFields();

        List<Coordinates> expectedList = new ArrayList<>();
        // Horizontal movement
        expectedList.add(new Coordinates(3, 4));
        expectedList.add(new Coordinates(5, 4));
        // Vertical movement
        expectedList.add(new Coordinates(4, 3));
        expectedList.add(new Coordinates(4, 5));
        // Diagonal movement
        expectedList.add(new Coordinates(5, 5));
        expectedList.add(new Coordinates(3, 3));
        expectedList.add(new Coordinates(3, 5));
        expectedList.add(new Coordinates(5, 3));

        Assertions.assertEquals(expectedList.size(), possibleNewFields.size());
        assertThat(possibleNewFields, containsInAnyOrder(expectedList.toArray()));
    }

    /**
     * Test whether:
     * - Knight jumps not out of the board
     * - Does NOT go on a field with a piece of the same color
     * - Goes on a field with a piece of the other color
     */
    @Test
    public void testKnightJump() {
        ChessField field = this.chessBoard.getField(1, 1);
        field.setPiece(new Knight(EnumChessColor.BLACK));
        // Knight can jump here (piece of other color)
        this.chessBoard.getField(2,3).setPiece(new Queen(EnumChessColor.WHITE));
        // Knight cannot jump here (piece of same color)
        this.chessBoard.getField(3,2).setPiece(new Queen(EnumChessColor.BLACK));

        List<Coordinates> possibleNewFields = field.findPossibleNewFields();

        List<Coordinates> expectedList = new ArrayList<>();
        expectedList.add(new Coordinates(0, 3));
        expectedList.add(new Coordinates(2, 3));
        expectedList.add(new Coordinates(3, 0));

        Assertions.assertEquals(expectedList.size(), possibleNewFields.size());
        assertThat(possibleNewFields, containsInAnyOrder(expectedList.toArray()));
    }

}
