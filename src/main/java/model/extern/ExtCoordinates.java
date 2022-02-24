package model.extern;

import model.intern.common.Coordinates;

public class ExtCoordinates {

    private final Coordinates coordinates;

    public ExtCoordinates(int x, int y) {
        this.coordinates = new Coordinates(x, y);
    }

    Coordinates getCoordinates() {
        return this.coordinates;
    }
}
