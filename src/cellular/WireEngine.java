package cellular;

public class WireEngine extends Engine
{
	public WireEngine(int states)
	{
		super(states);
	}
	/**
	 *  Computes the next state of a Cell based on the WireWorld automata.
	 */
	@Override
	public void computeNextState(Cell[] neighbors)
	{
		// Note state 0 does nothing.
		int currentState = neighbors[4].getState();
		switch (currentState)
		{
		case 0:
			neighbors[4].setNextState(0);
			break;
		case 1:
			neighbors[4].setNextState(3);
			break;
		case 2:
			neighbors[4].setNextState(1);
			break;
		case 3:
			int count = 0;
			for (int i = 0; i < neighbors.length; i++)
			{
				if (i == 4) continue;
				if (neighbors[i].getState() == 2) count++;
				if (count == 3) break;
			}
			if (count == 1
				|| count == 2)
			{
				neighbors[4].setNextState(2);
			}
			else
			{
				neighbors[4].setNextState(3);
			}
		}
	}
}