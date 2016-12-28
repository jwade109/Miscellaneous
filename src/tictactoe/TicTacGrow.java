package tictactoe;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Represents the sets of game orders and rules that accompany the board.
 * 
 * This class will play the role of the referee in the game. It receives moves,
 * testing whether they are legal or not, and allowing the game to be run in an
 * orderly manner.
 * 
 * @author William McDermott
 * @version 2016.12.27
 */
public class TicTacGrow
{
    /**
     * The board where the game is played.
     */
    private TreeGrid board;

    /**
     * The sub-grid location of the next move.
     */
    private int[] nextMove;

    /**
     * Number of moves made in the game.
     */
    private int moves;

    /**
     * An independent variable that don't need no man.
     */
    private static final int[][] WIN_CONDITIONS =
    {
        { 0, 1, 2 },
        { 3, 4, 5 },
        { 6, 7, 8 },
        { 0, 3, 6 },
        { 1, 4, 7 },
        { 2, 5, 8 },
        { 0, 4, 8 },
        { 2, 4, 6 } 
    };


    /**
     * Creates a new game instance of an arbitrary order.
     */
    public TicTacGrow(int order)
    {
        board = new TreeGrid(order);
        nextMove = new int[0];
        moves = 0;
    }


    /**
     * Gets the order of the internal board.
     * 
     * @return The order of the gameBoard
     */
    public int getOrder()
    {
        return board.getOrder();
    }


    /**
     * Gets the number of moves that have been made.
     * 
     * @return The number of moves that have been made.
     */
    public int getMoves()
    {
        return moves;
    }


    /**
     * Gets the nextMove pointer made on this board.
     * 
     * @return The nextMove that was made, which is
     *         truncated from the last move.
     */
    public int[] getNextMove()
    {
        return nextMove;
    }


    /**
     * Makes a move for a certain player in a certain location.
     * 
     * @param location
     *            The place to make the move.
     * @param shape
     *            Which player's symbol is being played.
     */
    public void move(int[] location, PlayEnum shape)
    {
        /** Do we validate the move here, or allow GameManager to do so? */
        if (!this.isValidMove(location))
        {
            // fake handling
            throw new IllegalStateException(
                "Move validation in TicTacGrow.move was bad yo");
        }

        board.setState(location, shape);
        this.updateWinnersAfterMove(location);

        // DEBUG
        System.out.println("BEFORE");
        System.out.print("Debug for nextMove calculation:"
            + " nextMove == [");
        for (int i = 0; i < nextMove.length; i++)
        {
            System.out.print(nextMove[i] + ", ");
            if (i == nextMove.length - 1)
            {
                System.out.print(nextMove[nextMove.length - 1]);
            }
        }
        // updating nextMove, which could be its own method
        // left shift the tree coordinates
        int[] truncatedLocation = new int[location.length - 1];
        for (int i = 0; i < location.length - 1; i++)
        {
            truncatedLocation[i] = location[i + 1];
        }
        // while we move out of the solved layers, truncate the last coordinate
        while (board.getState(truncatedLocation) != PlayEnum.U)
        {
            truncatedLocation = this.truncateLastIndex(truncatedLocation);
        }
        nextMove = truncatedLocation;
        // DEBUG
        System.out.println("AFTER");
        System.out.print("Debug for nextMove calculation:"
            + " nextMove == [");
        for (int i = 0; i < nextMove.length; i++)
        {
            System.out.print(nextMove[i] + ", ");
            if (i == nextMove.length - 1)
            {
                System.out.print(nextMove[nextMove.length - 1]);
            }
        }
        moves++;
    }


    /**
     * Tests if the board is won by a player.
     * 
     * @return The current win state of the entire board. Note this can return
     *         PlayEnum.U if the board is not yet won.
     */
    public PlayEnum getWinner()
    {
        return board.getState(new int[0]);
    }


    /**
     * Updates the winners after a move.
     * 
     * @param place
     *            The location where the last move happened.
     */
    private void updateWinnersAfterMove(int[] place)
    {
        // If the subgrid is won, truncate to go up a layer
        /*
         * if (board.getState(place) != PlayEnum.U)
         * {
         *     place = this.truncateLastIndex(place);
         * }
         */
        System.out.print("Debug for updateWinnersAfterMove: place == [");
        for (int i = 0; i < place.length; i++)
        {
            System.out.print(place[i] + ", ");
            if (i == place.length - 1)
            {
                System.out.print(place[place.length - 1]);
            }
        }
        System.out.println("]");
        PlayEnum winner = this.whoWonGrid(place);
        System.out.println(winner);
        // Now we recur, in case this win triggered a bigger one
        if (winner != PlayEnum.U)
        {
            board.setState(place, winner);
            if (place.length == 0)
            {
                return;
            }
            // int last = place[place.length - 1];
            this.updateWinnersAfterMove(this.truncateLastIndex(place));
            // this.addIndexSuffix(place, last);
        }
        // If the winner is U, then we can't determine a larger winner,
        // so we just end.
    }


    /**
     * Ripped off version of Wade's base case win tester, made into a
     * base case win state tester.
     * 
     * Instead of worrying about parameters and matching the locations to
     * each other, this method just straight up tests for all three patterns.
     * 
     * @return Which player has won the board, or an undetermined value.
     */
    private PlayEnum whoWonGrid(int[] place)
    {
        PlayEnum[] childrenStates = board.getSubGrid(place);
        if (childrenStates == null)
        {
            return board.getState(place);
        }
        for (int i = 0; i < WIN_CONDITIONS.length; i++)
        {
            int[] pattern = WIN_CONDITIONS[i];
            for (PlayEnum p : new PlayEnum[] { PlayEnum.X, PlayEnum.O })
            {
                boolean win = true;
                for (int j = 0; j < pattern.length; j++)
                {
                    if (childrenStates[pattern[j]] != p)
                    {
                        win = false;
                    }
                }
                if (win)
                {
                    return p;
                }
            }
        }
        return PlayEnum.U;
    }


    /**
     * Tests if the game is over, such that no player can move.
     * 
     * @return True if the game is over, for a win or for a tie.
     */
    public boolean isGameOver()
    {
        return this.isGameOverRecursive(new int[0]);
    }


    /**
     * Tests if a subGrid of the game is over or not.
     * Not currently used except in this class, but could be made public.
     * 
     * @param location
     *            The location to test for something being game over.
     * @return True if the no moves can be made, false otherwise.
     */
    private boolean isGameOverRecursive(int[] location)
    {
        // debug
        if (location.length > 0)
        {
            System.out.print("Checking game over at location: [");
        }
        for (int i = 0; i < location.length; i++)
        {
            System.out.print(location[i] + ", ");
            if (i == location.length - 1)
            {
                System.out.println(location[i] + "]");
            }
        }
        // These base cases could be better
        if (board.getState(location) != PlayEnum.U)
        {
            return true;
        }
        // Undetermined parent case
        if (location.length == this.getOrder())
        {
            return board.getState(location) != PlayEnum.U;
        }

        // For each undetermined board, determine if it is full.
        PlayEnum[] shapes = board.getSubGrid(location);
        if (shapes == null)
        {
            return false;
        }
        for (int i = 0; i < shapes.length; i++)
        {
            // We ignore won boards, they are boring.
            if (shapes[i] == null)
            {
                return false;
            }
            if (shapes[i] == PlayEnum.U)
            {
                int[] newLocation = this.addIndexSuffix(location, i);
                if (!this.isGameOverRecursive(newLocation))
                    ;
                {
                    newLocation = this.truncateLastIndex(newLocation);
                    return false;
                }
                // newLocation = this.truncateLastIndex(newLocation);
            }
        }
        return true;
    }


    /**
     * Returns an array of all valid moves, where a move is encoded as an
     * ordered coordinate pair.
     * 
     * @return An ArrayList of allowed moves, in iterator order.
     */
    public ArrayList<int[]> getValidMoves()
    {
        ArrayList<int[]> moves = new ArrayList<int[]>();
        Iterator<PlayEnum> iter = board.iterator();
        int index = 0;
        while (iter.hasNext())
        {
            if (iter.next() == PlayEnum.U)
            {
                int[] location = Converter.expandToTreeCoordinates(index, board
                    .getOrder());
                if (this.isValidMove(location))
                {
                    moves.add(location);
                }
            }
            index++;
        }
        return moves;
    }


    /**
     * Checks that a move is valid according to the game
     * A move is valid if the cell denoted by the location:
     * 1. is unoccupied (contains a U)
     * 2. is within the available sub-game
     * 
     * @return True if the move follows the game rules.
     */
    public boolean isValidMove(int[] thisMove)
    {
        return this.isSpaceUnoccupied(thisMove) && this.isMoveLegal(thisMove);
    }


    /**
     * Tests if a move is legal based on the previous move.
     * 
     * @param thisMove
     *            The move being made and tested.
     * @return True if the pastMove agrees with this move.
     */
    private boolean isMoveLegal(int[] thisMove)
    {
        for (int i = 0; i < nextMove.length; i++)
        {
            if (thisMove[i] != nextMove[i])
            {
                return false;
            }
        }
        return true;
    }


    /**
     * Tests if a space is unoccupied. Really too simple method.
     * 
     * @param location
     *            The place to be tested for presence.
     * @return True if there is an unoccupied space, false otherwise.
     */
    private boolean isSpaceUnoccupied(int[] location)
    {
        return board.getState(location) == PlayEnum.U;
    }


    /**
     * Adds a suffix integer to an array for recursive methods.
     * 
     * @param array
     *            The array to add a suffix to.
     * @param value
     *            The value to add to the array
     * @return The parameter array with the value attached.
     */
    public int[] addIndexSuffix(int[] array, int value)
    {
        int[] target = new int[array.length + 1];
        for (int i = 0; i < array.length; i++)
        {
            target[i] = array[i];
        }
        target[array.length] = value;
        return target;
    }


    /**
     * Truncate the last coordinate of the array.
     * 
     * @param array
     *            The array to truncate.
     * @return The same array with one coordinate removed.
     */
    public int[] truncateLastIndex(int[] array)
    {
        // System.out.println("Truncation performed!");
        if (array.length == 0)
        {
            return array;
        }
        int[] target = new int[array.length - 1];
        for (int i = 0; i < array.length - 1; i++)
        {
            target[i] = array[i];
        }
        return target;
    }
    
    /**
     * Prints out this object.
     */
    @Override
    public String toString()
    {
        return board.toString();
    }
}
