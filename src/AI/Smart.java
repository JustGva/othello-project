package AI;

import model.Board;
import model.Game;
import model.Move;

import java.util.List;

/**
 * A class for Smart AI.
 */
public class Smart implements Strategy {


    /**
     * Gets the name of the strategy.
     *
     * @return name of strategy
     */
    @Override
    public String getName() {
        return "SmartJJ";
    }

    /**
     * This method calculates the move to be made by a computer player.
     * Depending on the strategy it will either return a naive (random) move
     * or a smart (based on actual strategy) move
     *
     * @param game
     * @return move to be made by ComputerPlayer
     */
    @Override
    public Move determineMove(Game game) {
        int dim = Board.DIM - 1;
        List<Move> validMoves = game.getValidMoves(game.getTurn());
        if (validMoves == null) {
            return null;
        }
        // Checking if there is a corner as a valid move
        for (Move m : validMoves) {
            if (m.getCol() == 0 && m.getRow() == 0 ||
                    m.getCol() == 0 && m.getRow() == dim ||
                    m.getCol() == dim && m.getRow() == 0 ||
                    m.getCol() == dim && m.getRow() == dim) {
                return m;
            }
            if (m.getCol() == 0 || m.getCol() == dim || m.getRow() == 0 || m.getRow() == dim) {
                return m;
            }
            if (m.getCol() > 1 && m.getCol() < 6 && m.getRow() > 1 && m.getRow() < 6) {
                return m;
            }
        }
        return validMoves.get((int) (Math.random() * validMoves.size()));
    }
}
