package multitree;

public interface Player
{
    /**
     * Gets this player's next move, given a TicTacGrow game.
     * 
     * @param g A TicTacGrow game.
     * @return The path of the desired move.
     */
    public int[] makeMove(TicTacGrow g);

    /**
     * Gets a String representation of this Player.
     * 
     * @return A String object.
     */
    public String description();

    /**
     * Gets the name of this Player.
     * 
     * @return A string.
     */
    public String name();
}
