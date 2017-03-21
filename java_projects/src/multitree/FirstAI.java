package tree;

public class FirstAI implements Player
{
    /**
     * Gets the legal move in the game with the lowest index.
     */
    public int[] makeMove(TicTacGrow g)
    {
        return g.getLegalMoves().get(0);
    }

    /**
     * Gets a description of this AI.
     * 
     * @return A String.
     */
    public String description()
    {
        return "An AI which picks the legal move with the lowest index.";
    }

    public String name()
    {
        return "FirstAI";
    }

}
