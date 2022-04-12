package cs310.games;

import java.util.*;

public class PlayGame {
    private static String defaultGame = "TicTacToe";
    private final static String nimGame = "Nim";
    private static Game game; // game
    private final static Scanner scan = new Scanner(System.in);
    private String currentGame;

    public PlayGame(String pickGame) {
        if (pickGame.equals(nimGame)) game = new Nim();
        else {
            game = new TicTacToe();
        }
        this.currentGame = pickGame;
    }

    public void doComputerMove() {
        printBoard();
        BestMove compMove = game.chooseMove(Game.COMPUTER, 0);
        // needs to alternate between columns (ttt) and stars (nim) for clarity.
        if (currentGame.equals(defaultGame)) {
            System.out.println("Computer plays ROW = " + compMove.ro + " COL = " + compMove.col);
        }
        else {
            System.out.println("Computer plays ROW = " + compMove.ro + " NUM AMOUNT OF STARS = " + compMove.col);
        }
        game.makeMove(Game.COMPUTER, compMove.ro, compMove.col);
    }

    public void printBoard() {
        System.out.println(game);
    }

    public void doHumanMove() {
        if (currentGame.equals(nimGame)) {
            boolean legal;
            printBoard();
            do {
                System.out.println("row: ");
                int row = scan.nextInt();
                System.out.println("stars: ");
                int col = scan.nextInt();
                legal = game.makeMove(Game.HUMAN, row, col);
                if (!legal)
                    System.out.println("Illegal move, try again");
            } while (!legal);
        } else {
            boolean legal;
            printBoard();
            do {
                System.out.println("row: ");
                int row = scan.nextInt();
                System.out.println("column: ");
                int col = scan.nextInt();
                legal = game.makeMove(Game.HUMAN, row, col);
                if (!legal)
                    System.out.println("Illegal move, try again");
            } while (!legal);
        }
    }

    // return true if game is continuing, false if done
    boolean checkAndReportStatus() {
        if (game.isAWin(Game.COMPUTER)) {
            System.out.println("Computer Wins");
            return false; // game is done
        }
        if (game.isAWin(Game.HUMAN)) {
            System.out.println("Human Wins");
            return false; // game is done
        }
        if (game.isADraw()) {
            System.out.println("Game is a DRAW");
            return false;
        }
        System.out.println("continuing game...");
        return true;
    }

    public boolean getAndMakeMoves() {
        // let computer go first...
        doComputerMove();
        // System.out.println("count = " + t.getCount());
        if (!checkAndReportStatus())
            return false; // game over
        doHumanMove();
        return checkAndReportStatus(); // game over
    }

    void playOneGame() {
        boolean continueGame = true;
        game.init();
        while (continueGame) {
            continueGame = getAndMakeMoves();
        }
    }

    public static void main(String[] args) {
    // check parameter if its nim or none(which is default == ttt)
    if (!(args.length == 0)) {
        if (args[0].equals(nimGame)) defaultGame = nimGame;
        else {
            System.out.println("to Play Nim: java cs310.games.PlayGame Nim \nto Play TTT: " +
                    "java cs310.games.PlayGame");
            return;
        }
    }
        PlayGame ui = new PlayGame(defaultGame);
        ui.toString();
        ui.playOneGame();

    }
}
