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
    public Coordinate move(TicTacGrow gameBoard)
    {
        Coordinate move = super.move(gameBoard);
        // DEBUG
        /*
        ArrayList<Coordinate> list = gameBoard.getValidMoves();
        for (Coordinate c : list)
        {
            System.out.println(Arrays.toString(c.getTreePath()));
        }
        /* */
        // System.out.println(gameBoard.toString());
        while (!gameBoard.isValidMove(move))
        {
            move = super.move(gameBoard);
        }
        return move;
    }
}
