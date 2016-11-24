package cellular;

/**
 * Represents an automata's basic profile that specifies the number
 * of states and other properties of an automata. It is basically
 * a glorified container of these important qualities.
 * 
 * @author William McDermott
 * @version 2016.11.24
 */
public class Profile
{
    /**
     * The maximum state of the automata.
     */
    private final int maxState;

    /**
     * The cell dimensions of each cell, in pixels.
     */
    private final int cellSize;
    
    /**
     * The Engine for this class.
     */
    private final Engine engine;


    /**
     * Creates a new Profile object.
     * 
     * @param maxState
     *            The maxState of this object.
     * @param cellSize
     *            The cellSize of this object.
     */
    public Profile(int maxState, int cellSize, Engine engine)
    {
        this.maxState = maxState;
        this.cellSize = cellSize;
        this.engine = engine;
    }


    /**
     * Gets the maximum state this automata can reach.
     * 
     * @return The maximum state of the cell automata.
     */
    public int getMaxState()
    {
        return maxState;
    }


    /**
     * Gets the cell size of this automata, for graphical purposes, in pixels.
     * 
     * @return The cell size of this automata.
     */
    public int getCellSize()
    {
        return cellSize;
    }
    
    /**
     * Gets the Engine for this automata Profile.
     * 
     * @return  An engine that runs this automata.
     */
    public Engine getEngine()
    {
        return engine;
    }
}
