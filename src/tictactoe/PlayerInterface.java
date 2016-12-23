package tictactoe;

/**
 * This interface dictates the moves and options that a player must be able to
 * do in tic tac grow.
 * 
 * @author William McDermott
 * @version 2016.12.22
 */
public interface PlayerInterface
{
    /**
     * The AI's way of determining what to do, and blank for a human.
     * I guess.
     */
    void think();
    
    /**
     * Move in one of the 9 legal locations, ordered as normal.
     * If a move is illegal, you will have to ensure it doesn't happen.
     * @param location  The index to move at, as such:
     * 0 1 2
     * 3 4 5
     * 6 7 8
     * 
     * Where some indexes may result in an OccupiedSpotException.
     */
    void move(int location);
    
    /**
     * Move in an arbitrary location, as with a free move.
     * 
     * @param location A coordinate of where to move in the bigger grid.
     * The simulation will prefix the larger grid before this one,
     * which represents the last few coordinates.
     */
    void move(int[] location);
    
    /**
     * IM NOT SURE THIS METHOD IS NECESSARY...
     * Gets the subGrid for a cell
     * @param location The coordinate of a subgrid to find its children's states.
     * @return A grid of the states of the three cells below this
     * coordinate.
     */
    int[][] requestGrid(int[] location);
}
