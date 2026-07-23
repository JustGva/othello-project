package AI;

import model.Game;
import model.Move;

/**
 * A class for creating a Naive AI strategy.
 */
public interface Strategy {

    /**
     * Gets the name of the strategy.
     * @return name of strategy
     */
    String getName();

    /**
     * This method calculates the move to be made by a computer player.
     * Depending on the strategy it will either return a naive (random) move
     * or a smart (based on actual strategy) move.
     * @param game
     * @return move to be made by ComputerPlayer
     */
    Move determineMove(Game game);

}
