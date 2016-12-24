package tictactoe;

/**
 * Represents the sets of game orders and rules that accompany the board.
 * 
 * This class will play the role of the referee in the game. It tells players
 * to move and receives their moves, testing whether they are legal or not, and
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
    }
    
}
