package model.intern.chessboard;

import model.intern.exceptions.*;
import model.intern.common.Coordinates;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumKingThreat;
import model.intern.chesspieces.*;

import java.util.*;
import java.util.stream.Collectors;

public class ChessBoard extends Observable implements Observer {

    private static final int WIDTH = 8;
    private static final int HEIGHT = 8;

    private final ChessField[][] chessFields;
    private final ChessBoardState boardState;

    public ChessBoard() {
        this.chessFields = new ChessField[WIDTH][HEIGHT];
        this.boardState = new ChessBoardState();

        initializeChessFields();
    }

    private void initializeChessFields() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                this.chessFields[x][y] = new ChessField(x, y);
                this.chessFields[x][y].addObserver(this);
            }
        }
    }

    public ChessField getField(Coordinates coordinates) {
        return getField(coordinates.x(), coordinates.y());
    }

    public ChessField getField(int x, int y) {
        return this.chessFields[x][y];
    }

    public void executeMove(Coordinates source, Coordinates target) throws ExcInvalidMove {
        ChessField fieldSource = this.getField(source);
        ChessField fieldTarget = this.getField(target);

        if (!this.boardState.hasTurn(fieldSource.getPiece().getColor())) {
            throw new ExcDoesNotHaveTurn();
        }

        if (!fieldSource.hasPiece()) {
            throw new ExcNoValidSource();
        }

        Move move = new Move(fieldSource, fieldTarget);
        move.execute(this);
        this.boardState.addMoveToHistory(move);

        EnumKingThreat kingThreat = detectKingThreat(this.boardState.getFieldOfKing());
        if (kingThreat != EnumKingThreat.NO_THREAT) {
            // Invalid move, as the king of the active player is now / still checked.
            revertLastMove();
            throw new ExcKingChecked();
        }

        // The king of the active player is not checked. Therefore, everything is ok and the active player changes.
        this.boardState.changePlayer();
        EnumKingThreat kingThreatNewActivePlayer = detectKingThreat(this.boardState.getFieldOfKing());
        this.boardState.setKingThreat(kingThreatNewActivePlayer);
    }

    EnumKingThreat detectKingThreat(ChessField fieldKing) {
        List<MovePath> threateningMovePaths = fieldKing.findThreateningMoveDirections(this, fieldKing.getPiece().getColor());

        if (threateningMovePaths.isEmpty()) {
            // King is not threatened.
            return EnumKingThreat.NO_THREAT;
        } else if (fieldKing.findPossibleNewFields(this).stream()
                .anyMatch(possibleNewField -> possibleNewField.findThreateningMoveDirections(this, fieldKing.getPiece().getColor()).isEmpty())) {
            // King is checked but can move to another field that is not threatened.
            return EnumKingThreat.CHECK;
        } else if (threateningMovePaths.size() == 1
                && canOwnPieceBeUsedForKingProtection(fieldKing, threateningMovePaths.get(0))) {
            // King is checked and cannot move to another field.
            // However, there is only one threat that can be stopped by another own piece.
            return EnumKingThreat.CHECK;
        } else {
            // King is checkmate, as no possibilities could be found to escape from the threat.
            return EnumKingThreat.CHECKMATE;
        }
    }

    /**
     * Check for every field between the king and the threatening piece whether an own piece can be positioned
     * in between to protect the king.
     */
    boolean canOwnPieceBeUsedForKingProtection(ChessField fieldKing, MovePath threateningMovePath) {
        for (ChessField chessField : threateningMovePath.getFieldsOnPath()) {
            List<MovePath> moveDirectionsOwnPieces = chessField.findThreateningMoveDirections(this, fieldKing.getPiece().getColor());
            for (MovePath movePath : moveDirectionsOwnPieces) {
                // The king cannot be protected by himself.
                if (!movePath.getLastFieldOfPath().getCoordinates().equals(fieldKing.getCoordinates())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void revertLastMove() {
        Move moveToBeReverted = this.boardState.removeMoveFromHistory();
        // Check whether there is any move to revert.
        if (moveToBeReverted != null) {
            moveToBeReverted.revertMove();
            this.boardState.setActiveColor(moveToBeReverted.getPieceSource().getColor());
        }
    }

    public List<Coordinates> findPossibleNewFields(Coordinates coordinatesSource) {
        List<ChessField> possibleNewFields = this.getField(coordinatesSource).findPossibleNewFields(this);
        return possibleNewFields.stream()
                .map(ChessField::getCoordinates)
                .collect(Collectors.toList());
    }

    public List<Coordinates> findThreateningFields(Coordinates coordinatesSource) {
        List<MovePath> threateningMovePaths =
                this.getField(coordinatesSource).findThreateningMoveDirections(this, this.boardState.getActiveColor());
        return threateningMovePaths.stream()
                .map(threateningMovePath -> threateningMovePath.getLastFieldOfPath().getCoordinates())
                .collect(Collectors.toList());
    }

    public void initChessPieces() {
        this.getField(new Coordinates(0,0)).setPiece(new Rook(EnumChessColor.WHITE));
        this.getField(new Coordinates(1,0)).setPiece(new Knight(EnumChessColor.WHITE));
        this.getField(new Coordinates(2,0)).setPiece(new Bishop(EnumChessColor.WHITE));
        this.getField(new Coordinates(3,0)).setPiece(new Queen(EnumChessColor.WHITE));
        this.getField(new Coordinates(4,0)).setPiece(new King(EnumChessColor.WHITE));
        this.getField(new Coordinates(5,0)).setPiece(new Bishop(EnumChessColor.WHITE));
        this.getField(new Coordinates(6,0)).setPiece(new Knight(EnumChessColor.WHITE));
        this.getField(new Coordinates(7,0)).setPiece(new Rook(EnumChessColor.WHITE));

        for (int x = 0; x < WIDTH; x++) {
            this.getField(new Coordinates(x,1)).setPiece(new Pawn(EnumChessColor.WHITE));
        }

        for (int x = 0; x < WIDTH; x++) {
            this.getField(new Coordinates(x,6)).setPiece(new Pawn(EnumChessColor.BLACK));
        }

        this.getField(new Coordinates(0,7)).setPiece(new Rook(EnumChessColor.BLACK));
        this.getField(new Coordinates(1,7)).setPiece(new Knight(EnumChessColor.BLACK));
        this.getField(new Coordinates(2,7)).setPiece(new Bishop(EnumChessColor.BLACK));
        this.getField(new Coordinates(3,7)).setPiece(new Queen(EnumChessColor.BLACK));
        this.getField(new Coordinates(4,7)).setPiece(new King(EnumChessColor.BLACK));
        this.getField(new Coordinates(5,7)).setPiece(new Bishop(EnumChessColor.BLACK));
        this.getField(new Coordinates(6,7)).setPiece(new Knight(EnumChessColor.BLACK));
        this.getField(new Coordinates(7,7)).setPiece(new Rook(EnumChessColor.BLACK));

        this.boardState.changeFieldOfKing(EnumChessColor.WHITE, this.getField(new Coordinates(4,0)));
        this.boardState.changeFieldOfKing(EnumChessColor.BLACK, this.getField(new Coordinates(4,7)));
    }

    public ChessBoardState getBoardState() {
        return this.boardState;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ChessField) {
            ChessField changedChessField = (ChessField) arg;
            if (changedChessField.hasPiece() && changedChessField.getPiece() instanceof King) {
                this.boardState.changeFieldOfKing(changedChessField);
            }
            System.out.println(changedChessField.getCoordinates().x() + ", " + changedChessField.getCoordinates().y());
        }
        this.setChanged();
        notifyObservers(arg);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int y = HEIGHT - 1; y > -1; y--) {
            for (int x = 0; x < WIDTH; x++) {
                ChessPiece piece = this.getField(x, y).getPiece();
                if (piece == null) {
                    result.append("--");
                } else {
                    result.append(piece);
                }
                result.append(" ");
            }
            result.append(System.getProperty("line.separator"));
        }
        return result.toString();
    }

}
