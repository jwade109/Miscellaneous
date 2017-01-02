package tictactoe;

/**
 * A great basic AI that moves with slightly less wild abandon.
 * It moves randomly, but only if that random move is legal.
 * 
 * @author William McDermott (willm97)
 * @version 2017.01.01
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
    public Coordinate move(TicTacGrow gameBoard)
    {
        Coordinate move = super.move(gameBoard);
        while (!gameBoard.isValidMove(move))
        {
            move = super.move(gameBoard);
        }
        return move;
    }
}
