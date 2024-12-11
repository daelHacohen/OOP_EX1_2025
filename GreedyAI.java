import java.util.List;

/**
 * Represents a Greedy AI player for the game
 * This AI chooses its moves based on the strategy of maximizing the number of
 * opponent discs flipped in a single turn. In the case of ties, it prefers
 * positions with higher column values, and if columns are tied, it prefers
 * positions with higher row values
 */
public class GreedyAI extends AIPlayer {
    /**
     * Constructs a GreedyAI player with the specified player designation
     *
     * @param isPlayerOne Indicates if this AI is Player 1 (true) or Player 2 (false)
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Determines the best move for the GreedyAI based on the current game state
     *
     * @param gameStatus The current game state, implementing PlayableLogic
     * @return The move that maximizes the number of opponent discs flipped
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Get a list of all valid positions where the AI can place a disc
        List<Position> validPosition = gameStatus.ValidMoves();
        // Determine the current player
        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
        // Start by assuming the first valid position is the best
        int theMostFlips = gameStatus.countFlips(validPosition.get(0));
        Position theBestPosition = validPosition.get(0);
        // Iterate through all valid positions to find the best move
        for (int i = 1; i < validPosition.size(); i++) {
            int tempFlips = gameStatus.countFlips(validPosition.get(i));
            // Update the best position if more flips can be achieved
            if (tempFlips > theMostFlips) {
                theMostFlips = tempFlips;
                theBestPosition = validPosition.get(i);
            }
            // In case of a tie in flips, prioritize by column, then by row
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

// Create and return the move at the determined best position
        Move theMostGreedyMove = new Move(theBestPosition, new SimpleDisc(currentPlayer), null);

        return theMostGreedyMove;
    }
}