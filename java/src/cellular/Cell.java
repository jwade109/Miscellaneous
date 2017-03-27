package cellular;

public class Cell
{
	private int state;
	private int nextState;
	/**
	 *  Creates a new zero-state Cell.
	 */
	public Cell()
	{
		this(0);
	}
	/**
	 *  Creates a new Cell with a given state.
	 */
	public Cell(int state)
	{
		this.state = state;
	}
	/**
	 *  Returns the state of the Cell.
	 */
	public int getState()
	{
		return state;
	}
	/**
	 *  Sets the state of this Cell.
	 */
	public void setState(int state)
	{
		this.state = state;	
	}
	/**
	 *  Sets the nextState of this Cell.
	 */
	public void setNextState(int newState)
	{
		nextState = newState;
	}
	/**
	 *  Changes the state to the value of nextState.
	 */
	public void setToNext()
	{
		state = nextState;	
	}
	/**
	 *  Compares the states of two Cells to tell if they are the same.
	 */
	public boolean equals(Cell bud)
	{
		return state == bud.getState();
	}
	/**
	 *  Checks if this Cell is a certain state.
	 */
	public boolean isState(int state)
	{
		return this.state == state;
	}
	/**
	 *  Returns the state of this cell in a String.
	 */
	public String toString()
	{
		return String.valueOf(state);
	}
}