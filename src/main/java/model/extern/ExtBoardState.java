package model.extern;

import model.intern.common.EnumChessColor;
import model.intern.common.EnumKingThreat;

public class ExtBoardState {

    private final EnumChessColor activeColor;
    private final EnumKingThreat kingThreat;

    ExtBoardState(EnumChessColor activeColor, EnumKingThreat kingThreat) {
        this.activeColor = activeColor;
        this.kingThreat = kingThreat;
    }

    public EnumChessColor getActiveColor() {
        return this.activeColor;
    }

    public EnumKingThreat getKingThreat() {
        return this.kingThreat;
    }
}
