package networking;

/**
 * networking.Protocol class with constants and methods for creating protocol messages.
 */
public class Protocol {
    public Protocol() {}

    public static final String HELLO = "HELLO";
    public static final String FROM = "FROM";
    public static final String SEPARATOR = "~";
    public static final String ERROR = "ERROR";
    public static final String LOGIN = "LOGIN";
    public static final String ALREADY_LOGGED_IN = "ALREADYLOGGEDIN";
    public static final String LIST = "LIST";
    public static final String QUEUE = "QUEUE";
    public static final String NEW_GAME = "NEWGAME";
    public static final String MOVE = "MOVE";
    public static final String GAME_OVER = "GAMEOVER";
    public static final String DISCONNECT = "DISCONNECT";
    public static final String VICTORY = "VICTORY";
    public static final String DRAW = "DRAW";



    //-------------------------------------ERROR HANDLING-------------------------------------//

    /**
     * Build a new protocol message that indicates there was an illegal input.
     * It could indicate that the protocol is not followed, illegal move was sent etc.
     * @return protocol message
     */
    public static final String printError(String message) {
        return ERROR + SEPARATOR + message;
    }

    //---------------------------------------HANDSHAKE----------------------------------------//


    /**
     * Build a new protocol message that responds to the initial HELLO message
     * sent by the client. Could be used for both client and server.
     * First part of the handshake.
     * @return protocol message
     */
    public static String helloMessage(String message) {
        return HELLO + SEPARATOR + message;
    }

    /**
     * Build a new protocol message that instructs the server that you want to log in
     * with this name. Second part of the handshake.
     * @param name name of the client
     * @return protocol message
     */
    public static String loginMessage(String name) {
        return LOGIN + SEPARATOR + name;
    }


    /**
     * Build a new protocol message that the server uses to inform the client about a
     * successful login. Third and last part of the handshake.
     * @return protocol message
     */
    public static String successfulLogin() {
        return LOGIN;
    }

    /**
     * Build a new protocol message that is a reply to the client that the username
     * the client wants to log in is already taken.
     * @return protocol message
     */
    public static String usernameTaken() {
        return ALREADY_LOGGED_IN;
    }

    //-------------------------COMMANDS AVAILABLE AFTER THE HANDSHAKE-------------------------//

    /**
     * Build a new protocol message that the client can use to get the list of currently
     * connected clients to the server. The command is allowed only after the initial handshake.
     * @return protocol message
     */
    public static String getList() {
        return LIST;
    }

    /**
     * Build a new protocol message that the server sends as a response to the `LIST` command.
     * The response includes a list of clients that are currently logged in into the server.
     * @return protocol message
     */
    public static String returnCurrentUsers(String message) {
        return LIST + SEPARATOR + message;
    }

    /**
     * Build a new protocol message that the client sends in order to join the queue to participate
     * in the game. If the clients sends this command the second time, client is removed from the queue.
     * @return protocol message
     */
    public static String joinQueue() {
        return QUEUE;
    }

    /**
     * Build a new protocol message that the server sends in order to inform the two players who were
     * in the queue that they are put into a game. First player is the one who starts the game.
     * @param player1 name of the first player
     * @param player2 name of the second player
     * @return protocol message
     */
    public static String makeNewGame(String player1, String player2) {
        return NEW_GAME + SEPARATOR + player1 + SEPARATOR + player2;
        //TODO: FINISH THE COMMAND BY GETTING THE NAMES OF THE USER FROM THE LIST
    }

    /**
     * Build a new protocol message that is sent by the client to indicate which move
     * the client wants to play. This command is only allowed when it is the turn of the player.
     * @param move move the client wants to play
     * @return protocol message
     */
    public static String sendMove(String move) {
        return MOVE + SEPARATOR + move;
    }

    /**
     * Build a new protocol message that is sent by the server to indicate the next move played
     * by the server. This is sent to all the players in the game, including the player who
     * performed the move.
     * @param move move to be played
     * @return protocol message
     */
    public static String sendServerMove(String move) {
        return MOVE + SEPARATOR + move;
    }

    /**
     * Build a new protocol message that is sent by the server to indicate the end of the game.
     * The server provides the winning player and the reason for the end of the game.
     * The winner is included when the player won or the other player disconnected; when it is a draw,
     * no winner returned. The reasons are as follows:
     * 1. DISCONNECT if the game ended due to losing connection to one of the players. the winner is the player
     * who is still connected to the server;
     * 2. VICTORY if the game ended with one of the players winning;
     * 3. DRAW if the game ended in a draw.
     * @return protocol message
     */
    public static String sendGameOver(String outcome, String winner) {
        if (outcome.equalsIgnoreCase("disconnect")) {
            return GAME_OVER + SEPARATOR + DISCONNECT + SEPARATOR + winner;
        } else if (outcome.equalsIgnoreCase("draw")) {
            return GAME_OVER + SEPARATOR + DRAW;
        } else if (outcome.equalsIgnoreCase("victory")) {
            return GAME_OVER + SEPARATOR + VICTORY + SEPARATOR + winner;
        }
        return printError("unexpected error occurred - gameover");
    }
}
