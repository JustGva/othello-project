package player;

import model.Game;
import model.Move;

/**
 * A player of a turn-based game.
 */
public abstract class AbstractPlayer implements Player {

    private String name;

    /**
     * Creates an abstract player.
     * @param name of the player
     */
    public AbstractPlayer(String name) {
        this.name = name;
    }

    /**
     * Determine move for the game.
     * @param game
     * @return a valid move
     */
    public abstract Move determineMove(Game game);

    /**
     * Returns the name of the player.
     * @return name
     */
    public String getName() {
        return name;
    }
}
