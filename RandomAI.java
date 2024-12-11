import java.util.List;
import java.util.Random;

/**
 * Represents an AI player that makes random moves
 * The RandomAI class is a type of AIPlayer that selects a random valid move from the available moves,
 * and also randomly selects the type of disc to place (SimpleDisc, BombDisc, or UnflippableDisc)
 */
public class RandomAI extends AIPlayer {
    /**
     * Constructor to initialize the AI player as either Player One or Player Two
     *
     * @param isPlayerOne A boolean indicating if the AI is Player One (true) or Player Two (false)
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
    }

    /**
     * Makes a move for the AI player. The move is randomly selected from the available valid moves
     * Additionally, the type of disc to place is chosen randomly (SimpleDisc, BombDisc, or UnflippableDisc)
     *
     * @param gameStatus The current status of the game, used to determine the valid moves
     * @return A Move object representing the randomly selected move, or null if no valid moves are available
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Get the list of valid moves for the current game status
        List<Position> validPosition = gameStatus.ValidMoves();
// Determine which player is making the move
        Player currentPlayer = gameStatus.isFirstPlayerTurn() ? gameStatus.getFirstPlayer() : gameStatus.getSecondPlayer();
// If no valid moves exist, return null
        if (validPosition == null || validPosition.isEmpty()) {
            return null;
        }
        // Randomly select a position from the list of valid positions
        Random random = new Random();
        int randomIndex_Position = random.nextInt(validPosition.size());
        Position randomPosition = validPosition.get(randomIndex_Position);
        int randomIndex_DiscType = random.nextInt(3);
        // Randomly select a disc type (SimpleDisc, BombDisc, or UnflippableDisc)
        if (randomIndex_DiscType == 0) {
            // Return a move with a SimpleDisc
            return new Move(randomPosition, new SimpleDisc(currentPlayer), null);
        } else if (randomIndex_DiscType == 1) {
            // Return a move with a BombDisc
            return new Move(randomPosition, new BombDisc(currentPlayer), null);
        }
        if (randomIndex_DiscType == 2) {
            // Return a move with an UnflippableDisc
            return new Move(randomPosition, new UnflippableDisc(currentPlayer), null);
        }
// If no valid move was selected, return null (this should not happen)
        return null;
    }
}
