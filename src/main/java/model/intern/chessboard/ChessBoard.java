package model.intern.chessboard;

import model.intern.chessmove.Move;
import model.intern.chessmove.MovePath;
import model.common.EnumChessColor;
import model.intern.exceptions.*;
import model.common.Coordinates;
import model.common.EnumKingThreat;
import model.intern.chesspieces.*;

import java.util.*;

/**
 * A chess board which consists of 64 chess fields and manages
 * its status in a board state.
 */
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
                this.chessFields[x][y] = new ChessField(x, y, this);
                this.chessFields[x][y].addObserver(this);
            }
        }
    }

    /**
     * Sets all necessary pieces of a chess board on their starting positions.
     * The current positions of the kings are automatically tracked by the board state.
     */
    public void initChessPieces() {
        ChessPieceCreator.getInstance().initChessPieces(this);
    }

    /**
     * Return the field on the board based on the given coordinates, wrapped in a Coordinates object.
     */
    public ChessField getField(Coordinates coordinates) {
        return getField(coordinates.x(), coordinates.y());
    }

    /**
     * Return the field on the board based on the given coordinates.
     * x and y must be inside the interval of [0,7] - including borders.
     */
    public ChessField getField(int x, int y) {
        if (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT) {
            return this.chessFields[x][y];
        } else {
            throw new IllegalArgumentException("Invalid coordinates: (" + x + "," + y + ")");
        }
    }

    /**
     * Execute a move of the piece being on the source field to the target field.
     * If there is no piece of the right color on the source field or the move is not legal/possible,
     * an ExcInvalidMove occurs.
     * @param source Source of the move
     * @param target Target of the move
     * @throws ExcInvalidMove The move is not a valid chess move or the situation on the board does not allow it.
     */
    public void executeMove(Coordinates source, Coordinates target) throws ExcInvalidMove {
        ChessField fieldSource = this.getField(source);
        ChessField fieldTarget = this.getField(target);

        if (!fieldSource.hasPiece()) {
            throw new ExcNoValidSource();
        }

        if (!this.boardState.hasTurn(fieldSource.getPiece().getColor())) {
            throw new ExcDoesNotHaveTurn();
        }

        Move move = new Move(fieldSource, fieldTarget);
        move.execute(this);
        this.boardState.addMoveToHistory(move);

        EnumKingThreat kingThreat = detectKingThreat(this.boardState.getFieldOfKing());
        if (kingThreat != EnumKingThreat.NO_THREAT) {
            // Invalid move, as the king of the active player is now / still checked.
            revertLastMove();
            throw new ExcKingChecked(kingThreat);
        }

        // The king of the active player is not checked. Therefore, everything is ok and the active player changes.
        this.boardState.changePlayer();
        EnumKingThreat kingThreatNewActivePlayer = detectKingThreat(this.boardState.getFieldOfKing());
        this.boardState.setKingThreat(kingThreatNewActivePlayer);
    }

    /**
     * Returns whether the king is threatened and to which degree (check or checkmate).
     * @param fieldKing Field of the king that is analysed regarding who he is threatened
     */
    EnumKingThreat detectKingThreat(ChessField fieldKing) {
        List<MovePath> threateningMovePaths = fieldKing.findThreateningMoveDirections(fieldKing.getPiece().getColor());

        if (threateningMovePaths.isEmpty()) {
            // King is not threatened.
            return EnumKingThreat.NO_THREAT;
        } else if (fieldKing.findPossibleNewFields().stream()
                .anyMatch(possibleNewField ->
                        this.getField(possibleNewField).findThreateningMoveDirections(fieldKing.getPiece().getColor()).isEmpty())) {
            // King is checked but can move to another field that is not threatened.
            return EnumKingThreat.CHECK;
        } else if (threateningMovePaths.size() == 1
                && canOwnPieceBeUsedForKingProtection(fieldKing, threateningMovePaths.get(0))) {
            // King is checked and cannot move to another field.
            // However, there is only one threat, and it can be stopped by another own piece.
            return EnumKingThreat.CHECK;
        } else {
            // King is checkmate, as no possibilities can be found to escape from the threat.
            return EnumKingThreat.CHECKMATE;
        }
    }

    /**
     * Check for every field between the king and the threatening piece whether an own piece can be positioned
     * in between to protect the king.
     */
    boolean canOwnPieceBeUsedForKingProtection(ChessField fieldKing, MovePath threateningMovePath) {
        for (ChessField chessField : threateningMovePath.getFieldsOnPath()) {
            List<MovePath> moveDirectionsOwnPieces = chessField.findThreateningMoveDirections(fieldKing.getPiece().getColor().getOtherColor());
            for (MovePath movePath : moveDirectionsOwnPieces) {
                // The king cannot be protected by himself.
                if (!movePath.getLastFieldOfPath().getCoordinates().equals(fieldKing.getCoordinates())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reverts the last move, if there has been one.
     */
    public void revertLastMove() {
        Move moveToBeReverted = this.boardState.removeMoveFromHistory();
        if (moveToBeReverted != null) {
            moveToBeReverted.revertMove();
            this.boardState.setActiveColor(moveToBeReverted.getPieceSource().getColor());
        }
    }

    /**
     * Returns the color of the active player.
     */
    public EnumChessColor getActiveColor() {
        return this.boardState.getActiveColor();
    }

    /**
     * Returns how and to which degree the king is threatened.
     */
    public EnumKingThreat getKingThreat() {
        return this.boardState.getKingThreat();
    }

    /**
     * Returns the last move on this chess board.
     */
    public Move getLastMove() {
        return this.boardState.getLastMove();
    }

    /**
     * Update the current field of the king of the active player.
     */
    public void changeFieldOfKing(ChessField field) {
        this.boardState.changeFieldOfKing(field);
    }

    /**
     * Returns the field of the king of the current player.
     */
    public ChessField getFieldOfKingActivePlayer() {
        return this.boardState.getFieldOfKing();
    }

    /**
     * Adds the given move to the move history.
     */
    public void addMoveToHistory(Move move) {
        this.boardState.addMoveToHistory(move);
    }

    @Override
    public void update(Observable o, Object arg) {
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
