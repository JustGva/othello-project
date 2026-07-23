package model;

/**
 * Represents a move in the Othello game.
 * Each move has a row and column index.
 */
public class Move {
    private final int row;
    private final int col;

    /**
     * Assigns row and column numbers to a move.
     * @param row row of the move
     * @param col column of the move
     */
    public Move(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Gets the row index of the move.
     * @return row number
     */
    public int getRow() {
        return row;
    }

    /**
     * Gets the column index of the move.
     * @return column number
     */
    public int getCol() {
        return col;
    }
}
