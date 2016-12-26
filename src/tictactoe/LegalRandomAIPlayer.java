package tictactoe;

/**
 * 
 * 
 * @author William McDermott (willm97)
 * @version 2016.12.26
 */
public class LegalRandomAIPlayer extends TestAIPlayer
{
    /**
     * Creates a new object of this type.
     */
    public LegalRandomAIPlayer()
    {
        super();
    }
    
    /**
     * Creates a new object of this type.
     * @param name  The name this object will have.
     */
    public LegalRandomAIPlayer(String name)
    {
        super(name);
    }
    
    /**
     * Makes a random allowable move.
     */
    @Override
    public int[] move(TicTacGrow gameBoard)
    {
        int[] move = super.move(gameBoard);
        while (!gameBoard.isValidMove(move))
        {
            move = super.move(gameBoard);
        }
        return move;
    }
}
