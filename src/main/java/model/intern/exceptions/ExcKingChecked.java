package model.intern.exceptions;

import model.intern.common.EnumKingThreat;

public class ExcKingChecked extends ExcInvalidMove {

    private final EnumKingThreat kingThreat;

    public ExcKingChecked(EnumKingThreat kingThreat) {
        super();
        this.kingThreat = kingThreat;
    }

    public EnumKingThreat getKingThreat() {
        return kingThreat;
    }

}
