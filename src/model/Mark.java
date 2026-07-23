package model;

/**
 * Represents a mark in the Othello game. There are three possible values: Mark.BLACK,
 * Mark.WHITE and Mark.EMPTY.
 */
public enum Mark {

    BLACK, WHITE, EMPTY;

    /**
     * Returns the other mark.
     * @return the other mark if this mark is not EMPTY or EMPTY
     */
    //@ ensures this == BLACK ==> \result == WHITE && this == WHITE ==> \result == BLACK;
    //@ ensures (this != BLACK && this != WHITE) ==> \result == EMPTY;
    public Mark otherMark() {
        if (this == BLACK) {
            return WHITE;
        } else if (this == WHITE) {
            return BLACK;
        } else {
            return EMPTY;
        }
    }
}
