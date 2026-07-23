package networking;

import AI.Naive;
import AI.Smart;
import model.Mark;
import model.Move;
import model.OthelloGame;
import player.ComputerPlayer;
import player.HumanPlayer;
import player.Player;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Client implements Runnable {
    private static InetAddress address = null;
    private static int port = -1;
    private final Socket s;
    private static String username;
    private static PrintWriter out;
    private static BufferedReader in;
    private String p1name;
    private String p2name;
    private OthelloGame thisGame;
    private boolean isAI = false;
    private boolean running = true;

    /**
     * Create a client with the given socket.
     * @param s socket
     * @throws IOException
     */
    public Client(Socket s) throws IOException {
        this.s = s;
        out = new PrintWriter(s.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }

    /**
     * Main method for the implementation of a Client.
     * A new client needs to enter the IP address and a port number
     * of the Server. If all the given values are correct, the connection
     * should be established, otherwise one of the possible errors will be thrown.
     * @param args
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (address == null) {
            System.out.println("Please enter the IP address by name:");
            try {
                address = InetAddress.getByName(sc.nextLine());
            } catch (UnknownHostException e) {
                System.out.println("This host is unknown! " +
                        "Please try to enter IP address by name again!");
            }
        }

        while (port == -1) {
            System.out.println("Please enter the port number:");
            try {
                int possiblePort = sc.nextInt();
                if (possiblePort > 0 && possiblePort < 65536) {
                    port = possiblePort;
                    break;
                } else {
                    System.out.println("Invalid port. Please try again.");
                }
            } catch (InputMismatchException e) {
                sc.nextLine(); // consume the invalid input
                System.out.println("Invalid input. Port must be an integer!");
            }
        }

        System.out.println("Connecting to server...");

        if (port >= 0 && port < 65536) {
            try {
                Socket s = new Socket(address, port);
                Client client = new Client(s);
                System.out.println("Connected!");
                Thread threadForIn = new Thread(client);
                threadForIn.start();
                // Initiating handshake
                out.println(Protocol.helloMessage("Client by Jordan and Justas"));

            } catch (IOException e) {
                System.out.println("Failed to connect!");
            }
        }
    }

    /**
     * A handshake part where the user is asked for the name and
     * that name is sent to the server as a try to log in.
     * @throws IOException
     * @throws InterruptedException
     */
    public void login() throws IOException, InterruptedException {
        System.out.println("Please choose a username:");
        Scanner sc = new Scanner(System.in);
        // Replace any ~ that could be in the name
        username = sc.nextLine().replace("~", "");
        out.println(Protocol.loginMessage(username));
    }

    //start is called and forwards all commands sent by the user.

    /**
     *
     * @throws IOException
     */
    public void queue() throws IOException {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Type 'queue' to join a new game or 'quit' to close the game.");
            String line = sc.nextLine().toUpperCase();
            if (line.equals("QUEUE")) {
                System.out.println("Waiting for opponent...");
                out.println(Protocol.joinQueue());
                break;
            } else if (line.equals("QUIT")) {
                s.close();
                break;
            }
        }

    }

    /**
     * Allows player to choose how he wants to play the game: using AI or by itself.
     * After a player selects what he wants to do, a new game is created, started and declared
     * as running. In case of an incorrect input, the user is asked again for one.
     * @throws InterruptedException
     */
    public void choosePlayerType() throws InterruptedException {
        Scanner sc = new Scanner(System.in);
        System.out.println("Select player type:");
        System.out.println("1 - Play by myself");
        System.out.println("2 - Play as a naive AI");
        System.out.println("3 - Play as a smart AI");
        while (true) {
            Player p1;
            Player p2;
            int input = 0;

            try {
                input = sc.nextInt();
                sc.nextLine(); // clean the end-line "\n" since we use nextInt
                if (input == 1) {
                    System.out.println("You have selected yourself!");
                    p1 = new HumanPlayer(p1name, Mark.BLACK);
                    p2 = new HumanPlayer(p2name, Mark.WHITE);
                    thisGame = new OthelloGame(p1, p2);
                    running = true;
                    start(thisGame);
                    break;
                } else if (input == 2) {
                    isAI = true;
                    System.out.println("You have selected the naive AI");
                    if (p1name.equals(username)) {
                        p1 = new ComputerPlayer(Mark.BLACK, new Naive(), username);
                        p2 = new HumanPlayer(p2name, Mark.WHITE);
                    } else {
                        p1 = new HumanPlayer(p1name, Mark.BLACK);
                        p2 = new ComputerPlayer(Mark.WHITE, new Naive(), username);
                    }
                    thisGame = new OthelloGame(p1, p2);
                    running = true;
                    start(thisGame);
                    break;
                } else if (input == 3) {
                    isAI = true;
                    System.out.println("You have selected the smart AI");
                    if (p1name.equals(username)) {
                        p1 = new ComputerPlayer(Mark.BLACK, new Smart(), username);
                        p2 = new HumanPlayer(p2name, Mark.WHITE);
                    } else {
                        p1 = new HumanPlayer(p1name, Mark.BLACK);
                        p2 = new ComputerPlayer(Mark.WHITE, new Smart(), username);
                    }
                    thisGame = new OthelloGame(p1, p2);
                    running = true;
                    start(thisGame);
                    break;
                } else {
                    System.out.println("Please try entering a number again!");
                }
                String endline = sc.nextLine();
            } catch (InputMismatchException e) {
                sc.nextLine(); // consume the invalid input
                System.out.println("Invalid input. Please enter an integer.");
            }
        }
    }

    /**
     * Starts a new Othello game on a new thread.
     * includes the TUI for the user.
     * @param thisGame
     */
    public synchronized void start(OthelloGame thisGame) {
        // Starting TUI on a new thread
        Thread threadForTUI = new Thread(() -> {
            // Shows the player all available commands
            System.out.println(thisGame.toString());
            System.out.println(toString());
            // Prints possible moves if this client plays first
            if (thisGame.getTurn().getName().equals(username)) {
                String possibleMoves = "";
                List<Move> validMoves = thisGame.getValidMoves(thisGame.getTurn());
                for (Move m : validMoves) {
                    possibleMoves += (thisGame.invertIndex(m.getCol())) + (m.getRow() + 1) + " ";
                }
                System.out.println("Your possible moves are: " + possibleMoves);
            }
            // Starting game
            while (running) {
                // Introducing delay to give the server time to respond
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                if (thisGame.isGameOver()){
                    running = false;
                    break;
                }

                //if player chose an AI
                if (isAI) {
                    if (!thisGame.isGameOver()) {
                        if (thisGame.getTurn().getName().equals(username)) {
                            if (thisGame.getValidMoves(thisGame.getTurn()) == null) {
                                out.println(Protocol.sendMove("64"));
                            } else {
                                //class player.HumanPlayer cannot be cast to class player.ComputerPlayer (player.HumanPlayer and
                                //player.ComputerPlayer are in unnamed module of loader 'app')
                                Move move = ((ComputerPlayer) thisGame.getTurn()).
                                        determineMove(thisGame);
                                out.println(Protocol.sendMove(String.valueOf(move.getRow()
                                        * 8 + move.getCol())));
                            }
                        }
                    }

                    //if a player chose to play by itself
                } else {
                    Scanner sc = new Scanner(System.in);
                    String[] input = sc.nextLine().split(" ");
                    String command = input[0];
                    String message = "";
                    if (input.length > 1) {
                        message = input[1].toUpperCase();
                    }
                    switch (command.toUpperCase()) {
                        case "QUIT":
                            System.out.println("closing");
                            try {
                                s.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            running = false;
                            break;
                        case "HELP":
                            System.out.println(toString());
                            break;
                        case "MOVE":
                            //if player forgets to declare position
                            if (input.length < 2) {
                                System.out.println("You forgot something lol");
                                break;
                            }
                            //if its this player's turn
                            if (thisGame.getTurn().getName().equals(username)) {
                                //transforming the input into valid indices
                                int row = Character.getNumericValue(message.charAt(1)) - 1;
                                int col = thisGame.index(message.charAt(0));
                                if (row > 7 || col > 7 || row < 0 || col < 0) {
                                    System.out.println("Bro this isnt even a move");
                                    break;
                                }
                                Move move = new Move(row, col);
                                //check if move is valid <client side>
                                if (!thisGame.isValidMove(move, thisGame.getTurn())) {
                                    System.out.println("That's not a valid move :)");

                                } else {
                                    out.println(Protocol.sendMove(boardTo64(message)));
                                }
                            } else {
                                System.out.println("It's not your turn!");
                            }
                            break;
                        case "HINT":
                            ComputerPlayer ai;
                            if (thisGame.getTurn().getName().equals(username)) {
                                ai = new ComputerPlayer(thisGame.getTurn().getMark(),
                                        new Smart(), "R2D2");
                            } else {
                                ai = new ComputerPlayer(thisGame.getOtherPlayer().getMark(), new Smart(), "R2D2");
                            }
                            Move move = ai.determineMove(thisGame);
                            System.out.println("R2D2 says: try " + thisGame.invertIndex(move.getCol()) + (move.getRow() + 1));
                            break;
                        case "PASS":
                            if (thisGame.getTurn().getName().equals(username)) {
                                if (thisGame.getValidMoves(thisGame.getTurn()) == null) {
                                    out.println(Protocol.sendMove("64"));
                                } else {
                                    System.out.println("You still have moves to play!");
                                }
                            } else {
                                System.out.println("It's not your turn!");
                            }
                            break;
                        case "LIST":
                            out.println(Protocol.getList());
                            break;
                        default:
                            System.out.println("Invalid command!");
                    }
                }
            }
        });
        threadForTUI.start();
    }

    /**
     * translates a 2 dimensional cell into a one dimensional cell index.
     * @param input
     * @return corresponding index in one dimension array
     */
    public String boardTo64(String input) {
        int col = thisGame.index(input.charAt(0));
        int row = Character.getNumericValue(input.charAt(1)) - 1;
        return String.valueOf(row * 8 + col);
    }

    /**
     * translates a one dimensional cell into a two dimensional cell indices.
     * @param input
     * @return indices for a two-dimensional representation of the cell.
     */
    public int[] boardFrom64(String input) {
        int row = ((Integer.parseInt(input)) / 8);
        int col = ((Integer.parseInt(input)) % 8);
        return new int[]{row, col};
    }


    //run is called and receives all commands from the server

    /**
     * Handles all incoming messages sent by the server and acts accordingly.
     * Uses Protocol to interpret the messages.
     */
    @Override
    public void run() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                String[] input = line.split("~");
                String command = input[0];
                String message = "";
                String message2 = "";
                if (input.length == 2) {
                    message = input[1];
                }
                if (input.length == 3) {
                    message = input[1];
                    message2 = input[2];
                }
                switch (command) {
                    case "HELLO":
                        login();
                        break;
                    case "LOGIN":
                        System.out.println("Welcome " + username);
                        queue();
                        break;
                    case "ALREADYLOGGEDIN":
                        System.out.println("This user is already logged in!");
                        login();
                        break;
                    case "NEWGAME":
                        System.out.println("A new game between " + message + " and " + message2 + " has started!");
                        p1name = message;
                        p2name = message2;
                        choosePlayerType();
                        break;
                    case "MOVE":
                        if (message.equals("64")) {
                            System.out.println(thisGame.getTurn().getName() + " is out of moves and passed their turn!");
                            thisGame.changeTurn();
                        } else {
                            int[] indices = boardFrom64(message);
                            Move move = new Move(indices[0], indices[1]);
                            System.out.println(thisGame.getTurn().getName() + " played " + (thisGame.invertIndex(indices[1])) + "" + (indices[0] + 1));
                            if (!thisGame.isGameOver()) {
                                thisGame.doMove(move, thisGame.getTurn());
                            }

                        }
                        //prints updated board
                        System.out.println(thisGame.toString());
                        //prints possible moves
                        if (thisGame.getTurn().getName().equals(username) && !thisGame.isGameOver()) {
                            String possibleMoves = "";
                            List<Move> validMoves = thisGame.getValidMoves(thisGame.getTurn());
                            if (validMoves == null) {
                                System.out.println("You're out of moves buddy, please type 'pass'");
                            } else {
                                for (Move m : validMoves) {
                                    possibleMoves += (thisGame.invertIndex(m.getCol())) + (m.getRow() + 1) + " ";
                                }
                                System.out.println("Your possible moves are: " + possibleMoves);
                            }
                        }
                        break;
                    case "LIST":
                        System.out.println(message);
                        break;
                    case "GAMEOVER":
                        System.out.println(thisGame.toString());
                        System.out.println("GAME OVER!");
                        if (message.equals("DRAW")) {
                            System.out.println("Nobody wins!");
                        } else if (message.equals("VICTORY")) {
                            System.out.println(message2.toUpperCase() + " WINS!!");
                        } else {
                            System.out.println("Your opponent has left the game");
                        }
                        isAI = false;
                        running = false;
                        thisGame = null;
                        queue();
                        break;
                    case "ERROR":
                        System.out.println("Server: " + message);
                        break;
                    default:
                        System.out.println("Received something weird form the server...");
                }
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Disconnected from server");
            try {
                s.close();
            } catch (IOException ex) {
                System.out.println("Could not close connection");
            }
        }
    }

    /**
     * Prints the list of commands.
     * @return list of commands
     */
    @Override
    public String toString() {
        return "List of commands:\n"
                + "Help - to access this menu at any moment\n"
                + "Move x - to execute a move at position x (eg: move D3)\n"
                + "Hint - to see all your possible moves\n"
                + "Pass - to pass the turn if you're unable to execute a move\n"
                + "List - to see all currently online players!\n";
    }
}
