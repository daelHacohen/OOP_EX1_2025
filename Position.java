/** Represents a position on the game board
 * A position is defined by a row and a column, which specify its coordinates
 * on the board. This class provides methods to access and modify these coordinates
 */
public class Position {
    // The row index of the position on the board
    private int row;
    // The column index of the position on the board
    private int col;
    /** Constructs a Position object with the specified row and column
     *
     * @param row The row index of the position
     * @param col The column index of the position
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    /** Gets the row index of this position
     *
     * @return The row index
     */
    public int row() {
        return row;
    }
    /** Sets the row index of this position
     *
     * @param row The new row index to set
     */
    public void setRow(int row) {
        this.row = row;
    }
    /** Gets the column index of this position
     *
     * @return The column index
     */
    public int col() {
        return col;
    }
    /** Sets the column index of this position
     *
     * @param col The new column index to set
     */
    public void setCol(int col) {
        this.col = col;
    }
}