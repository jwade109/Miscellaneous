package tictactoe;

/**
 * Runs an instance of the game, by calling methods on the players and board.
 * 
 * @author William McDermott
 * @version 2016.12.23
 */
public class GameManager
{
    /**
     * A TicTacGrow to play.
     */
    private TicTacGrow board;
    
    /**
     * The player who plays X.
     */
    private Player playerX;
    
    /**
     * The player who plays O.
     */
    private Player playerO;
    
    /**
     * Creates a new GameManager with two players and a board.
     */
    public GameManager(TicTacGrow board, Player playerX, Player playerO)
    {
        this.board = board;
        this.playerX = playerX;
        this.playerO = playerO;
    }
    
    /**
     * Runs the game until a winner is found!
     */
    public void run()
    {
        while (!board.gameOver())
        {
            this.makeMove();
        }
    }
    
    /**
     * Makes a move for a player with the specified enum.
     * @param   The enum for the player to make a move.
     */
    private void makeMove()
    {
        
    }
}
