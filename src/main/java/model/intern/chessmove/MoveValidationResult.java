package model.intern.chessmove;

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

    public EnumValidationResult getValidationResult() {
        return validationResult;
    }

    public Move getSubMove() {
        return subMove;
    }

    public boolean isMoveValid() {
        return this.validationResult ==
                EnumValidationResult.VALID || this.validationResult == EnumValidationResult.VALID_WITH_SUB_MOVE;
    }

}
