package multitree;

import java.util.ArrayList;

public class RandomAI implements Player
{
    /**
     * Gets a random legal move given a TicTacGrow game.
     * 
     * @param g A TicTacGrow game.
     * @return The path of the random move.
     */
    public int[] makeMove(TicTacGrow g)
    {
        ArrayList<int[]> moves = g.getLegalMoves();
        return moves.get((int) (Math.random() * moves.size()));
    }

    /**
     * Gets a String representation of this AI.
     * 
     * @return A String.
     */
    public String description()
    {
        return "Computer player that makes random moves";
    }
    
    public String name()
    {
        return "RandomAI";
    }
}
