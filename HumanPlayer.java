/** Represents a human player in the game
 * This class extends the abstract Player class and provides implementation
 * specific to a human player. It primarily identifies itself as a human player
 */
public class HumanPlayer extends Player{
    /** Constructs a HumanPlayer with the specified player designation
     *
     * @param isPlayerOne Indicates if this player is Player 1 (true) or Player 2 (false)
     */
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);
    }
    /** Identifies this player as a human
     *
     * @return true, indicating that this player is controlled by a human
     */
    @Override
    boolean isHuman() {
        return true;
    }


}