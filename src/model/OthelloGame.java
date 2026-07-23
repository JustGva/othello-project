package model;

import exceptions.NotAnIndexException;
import player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the Othello game.
 */
public class OthelloGame implements Game {

    Board gameBoard;
    Player player1;
    Player player2;
    Player turnOf;

    public static int flips;

    /**
     * Takes two players to create a new Othello game
     * creates a new board and sets the starting discs.
     *
     * @param player1
     * @param player2
     */
    public OthelloGame(Player player1, Player player2) {
        this.gameBoard = new Board();
        this.player1 = player1;
        this.player2 = player2;
        turnOf = player1;
    }

    /**
     * Constructor for deepCopy to be used by computer players to determine next move.
     *
     * @param p1     player one
     * @param p2     player two
     * @param board  game board
     * @param turnOf player who currently has the turn
     */
    public OthelloGame(Player p1, Player p2, Board board, Player turnOf) {
        this.gameBoard = board;
        this.player1 = p1;
        this.player2 = p2;
        this.turnOf = turnOf;
    }

    public OthelloGame deepCopy() {
        return new OthelloGame(this.player1, this.player2, this.gameBoard.deepCopy(), this.turnOf);
    }

    public Board getBoard() {
        return gameBoard;
    }

    /**
     * Returns the player that currently does not have a move.
     *
     * @return player2 if player1 is the current player, and vice versa.
     */
    @Override
    public Player getOtherPlayer() {
        if (turnOf == player1) {
            return player2;
        } else {
            return player1;
        }
    }

    /**
     * Checks if the game is over. The game is over if both
     * of the players have no valid moves or the board is full.
     *
     * @return true if the game is over; false otherwise.
     */
    @Override
    public boolean isGameOver() {
        if (gameBoard.isFull() || getValidMoves(turnOf) == null
                && getValidMoves(getOtherPlayer()) == null) {
            return true;
        }
        return false;
    }

    /**
     * Returns the player that currently has a turn.
     *
     * @return player
     */
    @Override
    public Player getTurn() {
        return turnOf;
    }

    /**
     * Returns the winner of the game. The winner of the game
     * is the player that has more disks when the game has ended.
     *
     * @return player that won the game
     */
    @Override
    public Player getWinner() {
        int blackCounter = countBlackDisks(); //for counting black discs
        int whiteCounter = countWhiteDisks(); //for counting white discs
        //returns the player with most discs or null if they are equal
        if (blackCounter > whiteCounter) {
            return player1;
        } else if (blackCounter < whiteCounter) {
            return player2;
        } else {
            return null;
        }
    }

    /**
     * returns the loser of the game, the loser
     * if the player that has fewer disks when the game has ended.
     *
     * @return player that lost the game
     */
    @Override
    public Player getLoser() {
        if (getWinner() != null) {
            Player winner = getWinner();
            if (winner.equals(player1)) {
                return player2;
            } else {
                return player1;
            }
        }
        return null;
    }

    /**
     * Counts black disks of the first player when the method is called.
     *
     * @return number of black disks
     */
    public int countBlackDisks() {
        int blackCounter = 0;
        for (Mark[] rows : gameBoard.getFields()) {
            for (Mark cell : rows) {
                if (cell.equals(Mark.BLACK)) {
                    blackCounter++;
                }
            }
        }
        return blackCounter;
    }

    /**
     * Counts white disks of the second player when the method is called.
     *
     * @return number of white disks
     */
    public int countWhiteDisks() {
        int whiteCounter = 0;
        for (Mark[] rows : gameBoard.getFields()) {
            for (Mark cell : rows) {
                if (cell.equals(Mark.WHITE)) {
                    whiteCounter++;
                }
            }
        }
        return whiteCounter;
    }

    /**
     * Returns a list of moves that are possible for the current player to take.
     *
     * @param player a player whose turns the method returns
     * @return list of possible moves
     */
    @Override
    public List<Move> getValidMoves(Player player) {
        List<Move> result = new ArrayList<>();
        //iterate all cells in the board
        for (int i = 0; i < Board.DIM; i++) {
            for (int j = 0; j < Board.DIM; j++) {
                //for every empty cell we will check if there is a valid move
                if (gameBoard.isEmptyField(i, j)) {
                    Move move = new Move(i, j);
                    if (verticalBot(move, player) || verticalTop(move, player)
                            || horizontalLeft(move, player) || horizontalRight(move, player)
                            || diagBotLeft(move, player)
                            || diagTopLeft(move, player)
                            || diagBotRight(move, player)
                            || diagTopRight(move, player)) {
                        //checks all directions for the move and adds it to the list if it's valid
                        result.add(move);
                    }
                }
            }
        }
        if (result.isEmpty()) { //returns null if no valid moves were added
            return null;
        } else  {
            return result;
        }
    }

    /**
     * Checks if a given move is a valid one.
     *
     * @param move to be checked
     * @return true if this move can be taken; false otherwise.
     */
    @Override
    public boolean isValidMove(Move move, Player player) {
        List<Move> validMoves = getValidMoves(player);
        if (validMoves != null) {
            for (Move m : validMoves) {
                if (move.getRow() == m.getRow() && move.getCol() == m.getCol()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tries to make a move on the board. If the move is valid, the disk is
     * placed (for example, white) on the board and the disks of the opposite
     * color (black) that are between the current disks (white disks)
     * are flipped. The move is not done if it is not a valid one.
     *
     * @param move the move to do
     */
    @Override
    public void doMove(Move move, Player player) {
        if (isValidMove(move, player)) {
            gameBoard.setField(move.getRow(), move.getCol(), player.getMark()); //places the disc
            flip(move, player); //flips opponent's discs
            changeTurn(); // changes the turn to other player
        }
    }

    /**
     * Change turn of the player to the next one.
     * This happens either when the current player does a move
     * or a current player has no moves, but the other player
     * can do a move.
     */
    public void changeTurn() {
        turnOf = getOtherPlayer();
    }


    /**
     * Checks all the disks in the same column to the top of the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) and there exists a disk of the same color (white)
     * further in the top so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean verticalTop(Move move, Player player) {
        //if row = 2, we get 1 in toCheck
        int toCheck = move.getRow() - 1; // row nr
        int col = move.getCol(); // col nr
        //If row == 0, we go out of bounds so nothing to check
        if (!gameBoard.isField(toCheck, col)) {
            return false;
        }
        //Check if closest mark is the opposite
        if (gameBoard.getField(toCheck, col) == player.getMark().otherMark()) {
            toCheck--; // we checked the closest one
            // Check remaining ones (if any)
            for (int i = toCheck; i >= 0; i--) {
                if (gameBoard.getField(i, col) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(i, col) == player.getMark()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks all the disks in the same column to the bottom of the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) and there exists a disk of the same color (white)
     * further in the bottom so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean verticalBot(Move move, Player player) {
        //If row = 4, we get 5 in toCheck
        int toCheck = move.getRow() + 1; // row nr
        int col = move.getCol(); // col nr

        // If row == 7, we go out of bounds so nothing to check
        if (!gameBoard.isField(toCheck, col)) {
            return false;
        }
        //Check if closest mark is the opposite
        if (gameBoard.getField(toCheck, col) == player.getMark().otherMark()) {
            toCheck++; // we checked the closest one
            for (int i = toCheck; i < Board.DIM; i++) {
                if (gameBoard.getField(i, col) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(i, col) == player.getMark()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks all the disks in the same row to the right of the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) and there exists a disk of the same color (white)
     * further in the right so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean horizontalRight(Move move, Player player) {
        int toCheck = move.getCol() + 1;
        int row = move.getRow();

        if (!gameBoard.isField(row, toCheck)) {
            return false;
        }
        if (gameBoard.getField(row, toCheck) == player.getMark().otherMark()) {
            toCheck++;
            for (int i = toCheck; i < Board.DIM; i++) {
                if (gameBoard.getField(row, i) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(row, i) == player.getMark()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks all the disks in the same row to the left
     * of the disk of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) and there exists a disk of the same color (white)
     * further in the left so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean horizontalLeft(Move move, Player player) {
        int toCheck = move.getCol() - 1;
        int row = move.getRow();

        if (!gameBoard.isField(row, toCheck)) {
            return false;
        }
        if (gameBoard.getField(row, toCheck) == player.getMark().otherMark()) {
            toCheck--;
            for (int i = toCheck; i >= 0; i--) {
                if (gameBoard.getField(row, i) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(row, i) == player.getMark()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks all the disks in the same diagonal (bottom-left to top-right)
     * going up from the disk of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner and
     * there exists a disk of the same color (white)
     * going up the diagonal so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean diagTopRight(Move move, Player player) {
        int rowToCheck = move.getRow() - 1;
        int colToCheck = move.getCol() + 1;

        if (!gameBoard.isField(rowToCheck, colToCheck)) {
            return false;
        }
        if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark().otherMark()) {
            rowToCheck--;
            colToCheck++;

            while (colToCheck < Board.DIM && rowToCheck >= 0) {
                if (gameBoard.getField(rowToCheck, colToCheck) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark()) {
                    return true;
                }
                colToCheck++;
                rowToCheck--;
            }
        }
        return false;
    }

    /**
     * Checks all the disks in the same diagonal (bottom-right to top-left) going up from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner
     * and there exists a disk of the same color (white)
     * going up the diagonal so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean diagTopLeft(Move move, Player player) {
        int rowToCheck = move.getRow() - 1;
        int colToCheck = move.getCol() - 1;

        if (!gameBoard.isField(rowToCheck, colToCheck)) {
            return false;
        }
        if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark().otherMark()) {
            rowToCheck--;
            colToCheck--;
            while (colToCheck >= 0 && rowToCheck >= 0) {
                if (gameBoard.getField(rowToCheck, colToCheck) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark()) {
                    return true;
                }
                colToCheck--;
                rowToCheck--;
            }
        }
        return false;
    }

    /**
     * Checks all the disks in the same diagonal (top-left to bottom-right) going down from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the
     * corner and there exists a disk of the same color (white)
     * going down the diagonal so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean diagBotRight(Move move, Player player) {
        int rowToCheck = move.getRow() + 1;
        int colToCheck = move.getCol() + 1;

        // Check if there is anything to check
        if (!gameBoard.isField(rowToCheck, colToCheck)) {
            return false;
        }
        if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark().otherMark()) {
            rowToCheck++;
            colToCheck++;
            while (colToCheck < Board.DIM && rowToCheck < Board.DIM) {
                if (gameBoard.getField(rowToCheck, colToCheck) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark()) {
                    return true;
                }
                rowToCheck++;
                colToCheck++;
            }
            return false;
        }
        return false;
    }

    /**
     * Checks all the disks in the same diagonal (top-right to bottom-left) going down from the disk
     * of this move and returns true if a move
     * can be made because the disk (for example, white) is bordering the
     * opposite color disk (in this case, black) by the corner
     * and there exists a disk of the same color (white)
     * going down the diagonal so the move can be made. Returns false otherwise.
     *
     * @param move move to be checked
     * @return true if a move is legit; false otherwise.
     */
    @Override
    public boolean diagBotLeft(Move move, Player player) {
        int rowToCheck = move.getRow() + 1;
        int colToCheck = move.getCol() - 1;

        if (!gameBoard.isField(rowToCheck, colToCheck)) {
            return false;
        }
        if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark().otherMark()) {
            rowToCheck++;
            colToCheck--;

            while (colToCheck >= 0 && rowToCheck < Board.DIM) {
                if (gameBoard.getField(rowToCheck, colToCheck) == Mark.EMPTY) {
                    return false;
                }
                if (gameBoard.getField(rowToCheck, colToCheck) == player.getMark()) {
                    return true;
                }
                rowToCheck++;
                colToCheck--;
            }
        }
        return false;
    }

    /**
     * Converts the letter that user enters as a part of a move to a digit.
     *
     * @param c the letter to be converted
     * @return the number that represents the letter
     */
    @Override
    public int index(Character c) {
        return getBoard().index(c);
    }

    /**
     * Inverts a given index to it's corresponding letter on the board.
     *
     * @param index
     * @return the letter of the column
     */
    public String invertIndex(int index) {
        String result;
        switch (index) {
            case 0:
                result = "A";
                break;
            case 1:
                result = "B";
                break;
            case 2:
                result = "C";
                break;
            case 3:
                result = "D";
                break;
            case 4:
                result = "E";
                break;
            case 5:
                result = "F";
                break;
            case 6:
                result = "G";
                break;
            case 7:
                result = "H";
                break;
            default:
                throw new NotAnIndexException();
        }
        return result;
    }

    /**
     * Calls board to string for printing the current state of the board.
     * and adds the current player
     *
     * @return textual representation of the board
     */
    @Override
    public String toString() {
        return gameBoard.toString() + "\n" + "It's " + turnOf.getName() + "'s turn!\n";
    }

    /**
     * Replaces the opponent's discs with current player discs based on the move played.
     *
     * @param move
     */
    public void flip(Move move, Player player) {
        flips = 0;
        if (horizontalRight(move, player)) {
            int row = move.getRow();
            int col = move.getCol() + 1;
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                col++;
                flips++;
            }
        }
        if (horizontalLeft(move, player)) {
            int row = move.getRow();
            int col = move.getCol() - 1;
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                col--;
                flips++;
            }
        }
        if (verticalTop(move, player)) {
            int row = move.getRow() - 1;
            int col = move.getCol();
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                row--;
                flips++;
            }
        }
        if (verticalBot(move, player)) {
            int row = move.getRow() + 1;
            int col = move.getCol();
            while (gameBoard.getField(row, col) != turnOf.getMark()) {
                gameBoard.setField(row, col, turnOf.getMark());
                row++;
                flips++;
            }
        }
        if (diagTopRight(move, player)) {
            int row = move.getRow() - 1;
            int col = move.getCol() + 1;
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                row--;
                col++;
                flips++;
            }
        }
        if (diagBotRight(move, player)) {
            int row = move.getRow() + 1;
            int col = move.getCol() + 1;
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                row++;
                col++;
                flips++;
            }
        }
        if (diagBotLeft(move, player)) {
            int row = move.getRow() + 1;
            int col = move.getCol() - 1;
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                row++;
                col--;
                flips++;
            }
        }
        if (diagTopLeft(move, player)) {
            int row = move.getRow() - 1;
            int col = move.getCol() - 1;
            while (gameBoard.getField(row, col) != player.getMark()) {
                gameBoard.setField(row, col, player.getMark());
                row--;
                col--;
                flips++;
            }
        }
    }

}
