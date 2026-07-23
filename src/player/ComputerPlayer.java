package player;

import AI.Strategy;
import model.Game;
import model.Mark;
import model.Move;

/**
 * A class for the Computer player implementation.
 */
public class ComputerPlayer extends AbstractPlayer {

    private String name;
    private Mark mark;
    private Strategy strategy;

    /**
     * Constructs a computer player.
     * @param mark mark of the player
     * @param strategy strategy of the computer player
     * @param name name of the computer player
     */
    public ComputerPlayer(Mark mark, Strategy strategy, String name) {
        super(name);
        this.strategy = strategy;
        this.mark = mark;
        this.name = name;
    }

    /**
     * Determine a move for the game.
     * @param game game
     * @return move
     */
    public Move determineMove(Game game) {
        return strategy.determineMove(game);
    }

    /**
     * Returns the name of the player.
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the mark of the player.
     * @return
     */
    public Mark getMark() {
        return mark;
    }
}
