package model;

import exceptions.NotAnIndexException;

/**
 * Board for the Othello game.
 */
public class Board {
    //-------------- COLORS FOR THE DISCS-----------------
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    /**
     * Dimension of the board.
     */
    public static final int DIM = 8;
    private static final String[] UI_ELEMENT = {"  ____ ____ ____ ____ ____ ____ ____ ____",
        "  ----+----+----+----+----+----+----+----",
        "    A    B    C    D    E    F    G    H  "};
    private static final String LINE = UI_ELEMENT[1];
    private static final String BORDER = UI_ELEMENT[0];
    private static final String LETTERS = UI_ELEMENT[2];

    /**
     * An array for all the cells of the board.
     */
    private Mark[][] fields;

    /**
     * Creates an empty board with 4 marks in the middle squares,
     * according to the rules.
     */
    public Board() {
        this.fields = new Mark[DIM][DIM];
        this.reset();
    }

    /**
     * Returns all the fields of the board.
     * @return
     */
    public Mark[][] getFields() {
        return fields;
    }

    /**
     * Creates a deep copy of the board.
     * @return copy of the board
     */
    public Board deepCopy() {
        Board boardCopy = new Board();
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                boardCopy.setField(i, j, this.getField(i, j));
            }
        }
        return boardCopy;
    }

    /**
     * Returns the index that will be used to communicate via the protocol
     * to other clients/servers.
     * @param row row of the cell
     * @param col column of the cell
     * @return numerical representation (index) of the cell
     */
    public int index(int row, int col) {
        return DIM * row + col;
    }

    /**
     * Converts the letter that user enters as a part of a move to a digit.
     * @param col the letter to be converted
     * @return the number that represents the letter
     */
    public int index(Character col) throws NotAnIndexException {
        int index;
        switch (col) {
            case 'A':
                index = 0;
                break;
            case 'B':
                index = 1;
                break;
            case 'C':
                index = 2;
                break;
            case 'D':
                index = 3;
                break;
            case 'E':
                index = 4;
                break;
            case 'F':
                index = 5;
                break;
            case 'G':
                index = 6;
                break;
            case 'H':
                index = 7;
                break;
            default:
                index = 99;
        }
        return index;
    }

    /**
     * Returns true of the (row,col) pair refers to a valid field on the board.
     *
     * @return true if 0 <= row < DIM && 0 <= col < DIM
     */
    public boolean isField(int row, int col) {
        return row >= 0 && row < DIM && col >= 0 && col < DIM;
    }

    /**
     * Returns the content of the field referred to by the (row,col) pair.
     *
     * @param row the row of the field
     * @param col the column of the field
     * @return the mark on the field
     */
    public Mark getField(int row, int col) {
        if (isField(row, col)) {
            return fields[row][col];
        } else {
            System.out.println(row + " " + col);
            throw new NotAnIndexException();
        }
    }

    /**
     * Returns true if the field in the given row and column is empty.
     * @param row the row of the field
     * @param col the column of the field
     * @return true if it is empty; false otherwise
     */
    public boolean isEmptyField(int row, int col) {
        if (isField(row, col)) {
            return fields[row][col] == Mark.EMPTY;
        } else {
            throw new NotAnIndexException();
        }
    }

    /**
     * Tests if the whole board is full.
     *
     * @return true if all fields are occupied; false otherwise
     */
    public boolean isFull() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                if (fields[i][j] == Mark.EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns a String representation of this board. In addition to the current
     * situation, the String also shows the numbering of the fields.
     *
     * @return the game situation as String
     */
    @Override
    public String toString() {
        String s = "";
        for (int i = 0; i < DIM; i++) {
            String row = "" + (i + 1) + "|"; //each row is constructed and indexed
            for (int j = 0; j < DIM; j++) {
                try {
                    //inserting the contents of the cells into each row
                    row += "  " + getField(i, j).toString().substring(0, 1).replace("E", " ")
                            .replace("B", ANSI_PURPLE + "@" + ANSI_RESET)
                            .replace("W", ANSI_YELLOW + "@" + ANSI_RESET) + " ";
                } catch (NotAnIndexException e) {
                    throw new RuntimeException(e);
                }
                if (j < DIM - 1) {
                    row = row + "|";
                }
            }
            s = s + row; //adding the rows together to create the full grid
            if (i < DIM - 1) {
                s = s + "\n" + LINE + "\n";
            }
            if (i == DIM - 1) {
                s = s + "\n" + BORDER + "\n" + LETTERS + "\n"; //inserting indexing for columns
            }
        }
        return s;
    }

    /**
     * Resets the board: makes it empty and sets middle 4 fields
     * to the appropriate marks.
     */
    public void reset() {
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                fields[i][j] = Mark.EMPTY;
            }
        }
        fields[DIM / 2 - 1][DIM / 2] = Mark.BLACK;
        fields[DIM / 2][DIM / 2 - 1] = Mark.BLACK;
        fields[DIM / 2 - 1][DIM / 2 - 1] = Mark.WHITE;
        fields[DIM / 2][DIM / 2] = Mark.WHITE;
    }

    public void setField(int row, int col, Mark m) {
        if (isField(row, col)) {
            this.fields[row][col] = m;
        }
    }
}
