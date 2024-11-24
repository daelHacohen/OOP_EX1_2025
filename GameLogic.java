import java.util.ArrayList;
import java.util.List;

public class GameLogic implements PlayableLogic{
    private final Disc[][] discsOnBoard = new Disc[8][8];

   private Player player1;
    private Player player2;
    @Override
    public boolean locate_disc(Position a, Disc disc) {//ניסיתי לעשות  את הפונקציה לא יודע אם זה עבד.
        List<Position>positionList = ValidMoves();
        for (int i = 0; i < positionList.size(); i++) {
            if (positionList.get(i).col()==a.col()&&positionList.get(i).row()==a.row()){
                discsOnBoard[a.row()][a.col()]=disc;
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
        if (p.row()>=0&&p.row()<=8){
            if (p.col()<=8&&p.col()>=0){
                return true;
            }
        }
        return false;
    }
public boolean validMove(Move move){
        if (inTheBoard(move.position())&& discsOnBoard[move.position().row()][move.position().col()]==null){
            for (int i = move.position().row()-1; i <move.position().row()+3 ; i++) {
                for (int j = move.position().col()-1; j <move.position().col()+3 ; j++) {
                    if ()

                }

            }

        }
}

    @Override
    public List<Position> ValidMoves() {
        List<Position>possiblePositions =new ArrayList<>();

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
        return false;
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
