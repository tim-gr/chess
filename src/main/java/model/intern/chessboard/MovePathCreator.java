package model.intern.chessboard;

import model.intern.common.Coordinates;
import model.intern.common.EnumMovePath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MovePathCreator {

    private static final int MIN_FIELD = 0;
    private static final int MAX_FIELD = 7;

    private static MovePathCreator movePathCreator;
    private MovePathCreator(){}

    static MovePathCreator getInstance() {
        if (movePathCreator == null) {
            movePathCreator = new MovePathCreator();
        }
        return movePathCreator;
    }

    MovePath createMovePathStraight(ChessBoard board, Coordinates source, int deltaX, int deltaY, EnumMovePath direction) {

        // deltaX and deltaY need to be -1 or +1.
        // To make it easier for the caller, higher values can be given. However, only the tendency is considered.
        deltaX = deltaX == 0 ? deltaX : deltaX / Math.abs(deltaX);
        deltaY = deltaY == 0 ? deltaY : deltaY / Math.abs(deltaY);

        MovePath movePath = new MovePath(direction);

        int x = source.x() + deltaX;
        int y = source.y() + deltaY;

        while (x >= MIN_FIELD && x <= MAX_FIELD && y >= MIN_FIELD && y <= MAX_FIELD) {
            ChessField currentField = board.getField(x, y);
            movePath.addPossibleField(currentField);

            if (currentField.hasPiece()) {
                break;
            }
            x += deltaX;
            y += deltaY;
        }

        return movePath;
    }

    MovePath createMovePathJump(ChessBoard board, Coordinates source, int deltaX, int deltaY) {
        MovePath movePath = new MovePath(EnumMovePath.JUMP);

        int x = source.x() + deltaX;
        int y = source.y() + deltaY;

        if (x >= MIN_FIELD && x <= MAX_FIELD && y >= MIN_FIELD && y <= MAX_FIELD) {
            ChessField currentField = board.getField(x, y);
            movePath.addPossibleField(currentField);
        }

        return movePath;
    }

    List<MovePath> findAllMovePaths(ChessBoard board, Coordinates source) {
        List<MovePath> movePaths = new ArrayList<>();
        movePaths.addAll(findAllLinearMovePaths(board, source));
        movePaths.addAll(findAllDiagonalMovePaths(board, source));
        movePaths.addAll(findAllJumpMovePaths(board, source));
        return movePaths;
    }

    private List<MovePath> findAllLinearMovePaths(ChessBoard board, Coordinates source) {
        MovePath verticalPositive = createMovePathStraight(board, source, 0, 1, EnumMovePath.LINEAR);
        MovePath verticalNegative = createMovePathStraight(board, source, 0, -1, EnumMovePath.LINEAR);
        MovePath horizontalPositive = createMovePathStraight(board, source, 1, 0, EnumMovePath.LINEAR);
        MovePath horizontalNegative = createMovePathStraight(board, source, -1, 0, EnumMovePath.LINEAR);
        return Arrays.asList(verticalPositive, verticalNegative, horizontalPositive, horizontalNegative);
    }

    private List<MovePath> findAllDiagonalMovePaths(ChessBoard board, Coordinates source) {
        MovePath topRight = createMovePathStraight(board, source, 1, 1, EnumMovePath.DIAGONAL);
        MovePath topLeft = createMovePathStraight(board, source, -1, 1, EnumMovePath.DIAGONAL);
        MovePath bottomRight = createMovePathStraight(board, source, 1, -1, EnumMovePath.DIAGONAL);
        MovePath bottomLeft = createMovePathStraight(board, source, -1, -1, EnumMovePath.DIAGONAL);
        return Arrays.asList(topRight, topLeft, bottomRight, bottomLeft);
    }

    private List<MovePath> findAllJumpMovePaths(ChessBoard board, Coordinates source) {
        return Arrays.asList(
                createMovePathJump(board, source,-2, 1),
                createMovePathJump(board, source,-1, 2),
                createMovePathJump(board, source,1, 2),
                createMovePathJump(board, source,2, 1),
                createMovePathJump(board, source,2, -1),
                createMovePathJump(board, source,1, -2),
                createMovePathJump(board, source,-1, -2),
                createMovePathJump(board, source,-2, -1)
        );
    }

}
