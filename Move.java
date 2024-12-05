public class Move {//צריך לממש את הפונקציות כאן ( עוד לא ממומש)
    Position position;
    Disc disc;
    Disc[][] board = new Disc[8][8];

    public Move(Position p, Disc disc, Disc[][] board) {
        this.position = p;
        this.disc = disc;
        this.board = board;
    }


    public Position position() { return position; }

    public Disc disc() { return disc; }

    public Disc[][] getBoard() {
        return board;
    }

    public void Undo(){  }

}