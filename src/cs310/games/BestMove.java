package cs310.games;

final class BestMove {
    int ro;
    int col;
    int value;

    // value-only constructor: no position information
    public BestMove( int v ) {
        this( v, -6, -7 );
    }  // provide illegal position to detect accidental use

    public BestMove( int v, int r, int c ) {
        value = v; ro = r; col = c;
    }
}