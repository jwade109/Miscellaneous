package tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Represents the sets of game orders and rules that accompany the board.
 * 
 * This class will play the role of the referee in the game. It receives moves,
 * testing whether they are legal or not, and allowing the game to be run in an
 * orderly manner.
 * 
 * @author William McDermott
 * @version 2016.12.31
 */
public class TicTacGrow implements Cloneable
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
     *            The tree coordinate to make the move.
     * @param shape
     *            Which player's symbol is being played.
     */
    public void move(int[] location, PlayEnum shape)
    {
        /*
        System.out.print("Debug for isValidMove:"
            + " location == [");
        for (int i = 0; i < location.length; i++)
        {
            if (i == location.length - 1)
            {
                System.out.print(location[location.length - 1]);
            }
            else
            {
                System.out.print(location[i] + ", ");
            }
        }
        */
        /*
        if (!this.isValidMove(new Coordinate(location)))
        {
            // fake handling
            throw new IllegalStateException(
                "Move validation in TicTacGrow.move was bad yo");
        }
        */
        
        board.setState(location, shape);
        this.updateWinnersAfterMove(this.truncateLastIndex(location));
        
        // updating nextMove, which could be its own method
        // left shift the tree coordinates
        int[] truncatedLocation = new int[location.length - 1];
        for (int i = 0; i < location.length - 1; i++)
        {
            truncatedLocation[i] = location[i + 1];
        }
        // while we move out of the solved layers, truncate the last coordinate
        while (board.getState(truncatedLocation) != PlayEnum.U 
            && truncatedLocation.length != 0)
        {
            truncatedLocation = this.truncateLastIndex(truncatedLocation);
        }
        nextMove = truncatedLocation;
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
        // System.out.println("Testing win condtions..");
        // System.out.println(Arrays.toString(place));
        PlayEnum winner = this.whoWonGrid(place);
        if (winner == null)
        {
            return;
        }
        // System.out.println(winner);
        // Now we recur, in case this win triggered a bigger one
        if (winner != PlayEnum.U)
        {
            board.setState(place, winner);
            if (place.length == 0)
            {
                return;
            }
            board.clearSubgrid(place); // doesn't work?
            this.updateWinnersAfterMove(this.truncateLastIndex(place));
            return;
        }
        
        // If the winner is U, then we can't determine a larger winner,
        // so we just end.
        // ^^ FALSE! AND THIS SHALL REMAIN TO ADDRESS MY IGNORANCE!
        // The board can be tied, in which case winner != PlayEnum.U!
        /*
        if (this.isGameOverRecursive(place)) // I.E a tie
        {
            System.out.println("TEST");
            if (place.length == 0)
            {
                return;
            }
            this.updateWinnersAfterMove(this.truncateLastIndex(place));
            return;
        }
        */
    }

    /**
     * Ripped off version of Wade's base case win tester, made into a
     * base case win state tester.
     * 
     * Instead of worrying about parameters and matching the locations to
     * each other, this method just straight up tests for all three patterns.
     * 
     * @param   The location to check if it is won or not.
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
        // Checking for a tie
        boolean isTied = true;
        for (int i = 0; i < childrenStates.length; i++)
        {
            if (childrenStates[i] == PlayEnum.U 
                || childrenStates[i] == null)
            {
                isTied = false;
            }
        }
        if (isTied == true)
        {
            return PlayEnum.T;
        }
        // We have checked for both winners and the tie state, so
        // we now declare it undetermined.
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
        // These base cases could be better
        PlayEnum state = board.getState(location);
        if (state == null)
        {
            return false;
        }
        if (state != PlayEnum.U)
        {
            return true;
        }
        // Undetermined case
        if (location.length == this.getOrder())
        {
            return board.getState(location) != PlayEnum.U;
        }

        // Undetermined inner node
        // For each undetermined board, determine if it is over.
        PlayEnum[] shapes = board.getSubGrid(location);
        if (shapes == null && location.length != this.getOrder())
        {
            return false;
        }
        // ^^ now we know that the node at this location has children,
        // and isn't a currently nulled inner node
        for (int i = 0; i < shapes.length; i++)
        {
            // We ignore won boards, they are boring.
            // Test that the subgrids have been played in
            if (shapes[i] == null || shapes[i] == PlayEnum.U)
            {
                return false;
            }
            /*
            if (shapes[i] == PlayEnum.U)
            {
                int[] newLocation = this.addIndexSuffix(location, i);
                if (!this.isGameOverRecursive(newLocation))
                {
                    return false;
                }
                newLocation = this.truncateLastIndex(newLocation);
            }
            */
        }
        return true;
    }


    /**
     * Returns an array of all valid moves, where a move is encoded as an
     * ordered coordinate pair.
     * 
     * @return An ArrayList of allowed moves, in iterator order.
     */
    public ArrayList<Coordinate> getValidMoves()
    {
        ArrayList<Coordinate> moves = new ArrayList<Coordinate>();
        Iterator<PlayEnum> iter = board.iterator();
        int index = 0;
        while (iter.hasNext())
        {
            if (iter.next() == PlayEnum.U)
            {
                Coordinate location = new Coordinate(index, this.getOrder());
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
     * Checks that a move is valid according to the game.
     * A move is valid if the cell denoted by the location:
     * 1. is unoccupied (contains a U)
     * 2. is within the available sub-game
     * 3. is not in a won grid, after a free move.
     * 
     * @return True if the move follows the game rules.
     */
    public boolean isValidMove(Coordinate location)
    {
        int[] thisMove = location.getTreePath();
        boolean bool1 = this.isMoveLegal(thisMove);
        boolean bool2 = this.isNotWon(thisMove);
        return bool1 && bool2;
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
     * Recursively tests if a move is contained in a won grid or not.
     * 
     * @param location  The Tree path of a move, which must not be 
     * won at all levels.
     * @return Whether the move is in a won grid or not.
     */
    private boolean isNotWon(int[] location)
    {
        // System.out.println(Arrays.toString(location));
        PlayEnum state = board.getState(location);
        if (location.length == 0)
        {
            return state == PlayEnum.U || state == null;
        }
        /*
        if (!(board.getState(location) == PlayEnum.U || state == null))
        {
            return false;
        }
        */
        return (state == PlayEnum.U || state == null)
            && this.isNotWon(this.truncateLastIndex(location));
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
    
    /**
     * Clones this object into another.
     */
    @Override
    public TicTacGrow clone()
    {
        TicTacGrow clone = new TicTacGrow(board.getOrder());
        clone.board = this.board.clone();
        clone.nextMove = Arrays.copyOf(this.nextMove, this.nextMove.length);
        clone.moves = this.moves;
        return clone;
    }
    
}
