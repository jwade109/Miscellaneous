package tictactoe;

import java.util.Observer;

/**
 * This interface dictates the moves and options that a player must be able to
 * do in tic tac grow.
 * 
 * It's generally a good idea that Wade give feedback on this class 
 * before it gets used.
 * 
 * @author William McDermott
 * @version 2016.12.24
 */
public interface Player
{
    /**
     * Gets whether the object is a human or an AI.
     * @return True if the object is an AI, false if it is a human.
     */
    boolean isAI();
    
    /**
     * Gets the name of the player for output purposes.
     * @return  The name of the player.
     */
    String getName();
    
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
    int[] move(int location);
    
    /**
     * Move in an arbitrary location, which will only be valid if it passes
     * the test of a legal move in TicTacGrow.
     * Improper moves will end the simulation with an OccupiedSpotException.
     * 
     * @param location A coordinate of where to move in the bigger grid.
     * The simulation will prefix the larger grid before this one,
     * which represents the last few coordinates.
     */
    int[] move(Board board, int[] lastMove);
    
    /**
     * Gets the state for a cell.
     * @param location The coordinate of a cell or subgrid.
     * @return The state of the specified cell.
     */
    int[][] getCellState(int[] location);
}
