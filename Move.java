/** Represents a move in the game
 * A move consists of a position, a disc being placed, and the state of the board
 * at the time the move was made. This class is used to store move-related data,
 * which can be utilized for functionalities like undoing a move
 */
public class Move {
    // The position on the board where the move is made
    Position position;
    // The disc that is placed during the move
    Disc disc;
    // A snapshot of the board state at the time of this move
    Disc[][] board = new Disc[8][8];
    /** Constructs a Move with the specified position, disc, and board state
     *
     * @param p     The position where the move is made
     * @param disc  The disc being placed in the move
     * @param board A snapshot of the board state at the time of the move
     */
    public Move(Position p, Disc disc, Disc[][] board) {
        this.position = p;
        this.disc = disc;
        this.board = board;
    }
    /** Gets the position where the move was made
     *
     * @return The position of the move
     */
    public Position position() { return position; }
    /** Gets the disc that was placed during the move
     *
     * @return The disc placed in the move
     */
    public Disc disc() { return disc; }
    /** Retrieves the board state at the time the move was made
     *
     * @return A 2D array representing the board state
     */
    public Disc[][] getBoard() {
        return board;
    }

    public void Undo(){  }

}