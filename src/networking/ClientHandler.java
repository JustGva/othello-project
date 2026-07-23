package networking;

import model.OthelloGame;

import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private OthelloGame ourGame;
    private String name = "";
    private final BufferedReader in;
    private final PrintWriter out;

    /**
     * Constructs a new ClientHandler object
     * @param socket
     * @param server
     * @throws IOException
     */
    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    /**
     * notifies the client in case the opponent has disconnected.
     */
    public synchronized void notifyDisconnect() {
        out.println(Protocol.printError("Your opponent has disconnected"));
        out.println(Protocol.sendGameOver("DISCONNECT", name));
    }

    /**
     * Invoked by the server, send the client a move to be performed on their board.
     * @param i index
     */
    public synchronized void updateMove(String i) {
        out.println(Protocol.sendMove(i));
    }

    /**
     * invoked by the server, sends the client a GAMEOVER message.
     * This could be a result of one of three things.
     * Victory - the game has ended and there's a winner
     * Draw - the game has ended, and it's a draw
     * Disconnect - a player has disconnected (handled in notifyDisconnect()
     */
    public synchronized void notifyGameOver() {
        if (ourGame.getWinner() == null) {
            out.println(Protocol.sendGameOver("DRAW", "DRAW"));
        } else {
            out.println(Protocol.sendGameOver("victory", ourGame.getWinner().getName()));
        }
    }

    public String getName() {
        return name;
    }

    /**
     * A separate thread that is responsible for receiving and handling messages from the Client.
     */
    @Override
    public void run() {
        while (true) {
            try {
                String[] input = in.readLine().split("~");
                String command = input[0];
                String message = "";
                if (input.length > 1) {
                    message = input[1];
                }
                switch (command) {
                    case "HELLO":
                        out.println(Protocol.helloMessage("Server by Jordan and Justas"));
                        break;

                    case "LOGIN":
                        if (server.checkUsername(message)) {
                            out.println(Protocol.successfulLogin());
                            this.name = message;
                        } else {
                            out.println(Protocol.usernameTaken());
                        }
                        break;

                    case "QUEUE":
                        server.joinQueue(this);
                        while (server.getGame(this) == null) {
                            server.matchPlayers();
                        }
                        ourGame = server.getGame(this);
                        String p1name = ourGame.getTurn().getName();
                        String p2name = ourGame.getOtherPlayer().getName();
                        out.println(Protocol.makeNewGame(p1name, p2name));
                        break;

                    case "LIST":
                        out.println(Protocol.returnCurrentUsers(server.getList()));
                        break;

                    case "MOVE":

                        int index = Integer.parseInt(message);
                        //if it's the correct turn
                        if (ourGame.getTurn().getName().equals(name)) {
                            //if the player passes their turn, and they don't have a valid move
                            if (index == 64) {
                                if (ourGame.getValidMoves(ourGame.getTurn()) == null && !ourGame.isGameOver()) {
                                    ourGame.changeTurn();
                                    server.sendMoveToBothPlayers("64", ourGame);
                                } else {
                                    //if a player passes their turn, but they do have a valid move
                                    out.println(Protocol.printError("You cannot pass your turn"));
                                }
                                //if a player executes a move
                            } else if (index >= 0 && index < 64) {
                                server.sendMoveToBothPlayers(message, ourGame);
                            }
                        } else {
                            out.println(Protocol.printError("It's not your turn!"));
                        }
                        break;

                    default:
                        out.println(Protocol.printError("That's an invalid command"));
                }
            } catch (IOException | RuntimeException e) {
                System.out.println(name + " has disconnected from the server");
                out.println(Protocol.sendGameOver("DISCONNECT", name));
                server.removeHandler(this);
                server.notifyDisconnect(ourGame);
                try {
                    socket.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }
    }


}
