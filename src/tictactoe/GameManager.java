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
        /*
         * I'm gonna write some code here which may not work exactly just yet,
         * but I think it's a valid way to approach this conceptually
         * 
         * Player objects would need a single method called takeTurn(), which would take
         * a TicTacGrow object as an argument and return the same object modified by one move.
         * This is necessary because Players do not (should not) store references of the game
         * they are playing. This allows the Manager to manage when Players take their turn,
         * and allows TicTacGrow to ensure Players make valid moves.
         */

        boolean xTurn = false;
        
        if (xTurn) 
        {
            board = playerX.takeTurn(board);
        }
        else
        {
            board = playerO.takeTurn(board);
        }
        
        xTurn = !xTurn;
    }
}
