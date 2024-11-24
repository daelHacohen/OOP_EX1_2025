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
    public boolean locate_disc(Position a, Disc disc) {//ניסיתי לעשות  את הפונקציה לא יודע אם זה עבד.
        List<Position>positionList = ValidMoves();

        for (int i = 0; i < positionList.size(); i++) {
            if (positionList.get(i).col()==a.col()&&positionList.get(i).row()==a.row()){
                discsOnBoard[a.row()][a.col()]=disc;
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
                if (inTheBoard(new Position(i,j))){
                    if(discsOnBoard[i][j]!=null&&isFirstPlayerTurn()&&getDiscAtPosition(new Position(i,j)).getOwner()==player2){
                    directions.add(direction(p,new Position(i,j)));}
                }else if (inTheBoard(new Position(i,j))){
                    if(discsOnBoard[i][j]!=null&&isFirstPlayerTurn()==false&&getDiscAtPosition(new Position(i,j)).getOwner()==player1){
                        directions.add(direction(p,new Position(i,j)));
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
            for (int i = 0; i < directions.size(); i++) {
                Position temp = new Position(position.row(),position.col());
                for (int j = 0; j < 8; j++) {
                   temp=GoInDirection(temp,directions.get(i));
                   if (!inTheBoard(temp)) {break;}
                    if (discsOnBoard[temp.row()][temp.col()] == null) {break;}
                   if (discsOnBoard[temp.row()][temp.col()].getOwner()==getDiscAtPosition(position).getOwner())return true;
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
        return 0;
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
    }

    @Override
    public void undoLastMove() {

    }
}
