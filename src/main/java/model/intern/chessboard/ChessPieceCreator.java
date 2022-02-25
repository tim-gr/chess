package model.intern.chessboard;

import model.intern.chesspieces.*;
import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;

public class ChessPieceCreator {

    private static final int WIDTH = 8;

    private static ChessPieceCreator chessBoardCreator;
    private ChessPieceCreator(){}

    public static ChessPieceCreator getInstance() {
        if (chessBoardCreator == null) {
            chessBoardCreator = new ChessPieceCreator();
        }
        return chessBoardCreator;
    }

    /**
     * Sets all necessary pieces of a chess board on their starting positions.
     * The current positions of the kings are tracked by the board state.
     */
    void initChessPieces(ChessBoard board) {
        board.getField(new Coordinates(0,0)).setPiece(new Rook(EnumChessColor.WHITE));
        board.getField(new Coordinates(1,0)).setPiece(new Knight(EnumChessColor.WHITE));
        board.getField(new Coordinates(2,0)).setPiece(new Bishop(EnumChessColor.WHITE));
        board.getField(new Coordinates(3,0)).setPiece(new Queen(EnumChessColor.WHITE));
        board.getField(new Coordinates(4,0)).setPiece(new King(EnumChessColor.WHITE));
        board.getField(new Coordinates(5,0)).setPiece(new Bishop(EnumChessColor.WHITE));
        board.getField(new Coordinates(6,0)).setPiece(new Knight(EnumChessColor.WHITE));
        board.getField(new Coordinates(7,0)).setPiece(new Rook(EnumChessColor.WHITE));

        for (int x = 0; x < WIDTH; x++) {
            board.getField(new Coordinates(x,1)).setPiece(new Pawn(EnumChessColor.WHITE));
        }

        for (int x = 0; x < WIDTH; x++) {
            board.getField(new Coordinates(x,6)).setPiece(new Pawn(EnumChessColor.BLACK));
        }

        board.getField(new Coordinates(0,7)).setPiece(new Rook(EnumChessColor.BLACK));
        board.getField(new Coordinates(1,7)).setPiece(new Knight(EnumChessColor.BLACK));
        board.getField(new Coordinates(2,7)).setPiece(new Bishop(EnumChessColor.BLACK));
        board.getField(new Coordinates(3,7)).setPiece(new Queen(EnumChessColor.BLACK));
        board.getField(new Coordinates(4,7)).setPiece(new King(EnumChessColor.BLACK));
        board.getField(new Coordinates(5,7)).setPiece(new Bishop(EnumChessColor.BLACK));
        board.getField(new Coordinates(6,7)).setPiece(new Knight(EnumChessColor.BLACK));
        board.getField(new Coordinates(7,7)).setPiece(new Rook(EnumChessColor.BLACK));
    }

}
