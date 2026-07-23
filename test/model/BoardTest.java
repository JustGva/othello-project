package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for testing the methods of the Board and Mark class.
 */
public class BoardTest {

    private Board board;
    private Board deepCopy;

    /**
     * Sets the initial board to use in the methods.
     */
    @BeforeEach
    public void setUp() {
        board = new Board();
    }

    @Test
    public void testGetFields() {
        int counter = 0;
        assertEquals(Board.DIM, board.getFields().length);
        for (Mark[] rows : board.getFields()) {
            for (Mark cell : rows) {
                counter++;
            }
        }
        assertEquals(Board.DIM * Board.DIM, counter);
    }

    /**
     * A test to check the creation of the board. When board is created, all the
     * marks have to be empty except for the middle four: Mark.WHITE at (3, 3)
     * and (4,4) and Mark.BLACK at (3,4) and (4,3).
     */
    @Test
    public void testSetUp() {
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                if (i == 3 && j == 3 || i == 4 && j == 4) {
                    assertEquals(Mark.WHITE, board.getField(i, j));
                } else if (i == 3 && j == 4 || i == 4 && j == 3) {
                    assertEquals(Mark.BLACK, board.getField(i, j));
                } else {
                    assertEquals(Mark.EMPTY, board.getField(i, j));
                }
            }
        }
    }

    /**
     * A test to check that the index methods returns the correct index
     * of the cell given the coordinates. All the cells of the board are tested.
     */
    @Test
    public void testIndex() {
        int index = 0;
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                assertEquals(index, board.index(i, j));
                index++;
            }
        }
    }

    /**
     * A test to check whether a correct column index is returned
     * to a corresponding letter. Letter A should return 0, B should return 1, etc.
     * Last letter is H, it should return 7.
     */
    @Test
    public void testLetterToColumnIndex() {
        char letter = 'A';
        for (int i = 0; i < Board.DIM; i++) {
            assertEquals(i, board.index(letter));
            char next = (char) (letter + 1); // getting next letter
            letter = next;
        }
    }

    /**
     * A test to check that isField() method given the coordinates works as intended.
     * The test ensures that field given the coordinates exists if and only if
     * row and column indexes are between 0 and 7, given the current dimensions of the board.
     */
    @Test
    public void testIsFieldRowCol() {
        assertFalse(board.isField(-1, -1));
        assertFalse(board.isField(-1, 0));
        assertFalse(board.isField(0, -1));
        assertTrue(board.isField(0, 0));
        assertTrue(board.isField(0, 1));
        assertTrue(board.isField(1, 0));
        assertTrue(board.isField(1, 0));
        assertTrue(board.isField(Board.DIM - 1, Board.DIM - 1));
        assertFalse(board.isField(Board.DIM, Board.DIM - 1));
        assertFalse(board.isField(Board.DIM - 1, Board.DIM));
        assertFalse(board.isField(Board.DIM, Board.DIM));
    }

    /**
     * A test to ensure that setField() and getField() methods work as expected.
     * setField() method takes the coordinates and the mark to be placed white getField() method
     * takes the coordinates only. After the mark is set on some coordinates, getField() should
     * return the correct mark that has been placed by the setField() method.
     */
    @Test
    public void testSetAndGetFieldIndex() {
        board.setField(0, 0, Mark.BLACK);
        assertEquals(Mark.BLACK, board.getField(0, 0));
        board.setField(Board.DIM - 1, Board.DIM - 1, Mark.WHITE);
        assertEquals(Mark.WHITE, board.getField(Board.DIM - 1, Board.DIM - 1));
        board.setField(5, 1, Mark.BLACK);
        assertEquals(Mark.BLACK, board.getField(5, 1));
    }

    /**
     * A test to check if the board is reset in the correct way. All the marks have to
     * be empty except for the middle four: Mark.WHITE at (3, 3)
     * and (4,4) and Mark.BLACK at (3,4) and (4,3). All previously placed marks have
     * to be reset to the beginning position.
     */
    @Test
    public void testReset() {
        board.setField(0, 1, Mark.WHITE);
        board.setField(4, 7, Mark.BLACK);
        board.reset();
        assertEquals(Mark.EMPTY, board.getField(0, 1));
        assertEquals(Mark.EMPTY, board.getField(4, 7));
        assertEquals(Mark.BLACK, board.getField(Board.DIM / 2 - 1, Board.DIM / 2));
        assertEquals(Mark.BLACK, board.getField(Board.DIM / 2, Board.DIM / 2 - 1));
        assertEquals(Mark.WHITE, board.getField(Board.DIM / 2, Board.DIM / 2));
        assertEquals(Mark.WHITE, board.getField(Board.DIM / 2 - 1, Board.DIM / 2 - 1));
        assertEquals(Mark.EMPTY, board.getField(6, 3));
    }

    /**
     * A test to ensure that deepCopy() method copies the board and
     * all its marks as it is supposed to. If a player(s) had put some marks on the board, after
     * the method is called all the placed marks should remain in the same cells.
     * All cells that were empty should remain empty after the method is called.
     */
    @Test
    public void testDeepCopy() {
        board.setField(0, 0, Mark.BLACK);
        board.setField(6, 7, Mark.WHITE);
        deepCopy = board.deepCopy();

        // Test if all fields are the same for both board and deepCopy
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                assertEquals(board.getField(i, j), deepCopy.getField(i, j));
            }
        }

        // Test if we change one board, the other one does not change
        deepCopy.setField(6, 7, Mark.BLACK);
        assertEquals(Mark.BLACK, deepCopy.getField(6, 7));
        assertEquals(Mark.WHITE, board.getField(6, 7));
    }

    /**
     * Test to check if the field by given coordinates is empty. For it to
     * be empty, the field must match Mark.EMPTY.
     */
    @Test
    public void testIsEmptyFieldIndex() {
        board.setField(1, 1, Mark.BLACK);
        assertFalse(board.isEmptyField(1, 1));
        board.setField(1, 1, Mark.EMPTY);
        assertTrue(board.isEmptyField(1, 1));
        assertTrue(board.isEmptyField(1, 0));
    }

    /**
     * Test to check if the board is full. The board is full when all the
     * spots in the board are either Mark.BLACK or Mark.WHITE. Full board has no
     * Mark.EMPTY fields.
     */
    @Test
    public void testIsFullBoard() {
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                board.setField(i, j, Mark.BLACK);
            }
        }
        assertTrue(board.isFull());
        board.setField(Board.DIM - 1, Board.DIM - 1, Mark.EMPTY); // make the board not full
        assertFalse(board.isFull()); // test if it is not full
        board.setField(Board.DIM - 1, Board.DIM - 1, Mark.BLACK); // make it full again
        assertTrue(board.isFull()); // ensure it is full
    }

    /**
     * Test to check if otherMark() function of class Mark works correctly. If we have
     * black mark, it should return white and vice versa. In case the mark is empty, otherMark()
     * should return Mark.EMPTY.
     */
    @Test
    public void testMark() {
        board.setField(0, 0, Mark.WHITE);
        board.setField(0, 1, Mark.BLACK);
        board.setField(0, 2, Mark.EMPTY);
        assertEquals(Mark.BLACK, board.getField(0, 0).otherMark());
        assertEquals(Mark.WHITE, board.getField(0, 1).otherMark());
        assertEquals(Mark.EMPTY, board.getField(0, 2).otherMark());
    }

}
