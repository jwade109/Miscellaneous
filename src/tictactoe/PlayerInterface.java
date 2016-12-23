package tictactoe;

/**
 * This interface dictates the moves and options that a player must be able to
 * do in tic tac toe.
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
}
