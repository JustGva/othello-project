package player;

import model.Mark;

/**
 * A class representing a human player that has a name and
 * a mark.
 */
public class HumanPlayer implements Player {

    private String name;
    private Mark mark;

    /**
     * Creates a player and assigns it a name and a mark.
     * @param name name of the player
     * @param mark mark of the player
     */
    public HumanPlayer(String name, Mark mark) {
        this.name = name;
        this.mark = mark;
    }

    /**
     * Returns the name of a player.
     * @return name player name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the mark of a player.
     * @return mark player mark
     */
    public Mark getMark() {
        return this.mark;
    }
}
