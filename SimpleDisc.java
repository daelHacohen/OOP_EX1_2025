/** Represents a simple disc on the game board
 * A SimpleDisc belongs to a specific player and can be flipped during gameplay
 * This class implements the Disc interface, providing the necessary functionality for a standard game disc
 */
public class SimpleDisc implements Disc{
    // The player who owns this disc.
    private Player owner;
    /** Constructs a SimpleDisc with the specified owner
     *
     * @param owner The player who owns the disc
     */
    public SimpleDisc(Player owner) {
        this.owner = owner;
    }
    /** Gets the owner of this disc
     *
     * @return The player who owns the disc
     */
    @Override
    public Player getOwner() {
        return owner;
    }
    /** Sets the owner of this disc to a new player
     *
     * @param player The new owner of the disc
     */
    @Override
    public void setOwner(Player player) {
        this.owner = player;

    }

    /** Gets the type of this disc
     *
     * @return A string representing the disc's type ("⬤" for a simple disc)
     */
    @Override
    public String getType() {
        return "⬤";
    }
}