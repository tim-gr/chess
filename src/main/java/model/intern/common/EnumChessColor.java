package model.intern.common;

public enum EnumChessColor {

    BLACK("B"), WHITE("W");

    private final String shortName;

    EnumChessColor(String shortName) {
        this.shortName = shortName;
    }

    public EnumChessColor getOtherColor() {
        if (this == BLACK) {
            return WHITE;
        } else {
            return BLACK;
        }
    }

    @Override
    public String toString() {
        return this.shortName;
    }

}
