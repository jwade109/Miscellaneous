package tree;

public class MiddleAI implements Player
{
    /**
     * Gets the legal move in the game with the lowest index.
     */
    public int[] makeMove(TicTacGrow g)
    {
        return g.getLegalMoves().get(g.getLegalMoves().size()/2);
    }

    /**
     * Gets a description of this AI.
     * 
     * @return A String.
     */
    public String description()
    {
        return "An AI which picks the legal move with the middlemost index.";
    }
    
    public String name()
    {
        return "MiddleAI";
    }

}
