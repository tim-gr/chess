package model.extern;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.intern.common.Coordinates;
import model.intern.exceptions.ExcInvalidMove;

import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.stream.Collectors;

public class ChessGame extends Observable implements Observer {

    private ChessBoard chessBoard;

    public void startNewGame() {
        startNewGame(null);
    }

    public void startNewGame(Observer observer) {
        if (this.chessBoard != null) {
            // Cleanup old observers
            this.chessBoard.deleteObservers();
        }

        this.chessBoard = new ChessBoard();

        if (observer != null) {
            this.addObserver(observer);
        }
        this.chessBoard.addObserver(this);

        this.chessBoard.initChessPieces();
    }

    public void executeMove(ExtCoordinates source, ExtCoordinates target) {
        try {
            this.getChessBoard().executeMove(source.getCoordinates(), target.getCoordinates());
        } catch (ExcInvalidMove e) {
            e.printStackTrace();
        }
    }

    public List<ExtCoordinates> findPossibleNewFields(ExtCoordinates coordinatesSource) {
        List<Coordinates> listCoordinates = this.getChessBoard().findPossibleNewFields(coordinatesSource.getCoordinates());
        return listCoordinates.stream()
                .map(coordinates -> new ExtCoordinates(coordinates.x(), coordinates.y()))
                .collect(Collectors.toList());
    }

    public List<ExtCoordinates> findThreateningFields(ExtCoordinates coordinatesSource) {
        List<Coordinates> listCoordinates = this.getChessBoard().findThreateningFields(coordinatesSource.getCoordinates());
        return listCoordinates.stream()
                .map(coordinates -> new ExtCoordinates(coordinates.x(), coordinates.y()))
                .collect(Collectors.toList());
    }

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
            System.out.println(changedChessField.getCoordinates().x() + ", " + changedChessField.getCoordinates().y());
        }
    }

}
