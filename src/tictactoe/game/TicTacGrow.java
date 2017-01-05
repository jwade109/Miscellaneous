package tictactoe.game;

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
 * @version 2017.01.05
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
        board.setState(location, shape);
        this.updateWinnersAfterMove(this.truncateLastIndex(location));
        this.nextMove = this.findNextMove(location);
        moves++;
    }
    
    /**
     * Calculates the nextMove based on a previous one.
     * 
     * @param thisMove  The move that was just made.
     * @param nextMove  A truncated location of a move that must soon be made.
     */
    public int[] findNextMove(int[] thisMove)
    {
        int[] target = new int[0];
        while (!this.isGameOverLocation(target) && target.length < thisMove.length - 1)
        {
            target = this.addIndexSuffix(target, thisMove[target.length + 1]);
        }
        // Because if we didn't hit the end, then we went one step too far.
        if (!this.isNotWon(target))
        {
            target = this.truncateLastIndex(target);
        }
        return target;
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
        PlayEnum winner = this.whoWonGrid(place);
        if (winner == null)
        {
            return;
        }
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
        // Unless you introduce a T variable. Which is smart, thanks Wade.
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
        return this.isGameOverLocation(new int[0]);
    }


    /**
     * Tests if a subGrid of the game is over or not.
     * Not currently used except in this class, but could be made public.
     * 
     * @param location
     *            The location to test for something being game over.
     * @return True if the no moves can be made, false otherwise.
     */
    private boolean isGameOverLocation(int[] location)
    {
        PlayEnum state = board.getState(location);
        return !(state == null || state == PlayEnum.U);
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
        // System.out.println("isValidMove debug = " + Arrays.toString(thisMove));
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
        if (location.length == 0)
        {
            return !this.isGameOverLocation(location);
        }
        return !this.isGameOverLocation(location)
            && this.isNotWon(this.truncateLastIndex(location));
    }
    
    /**
     * Clones this object into another.
     */
    @Override
    public TicTacGrow clone()
    {
        TicTacGrow clone = new TicTacGrow(this.board.getOrder());
        // Copying the board
        clone.board = this.copyBoard();
        clone.nextMove = this.nextMove;
        System.out.println("clone debug = " + Arrays.toString(nextMove));
        clone.moves = this.moves;
        return clone;
    }
    
    
    /**
     * Clones this object's TreeGrid board into another one.
     * 
     * @return  A copy of this TicTacGrow's board variable.
     */
    public TreeGrid copyBoard()
    {
        TreeGrid clone = new TreeGrid(this.board.getOrder());
        this.copySubgrid(new int[]{}, clone);
        return clone;
    }
    
    /**
     * Recursively copies the values in this tree into a clone.
     * 
     * @param place     The location to copy, which will copy all of its
     * children down as well.
     */
    public void copySubgrid(int[] place, TreeGrid board)
    {
        // base cases 
        PlayEnum state = this.board.getState(place);
        if (state == null)
        {
            return;
        }
        board.setState(place, state);
        if (place.length == this.board.getOrder())
        {
            return;
        }
        // recursing down the tree
        for (int i = 0; i < 9; i++)
        {
            this.copySubgrid(this.addIndexSuffix(place, i), board);
        }
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
}
