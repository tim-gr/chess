package model.intern.chessmove;

import model.intern.chessboard.ChessBoard;
import model.intern.chessboard.ChessField;
import model.common.Coordinates;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class has the knowledge of how to create valid move paths.
 */
public class MovePathCreator {

    private static final int MIN_FIELD = 0;
    private static final int MAX_FIELD = 7;

    private static MovePathCreator movePathCreator;
    private MovePathCreator(){}

    public static MovePathCreator getInstance() {
        if (movePathCreator == null) {
            movePathCreator = new MovePathCreator();
        }
        return movePathCreator;
    }

    /**
     * Returns a move path with all reachable fields on it,
     * based on the source field, the move tendency (deltaX, deltaY) and the move direction.
     */
    MovePath createMovePathStraight(ChessBoard board, Coordinates source, int deltaX, int deltaY, EnumMovePath direction) {

        if (direction == EnumMovePath.JUMP) {
            throw new IllegalArgumentException("A jump movement is not a straight path.");
        }

        if (deltaX == 0 && deltaY == 0) {
            throw new IllegalArgumentException("A move path needs a tendency given by deltaX and deltaY. At least one value has to be != 0.");
        }

        // The implementation needs a tendency in which the move path is going.
        // Therefore, deltaX and deltaY are always transformed into -1 and +1, when the given value is != 0.
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

    /**
     * Returns a move path of type jump with one possible field.
     * Both deltaX and deltaY have to be != 0.
     */
    MovePath createMovePathJump(ChessBoard board, Coordinates source, int deltaX, int deltaY) {

        if (deltaX == 0 || deltaY == 0) {
            throw new IllegalArgumentException("A jump move path needs both deltaX and deltaY being != 0.");
        }

        MovePath movePath = new MovePath(EnumMovePath.JUMP);

        int x = source.x() + deltaX;
        int y = source.y() + deltaY;

        if (x >= MIN_FIELD && x <= MAX_FIELD && y >= MIN_FIELD && y <= MAX_FIELD) {
            ChessField currentField = board.getField(x, y);
            movePath.addPossibleField(currentField);
        }

        return movePath;
    }

    /**
     * Returns all possible move paths from the source field (given by coordinates).
     * When the piece on the field is supposed to be considered as well, it has to be given as true.
     */
    public List<MovePath> findAllMovePaths(ChessBoard board, ChessField field, boolean considerPiece) {

        List<EnumMovePath> allowedMovePaths = new ArrayList<>();
        if (considerPiece && field.hasPiece()) {
            allowedMovePaths = field.getPiece().getValidMovePaths();
        }
        if (!considerPiece) {
            allowedMovePaths = Arrays.asList(EnumMovePath.values());
        }

        List<MovePath> movePaths = new ArrayList<>();
        if (allowedMovePaths.contains(EnumMovePath.LINEAR)) {
            movePaths.addAll(findAllLinearMovePaths(board, field.getCoordinates()));
        }
        if (allowedMovePaths.contains(EnumMovePath.DIAGONAL)) {
            movePaths.addAll(findAllDiagonalMovePaths(board, field.getCoordinates()));
        }
        if (allowedMovePaths.contains(EnumMovePath.JUMP)) {
            movePaths.addAll(findAllJumpMovePaths(board, field.getCoordinates()));
        }
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
