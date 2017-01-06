package tictactoe.player;

import tictactoe.game.Coordinate;
import tictactoe.game.TicTacGrow;

/**
 * This interface dictates the moves and options that a player must be able to
 * do in tic tac grow.
 * 
 * It's generally a good idea that Wade give feedback on this class 
 * before it gets used.
 * 
 * @author William McDermott
 * @version 2016.12.28
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
     * Move in an arbitrary location, which will only be valid if it passes
     * the test of a legal move in TicTacGrow.
     * Improper moves will end the simulation with an OccupiedSpotException,
     * or IllegalMoveException (when out of the scope of the current move).
     * 
     * @param board A clone of the board being played on, for viewing purposes.
     * 
     * The last move that was made will be in the TicTacGrow board,
     * accessible through the getLastMove() method.
     * This coordinate must be short, since it is a prefix of larger
     * coordinates. The player should add a suffix for their desired move,
     * which will be or an array of coordinates for a free move, although 
     * usually just one coordinate. The output MUST be the length of the order 
     * of the board.
     * 
     * @return A coordinate of where to move in the grid at large.
     */
   Coordinate move(TicTacGrow board);
}
