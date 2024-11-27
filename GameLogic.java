import java.util.*;

public class GameLogic implements PlayableLogic{
    private final Disc[][] discsOnBoard = new Disc[8][8];
    private final Disc[][] CopyDiscsOnBoard = new Disc[8][8];
   private ArrayList<Position>haya =new ArrayList<>();

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
                flips(a);
                isFirstPlayerTurn=!isFirstPlayerTurn;

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
                if (next.col()==current.col())return "mid";
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
        Player currentPlayer = isFirstPlayerTurn ? player1 : player2;
    int copyCounter =0;
    int boardCounter =0;

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (discsOnBoard[i][j] != null) {
                if (Objects.equals(discsOnBoard[i][j].getType(), "⬤")){
                CopyDiscsOnBoard[i][j] = new SimpleDisc(discsOnBoard[i][j].getOwner());
                }
                else if (Objects.equals(discsOnBoard[i][j].getType(), "💣")){
                    CopyDiscsOnBoard[i][j] = new BombDisc(discsOnBoard[i][j].getOwner());
                }
                else if (Objects.equals(discsOnBoard[i][j].getType(), "⭕")){
                    CopyDiscsOnBoard[i][j] = new UnflippableDisc(discsOnBoard[i][j].getOwner());
                }
            } else {
                CopyDiscsOnBoard[i][j] = null;
            }
        }
    }

        ArrayList<String> dir = onlyGoodDirection(a);

        for (String direction : dir) {
            Position temp = a;
            for (int j = 0; j < 8; j++) {
                temp = GoInDirection(temp, direction);
                if (!inTheBoard(temp)) break;

                Disc tempDisc = CopyDiscsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) break;
                if (tempDisc.getOwner() == currentPlayer) break;
                if (Objects.equals(tempDisc.getType(), "⬤")) {
                    tempDisc.setOwner(currentPlayer);
                    CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                }
                else if (Objects.equals(tempDisc.getType(), "💣")) {
                    tempDisc.setOwner(currentPlayer);
                    CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                    countFlipBomb(temp);
                    temp =GoInDirection(temp,direction);
                    temp= GoInDirection(temp,direction);
                }
                else if (Objects.equals(tempDisc.getType(), "⭕")) {
                    continue;
                }
            }
        }

    for (int i = 0; i < 8; i++) {
        for (int j = 0; j < 8; j++) {
            if (CopyDiscsOnBoard[i][j]!=null&&inTheBoard(new Position(i,j))&&getDiscAtPositionInCopy(new Position(i,j)).getOwner()==currentPlayer){
                copyCounter++;
            }
            if (discsOnBoard[i][j]!=null&&inTheBoard(new Position(i,j))&&getDiscAtPosition(new Position(i,j)).getOwner()==currentPlayer){
                boardCounter++;
            }

         }
        }
    return copyCounter-boardCounter;

    }
    public Disc getDiscAtPositionInCopy(Position p){
        return CopyDiscsOnBoard[p.row()][p.col()];
    }

    public void countFlipBomb(Position a) {
        Player currentPlayer = isFirstPlayerTurn ? player1 : player2;

        ArrayList<String> dirs = new ArrayList<>();
        dirs.add("upRight");
        dirs.add("up");
        dirs.add("upLeft");
        dirs.add("left");
        dirs.add("downLeft");
        dirs.add("down");
        dirs.add("downRight");
        dirs.add("right");

        for (String direction : dirs) {
            Position temp = a;
            for (int j = 0; j < 1; j++) {
                temp = GoInDirection(temp, direction);

                if (!inTheBoard(temp)) break;
                Disc tempDisc = CopyDiscsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) continue;

                if (tempDisc.getOwner() != currentPlayer) {
                    if (Objects.equals(tempDisc.getType(), "⬤")) {
                        tempDisc.setOwner(currentPlayer);
                        CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                    }
                    else if (Objects.equals(tempDisc.getType(), "💣")) {
                        tempDisc.setOwner(currentPlayer);
                        CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                        countFlipBomb(temp);
                    }
                }
            }
        }
    }




    public ArrayList<String> onlyGoodDirection(Position a) {
        ArrayList<String> newDirections = new ArrayList<>();
        ArrayList<String> directions = getDirections(a);
        for (int i = 0; i < directions.size(); i++) {
            Position temp = a;
            newDirections.add(directions.get(i));
            for (int j = 0; j < 8; j++) {
                temp = GoInDirection(temp, directions.get(i));
                if (!inTheBoard(temp)) {
                    newDirections.removeLast();
                    break;
                }
                Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) {
                    newDirections.removeLast();
                    break;
                }
                if (tempDisc.getOwner() == (isFirstPlayerTurn ? player1 : player2)) {
                    break;
                }
            }
        }
    return newDirections;
    }

    public void flips(Position a) {
        Player currentPlayer = isFirstPlayerTurn ? player1 : player2;
        ArrayList<String> dir = onlyGoodDirection(a);

        for (String direction : dir) {
            Position temp = a;
            for (int j = 0; j < 8; j++) {
                temp = GoInDirection(temp, direction);
                if (!inTheBoard(temp)) break;

                Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) break;
                if (tempDisc.getOwner() == currentPlayer) break;
                if (Objects.equals(tempDisc.getType(), "⬤")) {
                    tempDisc.setOwner(currentPlayer);
                    discsOnBoard[temp.row()][temp.col()] = tempDisc;
                }
                else if (Objects.equals(tempDisc.getType(), "💣")) {
                    tempDisc.setOwner(currentPlayer);
                    discsOnBoard[temp.row()][temp.col()] = tempDisc;
                    flipBomb(temp);
                    temp =GoInDirection(temp,direction);
                    temp= GoInDirection(temp,direction);
                }
                else if (Objects.equals(tempDisc.getType(), "⭕")) {
                    continue;
                }
            }
        }
    }

    public void flipBomb(Position a) {
        Player currentPlayer = isFirstPlayerTurn ? player1 : player2;

        ArrayList<String> dirs = new ArrayList<>();
        dirs.add("upRight");
        dirs.add("up");
        dirs.add("upLeft");
        dirs.add("left");
        dirs.add("downLeft");
        dirs.add("down");
        dirs.add("downRight");
        dirs.add("right");

        for (String direction : dirs) {
            Position temp = a;
            for (int j = 0; j < 1; j++) {
                temp = GoInDirection(temp, direction);

                if (!inTheBoard(temp)) break;
                Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) continue;

                if (tempDisc.getOwner() != currentPlayer) {
                    if (Objects.equals(tempDisc.getType(), "⬤")) {
                        tempDisc.setOwner(currentPlayer);
                        discsOnBoard[temp.row()][temp.col()] = tempDisc;
                    }
                    else if (Objects.equals(tempDisc.getType(), "💣")) {
                        tempDisc.setOwner(currentPlayer);
                        discsOnBoard[temp.row()][temp.col()] = tempDisc;
                        flipBomb(temp);
                    }
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
        List<Position>validMoves = ValidMoves();
        int counterP1 =0;
        int counterP2 =0;
        if (validMoves.isEmpty()){
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                  if (getDiscAtPosition(new Position(i,j))!=null&&getDiscAtPosition(new Position(i,j)).getOwner()==player1){
                      counterP1++;
                  }else counterP2++;

                }
            }
            if (counterP1>counterP2){
                player1.addWin();
            }else if (counterP1<counterP2){
                player2.addWin();
            }
            return true;
        }

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
