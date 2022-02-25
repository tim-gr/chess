package model.common;

/**
 * Enumeration of the available chess color BLACK and WHITE.
 */
public enum EnumChessColor {

    BLACK("B"), WHITE("W");

    private final String shortName;

    EnumChessColor(String shortName) {
        this.shortName = shortName;
    }

    /**
     * Returns the other color (BLACK -> WHITE / WHITE -> BLACK).
     */
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
