package model.common;

/**
 * Coordinates, consisting of (x|y).
 */
public class Coordinates {

    private final int x;
    private final int y;

    public Coordinates(int x, int y) {
        if (x >= 0 && x < 8 && y >= 0 && y < 8) {
            this.x = x;
            this.y = y;
        } else {
            throw new IllegalArgumentException("x and y have to been between 0 and 7 (both inclusive).");
        }
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Coordinates) {
            Coordinates otherCoordinates = (Coordinates) obj;
            return otherCoordinates.x() == this.x() && otherCoordinates.y() == this.y();
        }
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
