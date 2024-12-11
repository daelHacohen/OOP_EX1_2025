/**
 * Represents a "Bomb" type disc in the game
 * When a bomb disc is flipped, it detonates and switches the color of the discs is 8 directions
 * This disc type belongs to a specific player and has a special identifier ("💣")
 */
public class BombDisc implements Disc{
    // The player who owns this disc
    private Player owner;

    /** Constructs a BombDisc with the specified owner
     *
     * @param owner The player who owns the disc
     */
    public BombDisc(Player owner) {
        this.owner = owner;
    }

    /** Gets the owner of the disc
     *
     * @return The player who owns the disc
     */
    @Override
    public Player getOwner() {
        return owner;
    }


    /** Sets a new owner for the disc
     *
     * @param player The player who will now own the disc
     */
    @Override
    public void setOwner(Player player) {
        this.owner = player;

    }

    /** Gets the type of the disc
     * The type "💣" indicates this disc is a bomb
     *
     * @return A string representing the type of the disc ("💣")
     */
    @Override
    public String getType() {
        return "💣";
    }
}
