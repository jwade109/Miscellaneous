package cellular;

/**
 * Abstract class for the automata engines.
 * This class provides, more or less, a basic interface for engines that
 * can be implemented to run an automata.
 * 
 * @author William McDermott
 * @version Late Summer 2016
 * @version Comments 2016.11.24
 */
public abstract class Engine
{
    /**
     * The maximum state of this automata.
     */
    private int maxState;


    /**
     * Creates a new Engine object.
     * 
     * @param maxState
     *            The maximum state this engine will have.
     *            Subclasses will call this constructor with the state as a
     *            constant.
     */
    public Engine(int maxState)
    {
        this.maxState = maxState;
    }


    /**
     * Returns the maximum allowed State for this simulation.
     * 
     * @return The maximum state of this automata, if the states start at 0.
     */
    public int getMaxState()
    {
        return maxState;
    }


    /**
     * Computes the next state of the given Cell from the rule for this type.
     * 
     * @param neighbors
     *            The cells in the immediate vicinity of this object.
     */
    public abstract void computeNextState(Cell[] neighbors);
}
