package networking;

import model.Mark;
import model.Move;
import model.OthelloGame;
import player.HumanPlayer;
import player.Player;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server implements Runnable {
    private ServerSocket ss;
    private boolean run = true;
    private Thread thread;
    private HashMap<ClientHandler, OthelloGame> handlers;
    private List<ClientHandler> queue;
    private static int port;


    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("Choose a port between 0 and 65535");

                if (!scanner.hasNextInt()) {
                    System.out.println("Invalid input. Please enter a number!");
                    scanner.nextLine();
                    continue;
                }

                try {
                    port = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a number!");
                    scanner.nextLine();
                    continue;
                }

                if (port < 0 || port > 65535) {
                    System.out.println("Invalid input. Please try again.");
                    continue;
                }
                scanner.nextLine(); // clean the end-line "\n" since we use nextInt
                Server server = new Server();
                server.start(port);
                System.out.println("Server started on port " + server.getPort());
                break;

            } catch (IOException e) {
                //if port is not available prompt user and loop again
                System.out.println("Could not start server on that port");
                System.out.println("Please enter 0 for an available port");
            }
        }
    }

    public void start(int port) throws IOException {
        //start a new server socket and run it on a new thread
        this.handlers = new HashMap<>();
        this.queue = new ArrayList<>();
        this.ss = new ServerSocket(port);
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void removeHandler(ClientHandler handler) {
        handlers.remove(handler);
    }

    public int getPort() {
        return ss.getLocalPort();
    }

    public synchronized OthelloGame getGame(ClientHandler ch) {
//        if (handlers.get(ch) != null){
            return handlers.get(ch);

    }

    public synchronized boolean checkUsername(String name) {
        for (ClientHandler handler : handlers.keySet()) {
            if (handler.getName().equals(name)) {
                return false;
            }
        }
        return true;
    }

    public synchronized void joinQueue(ClientHandler ch) {
        System.out.println(ch.getName() + " joined the queue");
        queue.add(ch);

    }

    public synchronized void matchPlayers() {
        if (queue.size() > 1) {
            Player p1 = new HumanPlayer(queue.get(0).getName(), Mark.BLACK);
            Player p2 = new HumanPlayer(queue.get(1).getName(), Mark.WHITE);
            OthelloGame game = new OthelloGame(p1, p2);
            handlers.put(queue.get(0), game);
            handlers.put(queue.get(1), game);
            System.out.println("A new game between " + queue.get(0).getName() + " and " + queue.get(1).getName() + " has started!");
            for (int i = 0; i < 2; i++) {
                queue.remove(0);
            }
        }
    }

    public int[] boardFrom64(String input) {
        int row = ((Integer.parseInt(input)) / 8);
        int col = ((Integer.parseInt(input)) % 8);
        return new int[]{row, col};
    }

    public synchronized void sendMoveToBothPlayers(String field, OthelloGame game) {
        if (!field.equals("64")){
            //transforming 0~63 to 2 indices because our board is a two-dimensional array
            int[] indices = boardFrom64(field);
            Move move = new Move(indices[0], indices[1]);
            //check if valid move <server side>
            if (game.isValidMove(move, game.getTurn())) {
                //update the game object with the new move
                game.doMove(move, game.getTurn());
            }
        }
        //look for all handlers in the server that run that particular game
        for (ClientHandler handler : handlers.keySet()) {
            //if we found a handler with the corresponding game
            if (getGame(handler) != null && getGame(handler) == game) {
                //prompt both handlers to send that move to their clients
                handler.updateMove(field);
            }
        }
        checkWinner(game);
    }

    public synchronized void checkWinner(OthelloGame game) {
        if (game.isGameOver()) {
            for (ClientHandler handler : handlers.keySet()) {
                if (getGame(handler) != null && getGame(handler).equals(game)) {
                    handler.notifyGameOver();
                    handlers.put(handler, null);
                }
            }
        }
    }

    public synchronized void notifyDisconnect(OthelloGame game) {
        for (ClientHandler handler : handlers.keySet()) {
            if (getGame(handler) != null && getGame(handler).equals(game)) {
                handlers.put(handler, null);
                System.out.println("Notified disconnect to " + handler.getName());
                handler.notifyDisconnect();
            }
        }
    }

    /**
     * Gets the list of players that are currently connected to the server.
     * List is formatted as a String.
     * @return String of a list
     */
    public synchronized String getList() {
        String result = "";
        int size = handlers.size();
        int counter = 1;
        result += "[";
        for (ClientHandler handler : handlers.keySet()) {
            result += handler.getName();
            if (counter != size) {
                result += ", ";
                counter++;
            }
        }
        result += "]";
        return result;
    }


    @Override
    public void run() {
        int clientCount = 0;
        while (run) {
            try {
                Socket s = ss.accept();
                System.out.println("accepted client " + clientCount);
                ClientHandler ch = new ClientHandler(s, this);
                handlers.put(ch, null);
                new Thread(ch).start();
                clientCount++;


            } catch (IOException e) {
                System.out.println("IOException while listening on port");
                run = false;
            }
        }
    }
}
