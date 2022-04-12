package cs310.games;
import java.util.*;

class Position {
    private int[][] board;

    public Position(int[][] theBoard) {
        board = new int[3][3];
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = theBoard[i][j];
        }
        public boolean equals(Object rhs) {
            if (!(rhs instanceof Position)) return false;

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    if (board[i][j] != ((Position) rhs).board[i][j])
                        return false;
            return true;
        }

        public int hashCode() {
            int hashVal = 0;

            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    hashVal = hashVal * 4 + board[i][j];

            return hashVal;
        }
    }

    public class TicTacToe implements Game{

        public static final int DRAW = 1;
        public static final int EMPTY = 2;

        // Constructor
        public TicTacToe() {
            init(); // init() replaced clearBoard.
        }

        // removed getBoard() here.
        public String toString() {
            // int[][] board = g.getBoard();
            StringBuilder boardStr = new StringBuilder("");
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    String spot;
                    switch (board[row][col]) {
                        case HUMAN:
                            spot = " " + "X" + " ";
                            break;
                        case COMPUTER:
                            spot = " " + "O" + " ";
                            break;
                        case EMPTY:
                            spot = "   ";
                            break;
                        default:
                            return "Bad board entry in printBoard";
                          //  return spot;
                    }
                    boardStr.append(spot);
                    if (col < 2)
                        boardStr.append("|");
                }
                if (row < 2)
                    boardStr.append("\n-----------\n");
            }
            return boardStr.toString();
        }
        // Find optimal move
        public BestMove chooseMove(int side, int depth) {
            int opp; // The other side
            BestMove reply; // Opponent's best reply
            int simpleEval; // Result of an immediate evaluation
            int bestRow = -1; // Initialize running value with out-of-range value
            int bestColumn = -1;
            int value;
            Position thisPosition = new Position(board);

            if ((simpleEval = positionValue()) != UNCLEAR)
                return new BestMove(simpleEval);

            // Don't look up top-level value: at top level, we need to explore moves
            // out from here to find the best move to make)
            if (depth > 0) {
                Integer lookupVal = store.get(thisPosition);
                if (lookupVal != null)
                    return new BestMove(lookupVal);
            }
            // Initialize running values with out-of-range values (good software practice)
            // Here also to ensure that *some* move is chosen, even if a hopeless case
            if (side == COMPUTER) {
                opp = HUMAN;
                value = HUMAN_WIN - 1; // impossibly low value
            } else {
                opp = COMPUTER;
                value = COMPUTER_WIN + 1; // impossibly high value
            }

            for (int row = 0; row < 3; row++)
                for (int column = 0; column < 3; column++)
                    if (squareIsEmpty(row, column)) {
                        place(row, column, side);
                        reply = chooseMove(opp, depth+1);
                        place(row, column, EMPTY);
                        // Update if side gets better position
                        if (side == COMPUTER && reply.value > value || side == HUMAN && reply.value < value) {
                            value = reply.value;
                            bestRow = row;
                            bestColumn = column;
                        }
                    }
            store.put(thisPosition, value);
            return new BestMove(value, bestRow, bestColumn);
        }

        // Play move, including checking legality
        public boolean makeMove(int side, int row, int column) {
            if (row < 0 || row >= 3 || column < 0 || column >= 3 || board[row][column] != EMPTY)
                return false;
            board[row][column] = side;
            return true;
        }

        public void init() {
            for (int i = 0; i < 3; i++)
                for (int j = 0; j < 3; j++)
                    board[i][j] = EMPTY;
            }

        // added by eoneil: more general-purpose than "boardIsFull"
        public boolean isADraw() {
            return !isAWin(HUMAN)&&!isAWin(COMPUTER)&&boardIsFull();
        }

        public boolean isAWin(int side) {
            int row, column;

            /* Look for all in a row */
            for (row = 0; row < 3; row++) {
                for (column = 0; column < 3; column++)
                    if (board[row][column] != side)
                        break;
                if (column >= 3)
                    return true;
            }

            /* Look for all in a column */
            for (column = 0; column < 3; column++) {
                for (row = 0; row < 3; row++)
                    if (board[row][column] != side)
                        break;
                if (row >= 3)
                    return true;
            }

            /* Look on diagonals */
            if (board[1][1] == side && board[2][2] == side && board[0][0] == side)
                return true;

            if (board[0][2] == side && board[1][1] == side && board[2][0] == side)
                return true;

            return false;
        }


        private int[][] board = new int[3][3];
        private Map<Position, Integer> store = new HashMap<cs310.games.Position, Integer>();

        // made private by eoneil: now a helper to isADraw of public API
        private boolean boardIsFull() {
            for (int row = 0; row < 3; row++)
                for (int column = 0; column < 3; column++)
                    if (board[row][column] == EMPTY)
                        return false;
            return true;
        }
        // Play a move, possibly clearing a square
        private void place(int row, int column, int piece) {
            board[row][column] = piece;
        }

        private boolean squareIsEmpty(int row, int column) {
            return board[row][column] == EMPTY;
        }

        // Compute static value of current position (win, draw, etc.)
        public int positionValue() {
            return isAWin(COMPUTER) ? COMPUTER_WIN : isAWin(HUMAN) ? HUMAN_WIN : boardIsFull() ? DRAW : UNCLEAR;
        }
    }
