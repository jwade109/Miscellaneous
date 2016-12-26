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
 * @version 2016.12.26
 */
public class TicTacGrow
{
    /**
     * The board where the game is played.
     */
    private Board board;

    /**
     * The sub-grid location of the next move.
     */
    private int[] nextMove;

    /**
     * Number of moves made in the game.
     */
    private int moves;

    /**
     * Creates a new game instance of an arbitrary order.
     */
    public TicTacGrow(int order)
    {
        board = new Board(order);
        nextMove = new int[0];
        moves = 0;
    }
    
    /**
     * Gets the order of the internal board.
     * @return  The order of the gameBoard
     */
    public int getOrder()
    {
        return board.getOrder();
    }
    
    /**
     * Gets the number of moves that have been made.
     * @return  The number of moves that have been made.
     */
    public int getMoves()
    {
        return moves;
    }
    
    /**
     * Gets the nextMove pointer made on this board.
     * @return The nextMove that was made, which is 
     * truncated from the last move.
     */
    public int[] getNextMove()
    {
        return nextMove;
    }
    
    /**
     * Makes a move for a certain player in a certain location.
     * @param location  The place to make the move.
     * @param shape     Which player's symbol is being played.
     */
    public void move(int[] location, PlayEnum shape)
    {
        /** Do we validate the move here, or allow GameManager to do so? */
        if (!this.isValidMove(location))
        {
            // fake handling
            throw new IllegalStateException("Just a test");
        }
        board.setState(location, shape);
        
        // update wins
        
        // updating nextMove
        int[] truncatedLocation = new int[location.length - 1];
        for (int i = 0; i < location.length - 1; i++)
        {
            truncatedLocation[i] = location[i + 1]; 
        }
        while (board.getState(truncatedLocation) != PlayEnum.U)
        {
            int[] subArray = new int[truncatedLocation.length - 1];
            for (int i = 0; i < truncatedLocation.length - 1; i++)
            {
                subArray[i] = truncatedLocation[i];
            }
            truncatedLocation = subArray;
        }
        nextMove = truncatedLocation;
        moves++;
    }

    /**
     * Returns an array of all valid moves, where a move is encoded as an 
     * ordered coordinate pair.
     * @return  An ArrayList of allowed moves, in iterator order.
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
                int[] location = 
                    Converter.expandToTreeCoordinates(index, board.getOrder());
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
     * @return  True if the move follows the game rules.
     */
    public boolean isValidMove(int[] thisMove)
    {
        return this.isSpaceUnoccupied(thisMove) && this.isMoveLegal(thisMove);
    }
    
    /**
     * TEMPORARY
     * Tests if the game is over
     * @return  True if the game is over, for a win or for a tie.
     */
    public boolean isGameOver()
    {
        return board.isGameOver();
    }
    
    /**
     * Tests if a move is legal based on the previous move.
     * 
     * @param thisMove  The move being made and tested.
     * @return          True if the pastMove agrees with this move.
     */
    private boolean isMoveLegal(int[] thisMove)
    {
        for (int i = 1; i < nextMove.length; i++)
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
     * @param location  The place to be tested for presence.
     * @return  True if there is an unoccupied space, false otherwise.
     */
    private boolean isSpaceUnoccupied(int[] location)
    {
        return board.getState(location) == PlayEnum.U;
    }
}
