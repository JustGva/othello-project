import AI.Naive;
import AI.Smart;
import model.Game;
import model.Mark;
import model.OthelloGame;
import player.ComputerPlayer;
import player.Player;

public class MainClass {

    public static void main(String[] args) {
        int naiveWon = 0;
        int smartWon = 0;
        int draw = 0;
        for (int i = 0; i < 1000; i++) {
            Player p1 = new ComputerPlayer(Mark.BLACK, new Smart(), "SmartJJ");
            Player p2 = new ComputerPlayer(Mark.WHITE, new Naive(), "NaiveJJ");
            Game game = new OthelloGame(p1, p2);

            while (!game.isGameOver()) {
//                System.out.println(game);
                if (game.getValidMoves(game.getTurn()) != null) {
                    game.doMove(((ComputerPlayer) game.getTurn()).determineMove(game),
                            game.getTurn());
                } else {
                    game.changeTurn();
                }
            }
            if (game.getWinner() == null) {
                System.out.println("ITS A DRAW!");
                draw++;
            } else {
                System.out.println("Congratulations " +
                        game.getWinner().getName() + ", YOU WIN!!\n");
            }
            if (game.getWinner() == p1) {
                smartWon++;
            }
            if (game.getWinner() == p2) {
                naiveWon++;
            }
        }
        System.out.println("Naive won " + naiveWon + " times!");
        System.out.println("Smart won " + smartWon + " times!");
        System.out.println("We got a draw in " + draw + " games!");
        System.out.println((double) smartWon / naiveWon);
    }
}
