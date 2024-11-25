import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic{
    private final Disc[][] discsOnBoard = new Disc[8][8];

   private Player player1;
    private Player player2;
   private boolean isFirstPlayerTurn;

    public GameLogic() {
        this.isFirstPlayerTurn = true;
    }

    @Override
    public boolean locate_disc(Position a, Disc disc) {
        List<Position>positionList = ValidMoves();

        for (int i = 0; i < positionList.size(); i++) {
            if (positionList.get(i).col()==a.col()&&positionList.get(i).row()==a.row()){
                discsOnBoard[a.row()][a.col()]=disc;
                isFirstPlayerTurn=!isFirstPlayerTurn;
                flips(a);
                return true;
                }
        }

        return false;
    }

    @Override
    public Disc getDiscAtPosition(Position position) {
        if (discsOnBoard[position.row()][position.col()]==null)
        return null;
        else return discsOnBoard[position.row()][position.col()];
    }

    @Override
    public int getBoardSize() {

        return discsOnBoard.length;
    }
    public boolean inTheBoard(Position p){
        if (p.row()>=0&&p.row()<8){
            if (p.col()<8&&p.col()>=0){
                return true;
            }
        }
        return false;
    }
    public String direction (Position current, Position next){
        if (inTheBoard(current)&&inTheBoard(next)){
            if (next.row()==current.row()-1){
                if (next.col()==current.col()-1)return "upLeft";
                else if (next.col() == current.col()) return "up";
                else if (next.col() == current.col() + 1) return "upRight";
            } else if (next.row() == current.row()) {
                if (next.col()==current.col()-1)return "left";
                else if (next.col() == current.col() + 1) return "right";
            } else if (next.row()==current.row()+1) {
                if (next.col()==current.col()-1)return "downLeft";
                else if (next.col()==current.col())return "down";
                else if (next.col()==current.col()+1)return "downRight";

            }
        }
        return null;
    }
    public ArrayList<String> getDirections(Position p){
        ArrayList<String>directions =new ArrayList<>();
        for (int i = p.row()-1; i <p.row()+3 ; i++) {
            for (int j = p.col()-1; j <p.col()+3 ; j++) {
                Position neighbor = new Position(i, j);
                if (inTheBoard(new Position(i,j))&&discsOnBoard[i][j]!=null){
                    Disc disc = discsOnBoard[i][j];
                    if ((isFirstPlayerTurn && disc.getOwner() == player2) ||
                            (!isFirstPlayerTurn && disc.getOwner() == player1)) {
                        String dir = direction(p, neighbor);
                        if (dir != null) directions.add(dir);
                    }
                }
            }
        }
        return directions;

    }
    public Position GoInDirection(Position p,String str){
        if (str.equals("upLeft"))return new Position(p.row()-1,p.col()-1);
        else if (str.equals("up"))return new Position(p.row()-1,p.col());
        else if (str.equals("upRight"))return new Position(p.row()-1,p.col()+1);
        else if (str.equals("left"))return new Position(p.row(),p.col()-1);
        else if (str.equals("right"))return new Position(p.row(),p.col()+1);
        else if (str.equals("downLeft"))return new Position(p.row()+1,p.col()-1);
        else if (str.equals("down"))return new Position(p.row()+1,p.col());
        else if (str.equals("downRight"))return new Position(p.row()+1,p.col()+1);
        return null;
    }
public boolean isvalidMove(Position position){
        if (inTheBoard(position)&& discsOnBoard[position.row()][position.col()]==null){
         ArrayList<String>directions =getDirections(position);
         if (directions.isEmpty())return false;
            for (String direction : directions) {
                Position temp = position;
                for (int j = 0; j < 8; j++) {
                    temp = GoInDirection(temp, direction);
                    if (!inTheBoard(temp)) {
                        break;
                    }
                    Disc tempDisc = discsOnBoard [temp.row()][temp.col()];
                    if (tempDisc == null) {
                        break;
                    }
                    if (tempDisc.getOwner() == (isFirstPlayerTurn ? player1 : player2))
                        return true;
                }
            }
        }
        return false;
}

    @Override
    public List<Position> ValidMoves() {
        List<Position>possiblePositions =new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p =new Position(i,j);
                if (isvalidMove(p)){
                    possiblePositions.add(p);
                }
            }
        }
        return possiblePositions;
    }

    @Override
    public int countFlips(Position a) {

    int counter = 0;
    int allCounters=0;
        if (inTheBoard(a)&&isvalidMove(a)){
            if (isvalidMove(a)){
                ArrayList<String> directions= getDirections(a);
                for (String direction : directions) {
                    Position temp = a;
                    for (int j = 0; j < 8; j++) {
                        temp = GoInDirection(temp, direction);
                        if (!inTheBoard(temp)) {
                            counter = 0;
                            break;
                        }
                        Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                        if (tempDisc == null) {
                            counter = 0;
                            break;
                        }
                        if (tempDisc.getOwner() != (isFirstPlayerTurn ? player1 : player2)) {
                            counter++;
                        }
                        if (tempDisc.getOwner() == (isFirstPlayerTurn ? player1 : player2)){
                            allCounters =allCounters+counter;
                            break;
                        }

                    }
                 }
            }
        }

        return allCounters;
    }
    public ArrayList<String> onlyGoodDirection(Position a) {
       ArrayList<String>newDirections = new ArrayList<>();
        if (inTheBoard(a)&&isvalidMove(a)){
            if (isvalidMove(a)){
                ArrayList<String> directions= getDirections(a);
                for (String direction : directions) {
                    Position temp = a;
                    newDirections.add(direction);
                    for (int j = 0; j < 8; j++) {
                        temp = GoInDirection(temp, direction);
                        if (!inTheBoard(temp)) {
                            newDirections.removeLast();
                            break;
                        }
                        Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                        if (tempDisc == null) {
                            newDirections.removeLast();
                            break;
                        }
                        if (tempDisc.getOwner() == (isFirstPlayerTurn ? player1 : player2)){
                            break;
                        }

                    }
                }
            }
        }

        return newDirections;
    }

    public void flips (Position a){
        Player currentPlayer;
        if (isFirstPlayerTurn) {
            currentPlayer = player1;
        } else {
            currentPlayer = player2;
        }
        ArrayList<String> dir =onlyGoodDirection(a);
        for (String direction : dir) {
            Position temp = a;
            for (int j = 0; j < 8; j++) {
                temp = GoInDirection(temp, direction);
                if (!inTheBoard(temp)) {
                    break;
                }
                Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) {
                    break;
                }
                if (tempDisc.getOwner() == (isFirstPlayerTurn ? player1 : player2)){
                    break;
                }
                if (tempDisc.getOwner() != (isFirstPlayerTurn ? player1 : player2)){
                   Disc newDisc =getDiscAtPosition(temp);
                   newDisc.setOwner(currentPlayer);
                   discsOnBoard[temp.row()][temp.col()]=newDisc;
                }

            }
        }
    }



    @Override
    public Player getFirstPlayer() {return player1;}

    @Override
    public Player getSecondPlayer() {return player2;}

    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    @Override
    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    @Override
    public boolean isGameFinished() {
        return false;
    }

    @Override
    public void reset() {
        for (int i = 0; i < discsOnBoard.length; i++) {
            for (int j = 0; j < discsOnBoard.length; j++) {
                discsOnBoard[i][j]=null;
            }
        }
        SimpleDisc d1 =new SimpleDisc(player1);
        SimpleDisc d2 =new SimpleDisc(player1);
        SimpleDisc d3 =new SimpleDisc(player2);
        SimpleDisc d4 =new SimpleDisc(player2);
        discsOnBoard[3][3]=d1;
        discsOnBoard[4][4]=d2;
        discsOnBoard[3][4]=d3;
        discsOnBoard[4][3]=d4;

        isFirstPlayerTurn=true;
    }

    @Override
    public void undoLastMove() {

    }
}
