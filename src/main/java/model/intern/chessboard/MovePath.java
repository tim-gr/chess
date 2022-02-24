package model.intern.chessboard;

import model.intern.common.EnumMovePath;

import java.util.ArrayList;
import java.util.List;

public class MovePath {

    private final EnumMovePath direction;
    private final List<ChessField> fieldsOnPath;

    MovePath(EnumMovePath direction) {
        this.direction = direction;
        this.fieldsOnPath = new ArrayList<>();
    }

    void addPossibleField(ChessField possibleField) {
        this.fieldsOnPath.add(0, possibleField);
    }

    ChessField getLastFieldOfPath() {
        if (this.fieldsOnPath.isEmpty()) {
            return null;
        } else {
            return this.fieldsOnPath.get(0);
        }
    }

    public EnumMovePath getDirection() {
        return this.direction;
    }

    public List<ChessField> getFieldsOnPath() {
        return this.fieldsOnPath;
    }

}
