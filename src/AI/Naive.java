package AI;

import model.Game;
import model.Move;

import java.util.List;

/**
 * A class for creating a Naive AI strategy.
 */
public class Naive implements Strategy {


    /**
     * Gets the name of the strategy.
     *
     * @return name of strategy
     */
    @Override
    public String getName() {
        return "NaiveJJ";
    }

    /**
     * This method calculates the move to be made by a computer player.
     * Depending on the strategy it will either return a naive (random) move
     * or a smart (based on actual strategy) move.
     *
     * @param game current game
     * @return move to be made by ComputerPlayer
     */
    @Override
    public Move determineMove(Game game) {
        List<Move> validMoves = game.getValidMoves(game.getTurn());
        // Out of the list of moves, get a random element and return it
        if (validMoves == null) {
            return null;
        }
        return validMoves.get((int) (Math.random() * validMoves.size()));
    }
}
