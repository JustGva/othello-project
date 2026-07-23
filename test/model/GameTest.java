package model;

import AI.Naive;
import AI.Smart;
import org.junit.jupiter.api.Test;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Test class for testing the methods of the OthelloGame class, testing
 * the intelligence of the AI and testing game logic.
 */
public class GameTest {

    /**
     * A test to ensure that getBoard() method does not return null when a
     * game has been created. After making several moves, it should be able
     * to return board again.
     */
    @Test
    public void testGetBoard() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Naive(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        assertNotEquals(null, game.getBoard());
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(2, 2), p2);
        assertNotEquals(null, game.getBoard());
    }

    /**
     * A test to ensure that getTurn(), getOtherPlayer() and changeTurn()
     * methods work. Method getTurn() should get the player that currently
     * has to make a move, getOtherPlayer() returns the other player that does
     * not have a turn at the moment, changeTurn() provides the turn to another player.
     * This test ensures that the game logic part where switching the turns of the players
     * and checking which player has a turn and which does not works as expected.
     * This test also checks the turns when the game itself is player. After making a move,
     * turn is changed automatically.
     */
    @Test
    public void testTurnsOfThePlayers() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Naive(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        assertEquals(p1, game.getTurn());
        assertEquals(p2, game.getOtherPlayer());
        game.changeTurn();
        assertEquals(p2, game.getTurn());
        assertEquals(p1, game.getOtherPlayer());
        game.changeTurn();
        assertEquals(p1, game.getTurn());
        assertEquals(p2, game.getOtherPlayer());
        /* Ensure that after making a move turn of the
           player changes */
        game.doMove(new Move(2, 3), p1);
        assertEquals(p2, game.getTurn());
        assertEquals(p1, game.getOtherPlayer());
        game.doMove(new Move(2, 2), p2);
        assertEquals(p1, game.getTurn());
        assertEquals(p2, game.getOtherPlayer());
    }

    /**
     * A test which ensures that both black and white disks are counted
     * correctly during the game process. Methods getBlackDisks() and getWhiteDisks()
     * should work in any stage of the game.
     */
    @Test
    public void testCountBlackAndWhiteDisks() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Naive(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        assertEquals(2, game.countBlackDisks());
        assertEquals(2, game.countWhiteDisks());
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(3, 2), p1);
        game.doMove(new Move(2, 4), p2);
        assertEquals(3, game.countBlackDisks());
        assertEquals(5, game.countWhiteDisks());
        game.doMove(new Move(5, 5), p1);
        game.doMove(new Move(3, 1), p2);
        assertEquals(3, game.countBlackDisks());
        assertEquals(7, game.countWhiteDisks());
        game.doMove(new Move(1, 1), p1);
        game.doMove(new Move(5, 3), p2);
        assertEquals(4, game.countBlackDisks());
        assertEquals(8, game.countWhiteDisks());
    }

    /**
     * A test that checks if a method isValidMove() works as intended: checks
     * whether it is possible to make a move after checking all 8 directions
     * of the board. If there is at least one direction that allows a move to
     * be made, the move is considered as valid. All directions are tested separately
     * in other tests.
     */
    @Test
    public void testIsValidMove() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Naive(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        // Checks the valid moves for p1 when the game is just created
        assertTrue(game.isValidMove(new Move(2, 3), p1));
        assertTrue(game.isValidMove(new Move(3, 2), p1));
        assertTrue(game.isValidMove(new Move(4, 5), p1));
        assertTrue(game.isValidMove(new Move(5, 4), p1));
        // Move (2,2) is not valid before black plays (2,3), only after (2,2) is played
        assertFalse(game.isValidMove(new Move(2, 2), p2));
        game.doMove(new Move(2, 3), p1);
        assertTrue(game.isValidMove(new Move(2, 2), p2));
        game.doMove(new Move(2, 2), p2);
        assertFalse(game.isValidMove(new Move(2, 4), p1));
        assertTrue(game.isValidMove(new Move(2, 1), p1));
        assertTrue(game.isValidMove(new Move(3, 2), p1));
        assertTrue(game.isValidMove(new Move(4, 5), p1));
        assertTrue(game.isValidMove(new Move(5, 4), p1));
        assertTrue(game.isValidMove(new Move(2, 4), p2));
        assertTrue(game.isValidMove(new Move(5, 3), p2));
        assertTrue(game.isValidMove(new Move(4, 2), p2));
        assertTrue(game.isValidMove(new Move(1, 3), p2));
        assertTrue(game.isValidMove(new Move(3, 5), p2));
    }

    /**
     * A test that checks if the winner of the game is correctly
     * determined after finishing the game. If the game ends with a tie,
     * the game should not have a winner therefore
     * getWinner() method should return null. In each case, different conditions are checked
     * for the return of getWinner().
     */
    @Test
    public void testGetWinner() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Naive(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        while (!game.isGameOver()) {
            if (game.getValidMoves(game.getTurn()) != null) {
                game.doMove(((ComputerPlayer) game.getTurn()).determineMove(game), game.getTurn());
            } else {
                // No moves, so change the turn
                game.changeTurn();
            }
        }
        if (game.countBlackDisks() > game.countWhiteDisks()) {
            assertEquals(p1, game.getWinner());
            assertNotEquals(p2, game.getWinner());
            assertNotEquals(null, game.getWinner());
        } else if (game.countBlackDisks() < game.countWhiteDisks()) {
            assertNotEquals(p1, game.getWinner());
            assertEquals(p2, game.getWinner());
            assertNotEquals(null, game.getWinner());
        } else {
            assertNotEquals(p1, game.getWinner());
            assertNotEquals(p2, game.getWinner());
            assertEquals(null, game.getWinner()); // it is a tie
        }
    }

    /**
     * A test to check if the doMove() method works as expected. This method
     * should do a move to the coordinates given if and only if that move is a
     * valid move, in other case, the move should never be made.
     */
    @Test
    public void testDoMove() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Smart(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);

        // Try to make an invalid move as a first player
        assertFalse(game.isValidMove(new Move(2, 2), p1));
        game.doMove(new Move(2, 2), p1);
        // After trying to make a not valid move, check if that field on the board remains empty
        assertEquals(Mark.EMPTY, game.getBoard().getField(2, 2));
        // Now try to make a valid move
        assertTrue(game.isValidMove(new Move(2, 3), p1));
        game.doMove(new Move(2, 3), p1);
        assertEquals(Mark.BLACK, game.getBoard().getField(2, 3));

        // Try to make an invalid move as a second player
        assertFalse(game.isValidMove(new Move(1, 3), p2));
        game.doMove(new Move(1, 3), p2);
        assertEquals(Mark.EMPTY, game.getBoard().getField(1, 3));
        // Now try to make a valid move
        assertTrue(game.isValidMove(new Move(2, 2), p2));
        game.doMove(new Move(2, 2), p2);
        assertEquals(Mark.WHITE, game.getBoard().getField(2, 2));
    }

    /**
     * A test that ensures flip() method works correctly. Disk(s) should be flipped
     * in case a move is done and there is at least one disk of opposite color that is in between
     * the disk that was placed by a move and another disk that is present further away in
     * one of the directions. It could happen that after a player places a disk, the disks that got
     * flipped are not in one line since all the directions are checked when flip() is called.
     * Therefore, disks from multiple directions could be flipped.
     */
    @Test
    public void testFlip() {
        Player p1 = new ComputerPlayer(Mark.BLACK, new Smart(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(2, 1), p1);
        game.doMove(new Move(2, 4), p2);
        // Check if two directions are flipped correctly
        assertEquals(Mark.WHITE, game.getBoard().getField(2, 4));
        assertEquals(Mark.WHITE, game.getBoard().getField(3, 4));
        game.doMove(new Move(2, 5), p1);
        assertEquals(Mark.BLACK, game.getBoard().getField(2, 4));
        assertEquals(Mark.BLACK, game.getBoard().getField(3, 4));
        game.doMove(new Move(1, 4), p2);
        assertEquals(Mark.WHITE, game.getBoard().getField(3, 4));
        assertEquals(Mark.WHITE, game.getBoard().getField(4, 4));
        game.doMove(new Move(4, 5), p1);
        assertEquals(Mark.BLACK, game.getBoard().getField(3, 4));
        assertEquals(Mark.BLACK, game.getBoard().getField(4, 4));
    }

    /**
     * A test to play 1000 different games using Smart AI as the first player and Naive AI
     * as the second player. This test ensures that all the games finish successfully, so in every
     * game there is a winner or there is a tie. Counter gets incremented each time the game
     * ends successfully. In addition, this is the measure to check if the Smart AI
     * is better than Naive AI, so the Smart ComputerPlayer
     * has to win more games to prove that this is true.
     * After each game, the number of moves made is checked, and it should not be more than 60.
     */
    @Test
    public void testOneThousandGamesWithDifferentDifficulties() {
        int naiveWon = 0;
        int smartWon = 0;
        int draw = 0;

        int counter = 0;
        int moveCounter = 0; // to check if the amount of moves played per game is at maximum 60
        for (int i = 0; i < 1000; i++) {
            // Create two players and start the game
            Player p1 = new ComputerPlayer(Mark.BLACK, new Smart(), "player one");
            Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
            Game game = new OthelloGame(p1, p2);

            while (!game.isGameOver()) {
                if (game.getValidMoves(game.getTurn()) != null) {
                    game.doMove(((ComputerPlayer) game.getTurn()).determineMove(game),
                            game.getTurn());
                    moveCounter++;
                } else {
                    // No moves, so change the turn
                    game.changeTurn();
                }
            }
            if (game.getWinner() == null) {
                System.out.println("ITS A DRAW!");
                draw++;
                counter++;
            } else {
                System.out.println("Congratulations " +
                        game.getWinner().getName() + ", YOU WIN!!\n");
                counter++;
            }
            if (game.getWinner() == p1) {
                smartWon++;
            }
            if (game.getWinner() == p2) {
                naiveWon++;
            }
            assertTrue(moveCounter <= 60);
            moveCounter = 0;
        }
        System.out.println("Naive won " + naiveWon + " times!");
        System.out.println("Smart won " + smartWon + " times!");
        System.out.println("We got a draw in " + draw + " games!");
        //System.out.println((double) smartWon/naiveWon);
        assertTrue(smartWon > naiveWon); // ensure that Smart AI is actually smarter than Naive AI
        assertEquals(1000, counter); // 1000 games were played without crashing
    }

    /**
     * A test to play one game of Othello fully where both of the players are Naive AI that
     * use a strategy to play a random move from the available list. If a game successfully ends,
     * counter gets incremented.
     */
    @Test
    public void testOneFullyRandomGame() {
        int counter = 0;
        Player p1 = new ComputerPlayer(Mark.BLACK, new Naive(), "player one");
        Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "player two");
        Game game = new OthelloGame(p1, p2);
        while (!game.isGameOver()) {
            if (game.getValidMoves(game.getTurn()) != null) {
                game.doMove(((ComputerPlayer) game.getTurn()).determineMove(game), game.getTurn());
            } else {
                // No moves, so change the turn
                game.changeTurn();
            }
        }
        if (game.getWinner() == null) {
            System.out.println("ITS A DRAW!");
            counter++;
        } else {
            System.out.println("Congratulations " + game.getWinner().getName() + ", YOU WIN!!\n");
            counter++;
        }
        assertEquals(1, counter);
    }



    /**
     * Test for a game where one of the players has no marks on the board
     * but the board is not full. The game should be over and of course there
     * should be a winner.
     */
    @Test
    public void testGameEndWhenBoardNotFull() {
        // Situation where the second player has no marks
        // and loses on not full board: e6 f4 e3 f6 g5 d6 e7 f5 c5
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        Move move;
        game.doMove(new Move(5, 4), p1); // e6
        game.doMove(new Move(3, 5), p2); // f4
        game.doMove(new Move(2, 4), p1); // e3
        game.doMove(new Move(5, 5), p2); // f6
        game.doMove(new Move(4, 6), p1); // g5
        game.doMove(new Move(5, 3), p2); // d6
        game.doMove(new Move(6, 4), p1); // e7
        game.doMove(new Move(4, 5), p2); // f5
        game.doMove(new Move(4, 2), p1); // c5
        assertTrue(game.isGameOver());
    }

    /**
     * Test to check the functionality of the verticalBot method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is below the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testVerticalBot() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.toString();
        assertTrue(game.verticalBot(new Move(2, 3), p1));
        assertFalse(game.verticalBot(new Move(2, 3), p2));
        assertFalse(game.isGameOver());
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(3, 2), p1);
        game.doMove(new Move(2, 4), p2);
        assertTrue(game.verticalBot(new Move(1, 2), p1));
        assertTrue(game.verticalBot(new Move(1, 3), p1));
        assertFalse(game.verticalBot(new Move(1, 3), p2));
        assertFalse(game.verticalBot(new Move(1, 2), p2));
    }

    /**
     * Test to check the functionality of the verticalTop method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is above the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testVerticalTop() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        assertTrue(game.verticalTop(new Move(5, 4), p1));
        assertFalse(game.verticalTop(new Move(5, 4), p2));
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(2, 2), p2);
        assertTrue(game.verticalTop(new Move(5, 4), p1));
        assertFalse(game.verticalTop(new Move(5, 4), p2));
        assertFalse(game.verticalTop(new Move(4, 5), p1));
        assertFalse(game.verticalTop(new Move(4, 5), p1));
    }

    /**
     * Test to check the functionality of the horizontalRight method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is to the right of the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testHorizontalRight() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(4, 5), p1);
        game.doMove(new Move(3, 5), p2);
        game.doMove(new Move(2, 6), p1);
        game.doMove(new Move(5, 5), p2);
        game.doMove(new Move(3, 2), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(5, 4), p1);
        game.doMove(new Move(2, 5), p2);
        assertTrue(game.horizontalRight(new Move(2, 4), p1));
        assertFalse(game.horizontalRight(new Move(2, 4), p2));
        assertFalse(game.horizontalRight(new Move(2, 3), p1));
        assertFalse(game.horizontalRight(new Move(2, 3), p2));
        assertTrue(game.horizontalRight(new Move(4, 2), p2));
        game.doMove(new Move(5, 6), p1);
        game.doMove(new Move(5, 2), p2);
        assertTrue(game.horizontalRight(new Move(2, 4), p1));
        assertTrue(game.horizontalRight(new Move(4, 2), p1));
        assertTrue(game.horizontalRight(new Move(3, 1), p2));
    }

    /**
     * Test to check the functionality of the horizontalLeft method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is to the left of the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testHorizontalLeft() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(4, 5), p1);
        game.doMove(new Move(3, 5), p2);
        game.doMove(new Move(2, 6), p1);
        game.doMove(new Move(5, 5), p2);
        game.doMove(new Move(3, 2), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(5, 4), p1);
        game.doMove(new Move(2, 5), p2);
        assertTrue(game.horizontalLeft(new Move(3, 6), p1));
        assertTrue(game.horizontalLeft(new Move(4, 6), p1));
        assertTrue(game.horizontalLeft(new Move(5, 6), p1));
        assertTrue(game.horizontalLeft(new Move(2, 7), p2));
        assertFalse(game.horizontalLeft(new Move(2, 4), p2));
    }

    /**
     * Test to check the functionality of the DiagonalTopRight method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is in the right-up cell from the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testDiagonalTopRight() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(4, 5), p1);
        game.doMove(new Move(3, 5), p2);
        game.doMove(new Move(2, 6), p1);
        game.doMove(new Move(5, 5), p2);
        game.doMove(new Move(3, 2), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(5, 4), p1);
        game.doMove(new Move(2, 5), p2);
        assertTrue(game.diagTopRight(new Move(5, 2), p2));
        assertTrue(game.diagTopRight(new Move(5, 3), p2));
        assertTrue(game.diagTopRight(new Move(6, 3), p2));
        game.doMove(new Move(2, 4), p1);
        game.doMove(new Move(5, 3), p2);
        assertTrue(game.diagTopRight(new Move(4, 2), p1));
        assertTrue(game.diagTopRight(new Move(5, 2), p1));
        assertTrue(game.diagTopRight(new Move(6, 2), p1));
        assertFalse(game.diagTopRight(new Move(3, 1), p1));
    }

    /**
     * Test to check the functionality of the DiagonalTopLeft method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is in the left-up cell from the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testDiagonalTopLeft() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(5, 4), p1);
        game.doMove(new Move(3, 5), p2);
        assertTrue(game.diagTopLeft(new Move(5, 5), p2));
        game.doMove(new Move(2, 4), p1);
        game.doMove(new Move(5, 3), p2);
        assertTrue(game.diagTopLeft(new Move(4, 6), p1));
        assertTrue(game.diagTopLeft(new Move(6, 5), p2));
        assertFalse(game.diagTopLeft(new Move(5, 5), p1));
        assertFalse(game.diagTopLeft(new Move(4, 5), p2));
    }

    /**
     * Test to check the functionality of the DiagonalBotRight method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is in the right-down cell from the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testDiagonalBotRight() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(3, 2), p1);
        game.doMove(new Move(2, 2), p2);
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(2, 4), p2);
        assertTrue(game.diagBotRight(new Move(1, 1), p1));
        assertFalse(game.diagBotRight(new Move(2, 1), p2));
        game.doMove(new Move(3, 5), p1);
        game.doMove(new Move(5, 3), p2);
        assertTrue(game.diagBotRight(new Move(1, 2), p1));
        assertTrue(game.diagBotRight(new Move(1, 3), p1));
        assertTrue(game.diagBotRight(new Move(2, 1), p2));
        assertFalse(game.diagBotRight(new Move(1, 1), p1));
        assertFalse(game.diagBotRight(new Move(1, 2), p2));
    }

    /**
     * Test to check the functionality of the DiagonalBotLeft method that checks
     * if it is possible to do a move in an empty spot at given coordinates when the opposite color
     * mark is in the left-down cell from the checked empty spot. Some moves are done manually to check more times.
     */
    @Test
    public void testDiagonalBotLeft() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(4, 5), p1);
        game.doMove(new Move(3, 5), p2);
        game.doMove(new Move(2, 3), p1);
        game.doMove(new Move(3, 2), p2);
        assertTrue(game.diagBotLeft(new Move(2, 5), p1));
        assertTrue(game.diagBotLeft(new Move(2, 6), p1));
        assertTrue(game.diagBotLeft(new Move(1, 4), p2));
        game.doMove(new Move(2, 2), p1);
        game.doMove(new Move(5, 4), p2);
        assertTrue(game.diagBotLeft(new Move(3, 6), p2));
        assertFalse(game.diagBotLeft(new Move(3, 6), p1));
        assertFalse(game.diagBotLeft(new Move(2, 4), p2));
    }

    /**
     * A test to check if the Letter as an index of the board is correctly
     * translated to the digit.
     */
    @Test
    public void testInvertIndex() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        assertEquals("A", game.invertIndex(0));
        assertEquals("B", game.invertIndex(1));
        assertEquals("C", game.invertIndex(2));
        assertEquals("D", game.invertIndex(3));
        assertEquals("E", game.invertIndex(4));
        assertEquals("F", game.invertIndex(5));
        assertEquals("G", game.invertIndex(6));
        assertEquals("H", game.invertIndex(7));
    }

    /**
     * A test that checks if toString() method works as expected. This method should return
     * some textual representation of the current board and the player who currently has the turn.
     * This test checks if the text printed with getString() method contains these things.
     */
    @Test
    public void testToString() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        game.doMove(new Move(4, 5), p1);
        assertEquals(true, game.toString().contains(game.getBoard().toString()));
        assertEquals(true, game.toString().contains(game.getTurn().getName()));
        game.doMove(new Move(3, 5), p2);
        assertEquals(true, game.toString().contains(game.getBoard().toString()));
        assertEquals(true, game.toString().contains(game.getTurn().getName()));
    }

    /**
     * A test to make sure the deppCopy() method works as expected.
     * It should make an identical copy of the board. If after that one
     * of the boards is changed, the other one does not change.
     */
    @Test
    public void testDeepCopy() {
        Player p1 = new HumanPlayer("First player", Mark.BLACK);
        Player p2 = new HumanPlayer("Second player", Mark.WHITE);
        Game game = new OthelloGame(p1, p2);
        // Do some moves and then create a copy of the game
        game.doMove(new Move(4, 5), p1);
        game.doMove(new Move(3, 5), p2);
        Game copy = game.deepCopy();
        assertEquals(game.getTurn(), copy.getTurn()); // p1 is the same
        assertEquals(game.getOtherPlayer(), copy.getOtherPlayer()); // p2 is the same
        // Check if the fields on both board are the same
        assertEquals(Mark.WHITE, copy.getBoard().getField(3, 3));
        assertEquals(Mark.WHITE, copy.getBoard().getField(3, 4));
        assertEquals(Mark.WHITE, copy.getBoard().getField(3, 5));
        assertEquals(Mark.BLACK, copy.getBoard().getField(4, 3));
        assertEquals(Mark.BLACK, copy.getBoard().getField(4, 4));
        assertEquals(Mark.BLACK, copy.getBoard().getField(4, 5));
        game.doMove(new Move(2, 2), p1); // only the original board should be changed
        assertEquals(Mark.EMPTY, copy.getBoard().getField(2, 2));
        assertEquals(Mark.BLACK, game.getBoard().getField(2, 2));
    }
}
