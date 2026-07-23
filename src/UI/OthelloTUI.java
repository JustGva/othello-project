package UI;

import model.Mark;
import model.Move;
import model.OthelloGame;
import player.HumanPlayer;
import player.Player;

import java.util.List;
import java.util.Scanner;

/**
 * TUI for the Othello game.
 */
public class OthelloTUI {
    private OthelloGame othelloGame;
    private static Player p1;
    private static Player p2;
    private static String p1Name;
    private static String p2Name;

    /**
     * Creates 2 new human players and a
     * TUI for the Othello game with these 2 human players.
     */
    public OthelloTUI() {
        p1 = new HumanPlayer(p1Name, Mark.BLACK);
        p2 = new HumanPlayer(p2Name, Mark.WHITE);
        othelloGame = new OthelloGame(p1, p2);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter player1 name:");
        p1Name = sc.nextLine();
        System.out.println("Enter player2 name:");
        p2Name = sc.nextLine();
        System.out.println("GAME IS STARTING");
        OthelloTUI oTUI = new OthelloTUI();
        oTUI.run();
    }

    /**
     * Runs the game that 2 players can play. While the game is not
     * finished, the TUI asks the user for the move and prints the board
     * when the move is played. When the game ends, TUI prints the final board
     * and announces the final result of the game.
     */
    public void run() {
        while (!othelloGame.isGameOver()) {
            Scanner input = new Scanner(System.in);
            Move move;
            int row;
            int col;
            System.out.println(othelloGame.toString()); // print the board
            String possibleMoves = ""; // possible moves for the current player
            String otherPossibleMoves = ""; // possible moves for the other player

            // calculate moves for the current player
            List<Move> validMovesForFirstPlayer =
                    othelloGame.getValidMoves(othelloGame.getTurn());
            List<Move> validMovesForSecondPlayer =
                    othelloGame.getValidMoves(othelloGame.getOtherPlayer());

            if (validMovesForFirstPlayer != null) {
                for (Move m : validMovesForFirstPlayer) {
                    possibleMoves +=
                            (othelloGame.invertIndex(m.getCol())) + (m.getRow() + 1) + " ";
                }
            }
            // calculate moves for the other player
            if (validMovesForSecondPlayer != null) {
                for (Move m : othelloGame.getValidMoves(othelloGame.getOtherPlayer())) {
                    otherPossibleMoves +=
                            (othelloGame.invertIndex(m.getCol())) + (m.getRow() + 1) + " ";
                }
            }

            // no player has moves
            if (othelloGame.isGameOver()) {
                System.out.println("yes!");
                continue;
            }

            /*
             Current player has no moves left, but the other player has,
             current player needs to pass.
             */
            if (possibleMoves.isEmpty() && !otherPossibleMoves.isEmpty()) {
                System.out.println("You have no possible moves. " +
                        "You need to type \"PASS\" to continue the game!");
                while (!input.nextLine().equalsIgnoreCase("PASS")) {
                    System.out.println("Invalid input. Please type \"PASS\" to continue the game!");
                }
                if (input.nextLine().equalsIgnoreCase("PASS")) {
                    othelloGame.changeTurn();
                }
            } else {
                System.out.println("Your possible moves are: " + possibleMoves);
                System.out.println("What would your next move be?");
                String line = input.nextLine().toUpperCase();
                col = othelloGame.getBoard().index(line.charAt(0));
                row = Character.getNumericValue(line.charAt(1)) - 1;
                move = new Move(row, col);
                othelloGame.doMove(move, othelloGame.getTurn());
            }
        }

        //Second player wins with not full board:
        if (othelloGame.getWinner() == null) {
            System.out.println("ITS A DRAW!");
        } else {
            System.out.println("Congratulations " +
                    othelloGame.getWinner().getName() + ", YOU WIN!!");
        }
    }
}
