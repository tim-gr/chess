package model.intern.chessboard;

import model.intern.chessmove.Move;
import model.intern.common.EnumChessColor;
import model.intern.common.EnumKingThreat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoardState {

    private EnumChessColor activeColor;
    private final Map<EnumChessColor, ChessField> fieldsOfKings;
    private EnumKingThreat kingThreat;
    private final List<Move> moves;

    public ChessBoardState() {
        // The player with the white pieces starts a chess game.
        this.activeColor = EnumChessColor.WHITE;
        this.fieldsOfKings = new HashMap<>();
        this.kingThreat = EnumKingThreat.NO_THREAT;
        this.moves = new ArrayList<>();
    }


    boolean hasTurn(EnumChessColor color) {
        return this.activeColor == color;
    }

    void setActiveColor(EnumChessColor activeColor) {
        this.activeColor = activeColor;
    }

    void changePlayer() {
        this.activeColor = this.activeColor.getOtherColor();
    }

    EnumChessColor getActiveColor() {
        return this.activeColor;
    }

    void setKingThreat(EnumKingThreat kingThreat) {
        this.kingThreat = kingThreat;
    }

    void changeFieldOfKing(ChessField fieldTarget) {
        changeFieldOfKing(this.activeColor, fieldTarget);
    }

    void changeFieldOfKing(EnumChessColor color, ChessField fieldTarget) {
        this.fieldsOfKings.put(color, fieldTarget);
    }

    ChessField getFieldOfKing() {
        return this.fieldsOfKings.get(this.activeColor);
    }

    public Move getLastMove() {
        if (this.moves.isEmpty()) {
            return null;
        }
        return this.moves.get(this.moves.size() - 1);
    }

    public void addMoveToHistory(Move move) {
        this.moves.add(move);
    }

    Move removeMoveFromHistory() {
        if (this.moves.isEmpty()) {
            return null;
        }
        return this.moves.remove(this.moves.size()- 1);
    }

}
