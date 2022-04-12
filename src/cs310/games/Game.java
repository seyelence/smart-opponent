package cs310.games;

public interface Game {
    int HUMAN = 0;
    int HUMAN_WIN = 0;
    int COMPUTER = 1;
    int COMPUTER_WIN = 3;
    int UNCLEAR = 2;

    BestMove chooseMove(int side, int depth);
    boolean makeMove(int side, int i, int j);
    void init();
    int positionValue();
    boolean isAWin(int side);
    String toString();
    boolean isADraw();
}
