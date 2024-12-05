import java.util.List;

public class GreedyAI extends AIPlayer{
    public GreedyAI(boolean isPlayerOne) {super(isPlayerOne);}

    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        List<Position>validPosition = gameStatus.ValidMoves();


        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();

        int theMostFlips = gameStatus.countFlips(validPosition.get(0));
        Position theBestPosition = validPosition.get(0);
        for (int i = 1; i < validPosition.size(); i++) {
            int tempFlips = gameStatus.countFlips(validPosition.get(i));
            if (tempFlips>theMostFlips){
                theMostFlips=tempFlips;
                theBestPosition=validPosition.get(i);
            }
            if (theMostFlips == tempFlips) {
                if (validPosition.get(i).col() > theBestPosition.col()) {
                    theBestPosition = validPosition.get(i);
                } else if (validPosition.get(i).col() == theBestPosition.col()) {
                    if (validPosition.get(i).row() > theBestPosition.row()) {
                        theBestPosition = validPosition.get(i);
                    }
                }
            }
        }


Move theMostGreedyMove = new Move(theBestPosition,new SimpleDisc(currentPlayer),null);

        return theMostGreedyMove;
    }
}
