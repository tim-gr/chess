package model.intern.chessmove;

/**
 * All data of the result of a move validation.
 */
public class MoveValidationResult {

    private final EnumValidationResult validationResult;
    private Move subMove;

    public MoveValidationResult(boolean moveValid) {
        this.validationResult = moveValid ? EnumValidationResult.VALID : EnumValidationResult.INVALID;
    }

    public MoveValidationResult(Move subMove) {
        this.validationResult = EnumValidationResult.VALID_WITH_SUB_MOVE;
        this.subMove = subMove;
    }

    /**
     * Returns the result of this validation.
     */
    public EnumValidationResult getValidationResult() {
        return validationResult;
    }

    /**
     * Returns the sub move that was appended during the validation.
     * This is necessary for special moves like castling.
     */
    public Move getSubMove() {
        return subMove;
    }

    /**
     * Returns whether the validation showed a valid result.
     */
    public boolean isMoveValid() {
        return this.validationResult ==
                EnumValidationResult.VALID || this.validationResult == EnumValidationResult.VALID_WITH_SUB_MOVE;
    }

}
