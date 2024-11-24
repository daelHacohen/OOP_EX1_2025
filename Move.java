public class Move {//צריך לממש את הפונקציות כאן ( עוד לא ממומש)
    Position p;
    Disc disc;

    public Move(Position p, Disc disc) {
        this.p = p;
        this.disc = disc;
    }

    public Position position() { return p; }

    public Disc disc() { return disc; }
   public void Undo(){  }

}