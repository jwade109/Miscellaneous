package cellular;

public abstract class Engine
{
	private int maxState;
	public Engine(int maxState)
	{
		this.maxState = maxState;
	}
	/**
	 *  Returns the maximum allowed State for this simulation.
	 */
	public int getMaxState()
	{
		return maxState;
	}
	/**
	 *  Computes the next state of the given Cell from the rule for this type.
	 */
    public abstract void computeNextState(Cell[] neighbors);
}