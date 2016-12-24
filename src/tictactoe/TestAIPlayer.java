package tictactoe;

/**
 * Basic AI that moves randomly with wild abandon.
 * 
 * @author William McDermott
 * @version 2016.12.24
 */
public class TestAIPlayer implements Player
{
    /**
     * The name of this TestAIPlayer.
     */
    public String name;
    
    /**
     * Makes a new TestAIPlayer with the default name.
     */
    public TestAIPlayer()
    {
        this("FiteMe");
    }
    
    /**
     * Makes a new TestAIPlayer.
     * @param name  The name of this object.
     */
    public TestAIPlayer(String name)
    {
        this.name = name;
    }
    
    /**
     * Tests whether this is an AI or not.
     * @return  True, because this is an AI.
     */
    @Override
    public boolean isAI()
    {
        return true;
    }

    /**
     * Gets the name for this AI.
     * @return The name initialized in the constructor.
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Move in an arbitrary location, which will only be valid if it passes
     * the test of a legal move in TicTacGrow.
     * Improper moves will end the simulation with an OccupiedSpotException,
     * or IllegalMoveException (when out of the scope of the current move).
     * 
     * @param board The board to play on.
     * 
     * @param lastMove The last move that was made.
     * This coordinate must be short, since it is a prefix of larger
     * coordinates. The player should add a suffix of the desired move,
     * which will be either one number for the basic move, or an array of
     * coordinates for a free move. It MUST be the length of the order of
     * the board.
     * 
     * @return A coordinate of where to move in the grid at large.
     */
    @Override
    public int[] move(Board board, int[] lastMove)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
