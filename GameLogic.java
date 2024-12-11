import java.util.*;

public class GameLogic implements PlayableLogic {
    private final Disc[][] discsOnBoard = new Disc[8][8];
    private final Disc[][] CopyDiscsOnBoard = new Disc[8][8];
    private ArrayList<Position> haya = new ArrayList<>();

    private Stack<Move> previousMoves = new Stack<>();


    private Player player1;
    private Player player2;
    private boolean isFirstPlayerTurn;

    public GameLogic() {
        this.isFirstPlayerTurn = true;
    }

    /**
     * This method lets the user place a disc on the game's board if the move is valid
     *
     * @param a    The position where the disc is to be placed.
     * @param disc The disc object to place, which can be of different types ( SimpleDisc, BombDisc, UnflippableDisc)
     * @return true if the disc placing was a success, otherwise false.
     */
    @Override
    public boolean locate_disc(Position a, Disc disc) {
        // Get the list of valid positions where a disc can be placed
        List<Position> positionList = ValidMoves();

        // Check if the current player has remaining "BombDisc" to use
        int bombsLeft = disc.getOwner().equals(player1) ? player1.number_of_bombs : player2.number_of_bombs;
        if (disc instanceof BombDisc && bombsLeft == 0) {
            return false;
        }

        // Check if the current player has remaining "UnflippableDisc" to use
        int unflipabaleLeft = disc.getOwner().equals(player1) ? player1.number_of_unflippedable : player2.number_of_unflippedable;
        if (disc instanceof UnflippableDisc && unflipabaleLeft == 0) {
            return false;
        }

        // Iterate through valid positions to check if the move is valid
        for (int i = 0; i < positionList.size(); i++) {
            if (positionList.get(i).col() == a.col() && positionList.get(i).row() == a.row()) {

                // Place the disc on the board at the specified position
                discsOnBoard[a.row()][a.col()] = disc;
                System.out.println(getPlayer() + "placed a " + disc.getType() + " in (" + a.row() + ", " + a.col() + ")");

                // Flip the appropriate discs according to game rules
                flips(a);

                // Manages the amount of special discs of each of the two players
                if (isFirstPlayerTurn) {
                    if (disc instanceof BombDisc) {
                        // Reduce the amount of BombDisc of Player1 by one
                        player1.reduce_bomb();
//                        System.out.println("player1 num of bomb:"+ player1.number_of_bombs);
                    }
                    if (disc instanceof UnflippableDisc) {
                        // Reduce the amount of UnflippableDisc of PLayer1 by one
                        player1.reduce_unflippedable();
//                        System.out.println("player1 num of unflippable:"+ player1.number_of_unflippedable);
                    }
                } else {
                    // Means it is Player2's turn
                    if (disc instanceof BombDisc) {
                        // Reduce the amount of BombDisc of Player2 by one
                        player2.reduce_bomb();
//                        System.out.println("player2 num of bomb:"+ player2.number_of_bombs);
                    }
                    if (disc instanceof UnflippableDisc) {
                        // Reduce the amount of UnflippableDisc of PLayer2 by one
                        player2.reduce_unflippedable();
//                        System.out.println( "player2 num of unflippable:"+ player2.number_of_unflippedable);
                    }
                }

                // Create a snapshot of the current board state
                Disc[][] current = new Disc[8][8];
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if (discsOnBoard[x][y] != null) {
                            // Create a copy of the disc based on its type
                            if (Objects.equals(discsOnBoard[x][y].getType(), "⬤")) {
                                current[x][y] = new SimpleDisc(discsOnBoard[x][y].getOwner());
                            } else if (Objects.equals(discsOnBoard[x][y].getType(), "💣")) {
                                current[x][y] = new BombDisc(discsOnBoard[x][y].getOwner());
                            } else if (Objects.equals(discsOnBoard[x][y].getType(), "⭕")) {
                                current[x][y] = new UnflippableDisc(discsOnBoard[x][y].getOwner());
                            }
                        } else {
                            current[x][y] = null;
                        }
                    }
                }
                // Record the move in the history for undo option (when pressing "back")
                Move move = new Move(a, disc, current);
                previousMoves.push(move);


                // Switch to Player2's turn
                isFirstPlayerTurn = !isFirstPlayerTurn;
                System.out.println();

                return true; // Disc has been successfully placed
            }
        }

        return false; // Move wasn't valid
    }

    /**
     * This method returns a string letting us know which player's turn is the current one
     *
     * @return which player is actually playing
     */
    public String getPlayer() {
        if (isFirstPlayerTurn) return "Player 1 ";
        else return "Player 2 ";
    }

    /**
     * This method let us know which Disc is located for a given position
     *
     * @param position The position for which to retrieve the disc.
     * @return The Disc at the position, or null if the position is empty
     */
    @Override
    public Disc getDiscAtPosition(Position position) {
        if (discsOnBoard[position.row()][position.col()] == null)
            return null;
        else return discsOnBoard[position.row()][position.col()];
    }

    /**
     * This method let us know the size of the game board by using the discsOnBoard array's length
     * which is always 8*8 in a standard reversi game
     *
     * @return the game board size (always 8*8)
     */
    @Override
    public int getBoardSize() {

        return discsOnBoard.length;
    }

    /**
     * Side method that helps us in other methods to check if a position is legal (bounded by 8*8)
     *
     * @param p is the position we want to check
     * @return true if the position is part of the game board
     */
    public boolean inTheBoard(Position p) {
        if (p.row() >= 0 && p.row() < 8) {
            if (p.col() < 8 && p.col() >= 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method determines the relative direction from one position to another on the board
     *
     * @param current is the current position on the board
     * @param next    is the neighbouring position to compare with
     * @return A string representing the direction ("up", "down", "left", "right", etc.)
     * *         or null if the positions are not valid or not adjacent
     */
    public String direction(Position current, Position next) {
        if (inTheBoard(current) && inTheBoard(next)) {
            if (next.row() == current.row() - 1) { // One row above
                if (next.col() == current.col() - 1) return "upLeft";
                else if (next.col() == current.col()) return "up";
                else if (next.col() == current.col() + 1) return "upRight";
            } else if (next.row() == current.row()) { //same row
                if (next.col() == current.col() - 1) return "left";
                if (next.col() == current.col()) return "mid";
                else if (next.col() == current.col() + 1) return "right";
            } else if (next.row() == current.row() + 1) { // One row below
                if (next.col() == current.col() - 1) return "downLeft";
                else if (next.col() == current.col()) return "down";
                else if (next.col() == current.col() + 1) return "downRight";

            }
        }
        return null;
    }

    /**
     * Finds all valid directions from a given position where neighboring discs belong to the opponent.
     * This method scans the board around the given position, identifying any adjacent opponent discs
     * and calculating their directions relative to the current position
     *
     * @param p The current position on the board
     * @return A list of strings representing the valid directions
     */
    public ArrayList<String> getDirections(Position p) {
        ArrayList<String> directions = new ArrayList<>();
        for (int i = p.row() - 1; i < p.row() + 3; i++) { // Check surrounding rows
            for (int j = p.col() - 1; j < p.col() + 3; j++) { // Check surrounding columns
                Position neighbor = new Position(i, j);
                if (inTheBoard(new Position(i, j)) && discsOnBoard[i][j] != null) {
                    Disc disc = discsOnBoard[i][j];
                    // Check if the disc belongs to the opponent
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

    /**
     * Moves from a given position in a specific direction and returns the resulting position
     *
     * @param p   The starting position on the board
     * @param str The direction to move in
     * @return The new position after moving in the specified direction, or null if the direction is invalid
     */
    public Position GoInDirection(Position p, String str) {
        if (str.equals("upLeft")) return new Position(p.row() - 1, p.col() - 1);
        else if (str.equals("up")) return new Position(p.row() - 1, p.col());
        else if (str.equals("upRight")) return new Position(p.row() - 1, p.col() + 1);
        else if (str.equals("left")) return new Position(p.row(), p.col() - 1);
        else if (str.equals("right")) return new Position(p.row(), p.col() + 1);
        else if (str.equals("downLeft")) return new Position(p.row() + 1, p.col() - 1);
        else if (str.equals("down")) return new Position(p.row() + 1, p.col());
        else if (str.equals("downRight")) return new Position(p.row() + 1, p.col() + 1);
        return null;
    }

    /**
     * Determines if a given position is a valid move for the current player
     * A move is valid if:
     * - The position is within the board
     * - The position is empty
     * - Placing a disc at this position would flip at least one opponent disc
     *
     * @param position The position to check
     * @return true if the move is valid, false otherwise
     */
    public boolean isvalidMove(Position position) {

        if (inTheBoard(position) && discsOnBoard[position.row()][position.col()] == null) {
            int counterFlip = countFlips(position);
            if (counterFlip <= 0) return false;
            ArrayList<String> directions = getDirections(position);
            if (directions.isEmpty()) return false;
            for (String direction : directions) {
                Position temp = position;
                for (int j = 0; j < 8; j++) {
                    temp = GoInDirection(temp, direction);
                    if (!inTheBoard(temp)) {
                        break;
                    }
                    Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
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

    /**
     * Calculates and returns all valid moves for the current player
     * The method iterates through every position on the board and checks if it's a valid move
     * using the isvalidMove() method
     *
     * @return A list of positions representing all valid moves for the current player
     */
    @Override
    public List<Position> ValidMoves() {
        List<Position> possiblePositions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Position p = new Position(i, j);
                if (isvalidMove(p)) {
                    possiblePositions.add(p);
                }
            }
        }
        return possiblePositions;
    }


    /**
     * Calculates the number of discs that would be flipped if a disc were placed at the given position.
     * This method simulates placing a disc at the specified position and determines how many opponent discs
     * would change ownership based on the game rules. The method considers all directions where flips are possible
     * We use this method in multiple other methods (showing on the screen how many disc would get flipped by placing
     * on a given spot , GreedyAi...)
     *
     * @param a The position where the disc would be placed.
     * @return The number of discs that would be flipped.
     */
    @Override
    public int countFlips(Position a) {
        Player currentPlayer = isFirstPlayerTurn ? player1 : player2;
        int copyCounter = 0;
        int boardCounter = 0;
        // Copy the current board state to simulate flips.

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (discsOnBoard[i][j] != null) {
                    if (Objects.equals(discsOnBoard[i][j].getType(), "⬤")) {
                        CopyDiscsOnBoard[i][j] = new SimpleDisc(discsOnBoard[i][j].getOwner());
                    } else if (Objects.equals(discsOnBoard[i][j].getType(), "💣")) {
                        CopyDiscsOnBoard[i][j] = new BombDisc(discsOnBoard[i][j].getOwner());
                    } else if (Objects.equals(discsOnBoard[i][j].getType(), "⭕")) {
                        CopyDiscsOnBoard[i][j] = new UnflippableDisc(discsOnBoard[i][j].getOwner());
                    }
                } else {
                    CopyDiscsOnBoard[i][j] = null;
                }
            }
        }

        ArrayList<String> dir = onlyGoodDirection(a);
// Simulate flips in each valid direction
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
                } else if (Objects.equals(tempDisc.getType(), "💣")) {
                    tempDisc.setOwner(currentPlayer);
                    CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                    countFlipBomb(temp);
                    temp = GoInDirection(temp, direction);
                } else if (Objects.equals(tempDisc.getType(), "⭕")) {
                    continue;
                }
            }
        }
// Compare the number of discs owned before and after the simulation
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (CopyDiscsOnBoard[i][j] != null && inTheBoard(new Position(i, j)) && getDiscAtPositionInCopy(new Position(i, j)).getOwner() == currentPlayer) {
                    copyCounter++;
                }
                if (discsOnBoard[i][j] != null && inTheBoard(new Position(i, j)) && getDiscAtPosition(new Position(i, j)).getOwner() == currentPlayer) {
                    boardCounter++;
                }

            }
        }
        return copyCounter - boardCounter;

    }

    /**
     * This method retrieves the disc at the specified position from the copied board state
     * it is used for simulating board changes during operations such as counting flips
     *
     * @param p The position to retrieve the disc from.
     * @return The disc at the specified position on the copied board, or null if no disc is present
     */
    public Disc getDiscAtPositionInCopy(Position p) {
        return CopyDiscsOnBoard[p.row()][p.col()];
    }

    /**
     * Simulates the effect of flipping a bomb disc at the given position
     * When a bomb disc is flipped, it affects neighboring discs in all 8 directions
     * This method recursively handles chains of bomb flips
     *
     * @param a The position of the bomb disc being flipped
     */
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
            for (int j = 0; j < 1; j++) { // Only affects immediate neighbors
                temp = GoInDirection(temp, direction);

                if (!inTheBoard(temp)) break;
                Disc tempDisc = CopyDiscsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) continue;

                if (tempDisc.getOwner() != currentPlayer) {
                    if (Objects.equals(tempDisc.getType(), "⬤")) {
                        tempDisc.setOwner(currentPlayer);
                        CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                    } else if (Objects.equals(tempDisc.getType(), "💣")) {
                        tempDisc.setOwner(currentPlayer);
                        CopyDiscsOnBoard[temp.row()][temp.col()] = tempDisc;
                        countFlipBomb(temp); // Recursively handle chain reactions
                    }
                }
            }
        }
    }


    /**
     * Filters the possible directions to only the one where flips can actually occur
     * A direction is considered "good" if it leads to flipping at least one opponent's disc
     *
     * @param a The position where a disc would be placed.
     * @return A list of valid directions for flipping discs.
     */
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

    /**
     * Executes the flipping of discs for a valid move at the given position
     * This method modifies the board state by changing the ownership of discs
     * that are flipped as a result of placing a disc at the specified position
     *
     * @param a The position where the disc is placed
     */
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
                    System.out.println(getPlayer() + "flipped the " + tempDisc.getType() + " in (" + temp.row() + ", " + temp.col() + ")");
                    tempDisc.setOwner(currentPlayer);
                    discsOnBoard[temp.row()][temp.col()] = tempDisc;
                } else if (Objects.equals(tempDisc.getType(), "💣")) {
                    System.out.println(getPlayer() + "flipped the " + tempDisc.getType() + " in (" + temp.row() + ", " + temp.col() + ")");
                    tempDisc.setOwner(currentPlayer);
                    discsOnBoard[temp.row()][temp.col()] = tempDisc;
                    flipBomb(temp); // Handle bomb chain reaction
                    temp = GoInDirection(temp, direction); // Continue to the next position in this direction
                } else if (Objects.equals(tempDisc.getType(), "⭕")) {
                    continue; // Skip unflippable discs
                }
            }
        }
    }

    /**
     * Handles the flipping of discs around a bomb disc when it is flipped
     * Similar to countFlipBomb, but directly modifies the actual board state instead of a copy
     *
     * @param a The position of the bomb disc being flipped
     */
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
            for (int j = 0; j < 1; j++) { // Only affects immediate neighbors
                temp = GoInDirection(temp, direction);

                if (!inTheBoard(temp)) break;
                Disc tempDisc = discsOnBoard[temp.row()][temp.col()];
                if (tempDisc == null) continue;

                if (tempDisc.getOwner() != currentPlayer) {
                    if (Objects.equals(tempDisc.getType(), "⬤")) {
                        System.out.println(getPlayer() + "flipped the " + tempDisc.getType() + " in (" + temp.row() + ", " + temp.col() + ")");
                        tempDisc.setOwner(currentPlayer);
                        discsOnBoard[temp.row()][temp.col()] = tempDisc;
                    } else if (Objects.equals(tempDisc.getType(), "💣")) {
                        System.out.println(getPlayer() + "flipped the " + tempDisc.getType() + " in (" + temp.row() + ", " + temp.col() + ")");
                        tempDisc.setOwner(currentPlayer);
                        discsOnBoard[temp.row()][temp.col()] = tempDisc;
                        flipBomb(temp); // Recursively handle chain reactions
                    }
                }
            }
        }
    }

    /**
     * Returns the first player in the game.
     *
     * @return The first player
     */
    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    /**
     * Returns the second player in the game.
     *
     * @return The second player
     */
    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    /**
     * Sets the players for the game
     *
     * @param player1 The first player
     * @param player2 The second player
     */
    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    /**
     * Determines if it is the first player's turn
     *
     * @return true if it is the first player's turn, false otherwise
     */
    @Override
    public boolean isFirstPlayerTurn() {
        return isFirstPlayerTurn;
    }

    /**
     * Checks if the game is finished.
     * The game ends if there are no valid moves left. The method calculates
     * the number of discs owned by each player and determines the winner
     *
     * @return true if the game is finished, false otherwise
     */
    @Override
    public boolean isGameFinished() {
        List<Position> validMoves = ValidMoves();
        int counterP1 = 0;
        int counterP2 = 0;
        if (validMoves.isEmpty()) {
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {

                    if (getDiscAtPosition(new Position(i, j)) != null && getDiscAtPosition(new Position(i, j)).getOwner() == player1) {
                        counterP1++;
                    } else counterP2++;

                }
            }
            if (counterP1 > counterP2) {
                System.out.println("Player 1 wins with " + counterP1 + " discs! Player 2 had " + counterP2 + " discs");
                player1.addWin();
            } else if (counterP1 < counterP2) {
                System.out.println("Player 2 wins with " + counterP2 + " discs! Player 1 had " + counterP1 + " discs");
                player2.addWin();
            }
            return true;
        }

        return false;
    }

    /**
     * Resets the game to its initial state
     * Resets players's special disc counts, clears the board, and reinitializes
     * the starting positions
     * It also resets the turn to the first player and clears
     * the move history
     */
    @Override
    public void reset() {
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
        for (int i = 0; i < discsOnBoard.length; i++) {
            for (int j = 0; j < discsOnBoard.length; j++) {
                discsOnBoard[i][j] = null;
            }
        }
        // Set up initial discs on the board
        SimpleDisc d1 = new SimpleDisc(player1);
        SimpleDisc d2 = new SimpleDisc(player1);
        SimpleDisc d3 = new SimpleDisc(player2);
        SimpleDisc d4 = new SimpleDisc(player2);
        discsOnBoard[3][3] = d1;
        discsOnBoard[4][4] = d2;
        discsOnBoard[3][4] = d3;
        discsOnBoard[4][3] = d4;

        isFirstPlayerTurn = true;

        previousMoves.clear();
// Create a new board state for the first move
        Disc[][] dael = new Disc[8][8];

        for (int i = 0; i < dael.length; i++) {
            for (int j = 0; j < dael.length; j++) {
                dael[i][j] = null;
            }
        }
        SimpleDisc d11 = new SimpleDisc(player1);
        SimpleDisc d22 = new SimpleDisc(player1);
        SimpleDisc d33 = new SimpleDisc(player2);
        SimpleDisc d44 = new SimpleDisc(player2);
        dael[3][3] = d11;
        dael[4][4] = d22;
        dael[3][4] = d33;
        dael[4][3] = d44;

        // Save the initial move
        Move firstMove = new Move(null, d11, dael);
        previousMoves.push(firstMove);

    }

    /** Undoes the last move made in the game
     * Restores the board to the state before the last move. If the last move involved
     * flipping discs, their ownership is reverted. Special disc counts (bombs, unflippable discs)
     * are also adjusted
     * If no previous move is available, an appropriate message is displayed
     *
     */
    @Override
    public void undoLastMove() {
        System.out.println("Undoing last move :");
        if (previousMoves.size() > 1) {

            Move lastMove = previousMoves.pop();
            Disc[][] dael = previousMoves.peek().getBoard();
            System.out.println("\tUndo: removing " + lastMove.disc.getType() + " from (" + lastMove.position.row() + ", " + lastMove.position.col() + ")");

            // Restore the board state
            for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 8; j++) {
                    if (dael[i][j] != null) {
                        if (Objects.equals(dael[i][j].getType(), "⬤")) {
                            if (getDiscAtPosition(new Position(i, j)).getOwner() != dael[i][j].getOwner()) {
                                System.out.println("\tUndo: flipping back ⬤ in (" + i + " ," + j + ")");
                            }
                            discsOnBoard[i][j] = new SimpleDisc(dael[i][j].getOwner());
                        } else if (Objects.equals(dael[i][j].getType(), "💣")) {
                            if (getDiscAtPosition(new Position(i, j)).getOwner() != dael[i][j].getOwner()) {
                                System.out.println("\tUndo: flipping back 💣 in (" + i + " ," + j + ")");
                            }
                            discsOnBoard[i][j] = new BombDisc(dael[i][j].getOwner());
                        } else if (Objects.equals(dael[i][j].getType(), "⭕")) {
                            if (getDiscAtPosition(new Position(i, j)).getOwner() != dael[i][j].getOwner()) {
                                System.out.println("\tUndo: flipping back ⭕ in (" + i + " ," + j + ")");
                            }
                            discsOnBoard[i][j] = new UnflippableDisc(dael[i][j].getOwner());
                        }
                    } else {
                        discsOnBoard[i][j] = null;
                    }

                }
            }

            if (lastMove != null) {
                // Update the turn and adjust special disc counts
                isFirstPlayerTurn = !isFirstPlayerTurn;
                Disc disc = lastMove.disc();
                Player current = lastMove.disc.getOwner();
                Player cuerretPlayer = isFirstPlayerTurn ? player1 : player2;
                if (disc instanceof UnflippableDisc) {
                    cuerretPlayer.number_of_unflippedable++;
                }
                if (disc instanceof BombDisc) {
                    cuerretPlayer.number_of_bombs++;
                }
            }

            System.out.println();
        } else {
            System.out.println("\tNo previous move available to undo");
            System.out.println();
        }
    }
}