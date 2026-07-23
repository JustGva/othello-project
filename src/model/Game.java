package model;

import player.Player;

import java.util.List;

public interface Game {


    /**
     * Returns the player that currently has no move.
     * @return player
     */
    Player getOtherPlayer();

    /**
     * Checks if the game is over.
     * Happens when both the current player and the other player don't have any moves to do.
     * @return true if game is over
     */
    boolean isGameOver();

    /**
     * Query whose turn it is.
     * @return the player whose turn it is
     */
    Player getTurn();

    /**
     * Gets the winner of that match, or null if it's a draw.
     * @return the winner, or null if draw
     */
    Player getWinner();

    /**
     * Gets the loser of that match or null if it's a draw.
     * @return the loser, or null if draw
     */
    Player getLoser();

    /**
     * Creates a list of valid moves the player can do at the beginning of their turn.
     * First checks for cells that are adjacent to the currently placed disks and then invokes
     * boolean methods to check if each cell has a direction in which it can flip.
     * If it can then this cell is added to the list.
     * @return a list of valid moves
     */
    List<Move> getValidMoves(Player player);

    /**
     * Checks whether a move declared by the player is contained in their validMoves.
     * @param move to be checked
     * @return true if it's in the list, false if it isn't
     */
    boolean isValidMove(Move move, Player player);

    /**
     * Assuming it's a valid move, perform it on the board.
     * @param move the move to do
     */
    void doMove(Move move, Player player);

    //----------------- checkers for valid moves ----------------------

    /**
     * Checks all the disks in the same column to the top of the
     * disk of this move and returns true if a move can be made because
     * the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) and there exists a disk of the same color (white)
     * further in the top so the move can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean verticalTop(Move move, Player player);

    /**
     * Checks all the disks in the same column to the bottom of the
     * disk of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) and there exists a disk of the same color (white)
     * further in the bottom so the move can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean verticalBot(Move move, Player player);

    /**
     * Checks all the disks in the same row to the right of the disk of this
     * move and returns true if a move can be made because the disk (for example, white)
     * is bordering the opposite color disk (in this case, black) and there exists
     * a disk of the same color (white) further in the right so the move can be made.
     * Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean horizontalRight(Move move, Player player);

    /**
     * Checks all the disks in the same row to the left of the disk of
     * this move and returns true if a move can be made because the disk
     * (for example, white) is bordering the opposite color disk (in this case, black)
     * and there exists a disk of the same color (white)
     * further in the left so the move can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean horizontalLeft(Move move, Player player);

    /**
     * Checks all the disks in the same diagonal (bottom-left to top-right) going up from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner and there
     * exists a disk of the same color (white) going up the diagonal so the
     * move can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean diagTopRight(Move move, Player player);

    /**
     * Checks all the disks in the same diagonal (bottom-right to top-left) going up from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner and there
     * exists a disk of the same color (white) going up the diagonal so the move
     * can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean diagTopLeft(Move move, Player player);

    /**
     * Checks all the disks in the same diagonal (top-left to bottom-right) going down from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner and
     * there exists a disk of the same color (white)
     * going down the diagonal so the move can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean diagBotRight(Move move, Player player);

    /**
     * Checks all the disks in the same diagonal (top-right to bottom-left) going down from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner and there
     * exists a disk of the same color (white) going down the diagonal
     * so the move can be made. Returns false otherwise.
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    boolean diagBotLeft(Move move, Player player);

    /**
     * Converts the letter that user enters as a part of a move to a digit.
     * @param c the letter to be converted
     * @return the number that represents the letter
     */
    int index(Character c);

    /**
     * Changes the turn of the player.
     */
    public void changeTurn();

    /**
     * Creates a copy of the game.
     * @return game
     */
    OthelloGame deepCopy();

    /**
     * Returns the board of a game.
     * @return board
     */
    Board getBoard();

    /**
     * Inverts the digit into a corresponding letter.
     * @param index index to convert
     * @return letter as a String
     */
    String invertIndex(int index);

    /**
     * Counts the amount of white disks on the board.
     * @return amount of disks
     */
    int countWhiteDisks();

    /**
     * Counts the amount of black disks on the board.
     * @return amount of disks
     */
    int countBlackDisks();
}
