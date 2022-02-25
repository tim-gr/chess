package model.extern;

import model.common.EnumChessColor;
import model.common.EnumChessPiece;
import model.common.EnumKingThreat;
import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.common.Coordinates;
import model.intern.exceptions.ExcInvalidMove;

import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Facade for interacting with the chess model.
 * This is the only class that is allowed to be accessed by clients.
 */
public class ChessGame extends Observable implements Observer {

    private ChessBoard chessBoard;

    /**
     * Start or restart a chess game. If there is current progress, this is lost when (re)starting the game.
     * @param observer Register an observer to stay updated about all piece changes on chess fields.
     *                 The update will be of type ExtFieldUpdate.
     */
    public void startNewGame(Observer observer) {
        if (this.chessBoard != null) {
            // Clean up old observers
            this.chessBoard.deleteObservers();
        }

        this.chessBoard = new ChessBoard();

        if (observer != null) {
            this.addObserver(observer);
        }
        this.chessBoard.addObserver(this);

        this.chessBoard.initChessPieces();
    }

    /**
     * Executes the given move. The status of the board is returned after executing the move.
     * @param source Source field of the move
     * @param target Target field of the move
     * @throws ExcInvalidMove The given move is not allowed on the current board.
     */
    public ExtBoardState executeMove(Coordinates source, Coordinates target) throws ExcInvalidMove {
        this.getChessBoard().executeMove(source, target);
        EnumChessColor activeColor = this.getChessBoard().getActiveColor();
        EnumKingThreat kingThreat = this.getChessBoard().getKingThreat();
        return new ExtBoardState(activeColor, kingThreat);
    }

    /**
     * Returns all possible fields that are reachable by one move, based on the given coordinates
     */
    public List<Coordinates> findPossibleNewFields(Coordinates coordinatesSource) {
        return this.getChessBoard().getField(coordinatesSource).findPossibleNewFields();
    }

    /**
     * Returns all fields that are threatening the field given by the coordinates.
     */
    public List<Coordinates> findThreateningFields(Coordinates coordinatesTarget) {
        return this.getChessBoard().getField(coordinatesTarget).findThreateningFields();
    }

    /**
     * Revert all effects from the last move.
     * This method can be called several times in a row.
     */
    public void revertLastMove() {
        this.getChessBoard().revertLastMove();
    }

    private ChessBoard getChessBoard() {
        if (this.chessBoard != null) {
            return this.chessBoard;
        } else {
            throw new IllegalStateException("The chess board is not initialized - call startNewGame first.");
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ChessField) {
            ChessField changedChessField = (ChessField) arg;
            Coordinates coordinates = changedChessField.getCoordinates();
            EnumChessPiece piece = changedChessField.getPiece().getPieceType();
            EnumChessColor color = changedChessField.getPiece().getColor();
            ExtFieldUpdate fieldUpdate = new ExtFieldUpdate(coordinates, piece, color);
            System.out.println(changedChessField.getCoordinates().x() + ", " + changedChessField.getCoordinates().y());
            setChanged();
            notifyObservers();
        }
    }

}
