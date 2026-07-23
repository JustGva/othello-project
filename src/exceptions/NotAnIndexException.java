package exceptions;

/**
 * A class to represent exception when input does not match the board cell.
 */
public class NotAnIndexException extends RuntimeException {
    /**
     * Prints the RunTimeException error message.
     * @param message message to throw
     */
    public NotAnIndexException(String message) {
        super(message);
    }

    /**
     * Prints the following message as an exception.
     */
    public NotAnIndexException() {
        this("Your input does not represent a board cell!");
    }
}
