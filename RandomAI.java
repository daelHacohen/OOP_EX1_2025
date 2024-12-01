import java.util.List;
import java.util.Random;

public class RandomAI extends AIPlayer{
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    @Override
    public Move makeMove(PlayableLogic gameStatus) {


        List<Position> validPosition = gameStatus.ValidMoves();

        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();


        if (validPosition == null || validPosition.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int randomIndex_Position = random.nextInt(validPosition.size());
        Position randomPosition = validPosition.get(randomIndex_Position);
        int randomIndex_DiscType = random.nextInt(3);
        if (randomIndex_DiscType==0){return new Move(randomPosition,new SimpleDisc(currentPlayer),null);}
       else if (randomIndex_DiscType==1){return new Move(randomPosition,new BombDisc(currentPlayer),null);}
        if (randomIndex_DiscType==2){return new Move(randomPosition,new UnflippableDisc(currentPlayer),null);}



        return null;
    }
}
