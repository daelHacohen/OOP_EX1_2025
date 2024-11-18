public class SimpleDisc implements Disc{
    private Player owner;
    @Override
    public Player getOwner() {
        return owner;
    }

    @Override
    public void setOwner(Player player) {
        this.owner = player;

    }

    @Override
    public String getType() {
        return "⬤";
    }
}
