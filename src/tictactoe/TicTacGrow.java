package tictactoe;

import java.util.ArrayList;

/**
 * Represents the sets of game orders and rules that accompany the board.
 * 
 * This class will play the role of the referee in the game. It tells players to
 * move and receives their moves, testing whether they are legal or not, and
 * allowing the game to be run in an orderly manner.
 * 
 * @author William McDermott
 * @version 2016.12.23
 */
public class TicTacGrow
{
    /**
     * The board where the game is played.
     */
    private Board gameBoard;

    /**
     * Number of moves made in the game.
     */
    private int moves;

    /**
     * Creates a new game instance of an arbitrary order.
     */
    public TicTacGrow(int order)
    {
        gameBoard = new Board(order);
        moves = 0;
    }

    /**
     * Makes the next move in the game,
     */
    public void nextMove()
    {
        boolean moveX = moves % 2 == 0;
    }

    /**
     * Returns an array of all valid moves, where a move is encoded as an ordered coordinate pair.
     */
    public ArrayList<int[]> getValidMoves()
    {
        
    }
    
    public boolean isValidMove(int[] coord)
    {
        /*
         * A move is valid if the cell denoted by the coordinate pair:
         * 1. is unoccupied (contains a U)
         * 2. is within the available subgame
         */
        return false;
    }
}
