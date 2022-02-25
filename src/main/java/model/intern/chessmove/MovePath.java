package model.intern.chessmove;

import model.intern.chessboard.ChessField;

import java.util.ArrayList;
import java.util.List;

/**
 * A move path consists of all chess fields on a path on a chess field.
 * A path can be linear, diagonal and a jump.
 * Linear and diagonal paths end at the border of the chess board or at a field with a piece on it.
 * A jump path always ends on a field reachable by an "L-Jump" (Knight movement rule)
 */
public class MovePath {

    private final EnumMovePath direction;
    private final List<ChessField> fieldsOnPath;

    MovePath(EnumMovePath direction) {
        this.direction = direction;
        this.fieldsOnPath = new ArrayList<>();
    }

    /**
     * Add a field that is on this move path and reachable.
     */
    void addPossibleField(ChessField possibleField) {
        this.fieldsOnPath.add(0, possibleField);
    }

    /**
     * Returns the last field of this move path.
     * This is the only field of this move path where a piece can be on.
     */
    public ChessField getLastFieldOfPath() {
        if (this.fieldsOnPath.isEmpty()) {
            return null;
        } else {
            return this.fieldsOnPath.get(0);
        }
    }

    /**
     * Returns the direction of this move path (Linear, Diagonal or Jump).
     */
    public EnumMovePath getDirection() {
        return this.direction;
    }

    /**
     * Returns all fields that are on this move path.
     */
    public List<ChessField> getFieldsOnPath() {
        return this.fieldsOnPath;
    }

}
