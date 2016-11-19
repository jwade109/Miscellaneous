package cellular;

public class AntEngine extends Engine
{
	public AntEngine(int states)
	{
		super(states);
	}
	/**
	 *  Finds the next state of the Ant with the rules of Langton's Ant.
	 *  Although it shouldn't break with multiple ants, it doesn't deal
	 *  with ants encountering eachother.
	 */
	@Override
	public void computeNextState(Cell[] neighbors)
	{
		// If the ant is coming into the cell, become the ant.
		int[] vals = new int[4];
		vals[0] = 1; // 1
		vals[1] = 3; // 3
		vals[2] = 7; // 7
		vals[3] = 5; // 5
		for (int i = 0; i < 4; i++)
		{
			if (neighbors[vals[i]] != null
				&& (neighbors[vals[i]].getState() == i + 1
				|| neighbors[vals[i]].getState() == i + 6))
			{
				if (neighbors[4].getState() / 5 == 0)
				{
					if (i == 0)
					{
						neighbors[4].setNextState(4);
					}
					else
					{
						neighbors[4].setNextState(i);
					}
				}
				else
				{
					if (i == 3)
					{
						neighbors[4].setNextState(6);
					}
					else
					{
						neighbors[4].setNextState(i + 7);
					}
				}
				// Change cell the ant was in to the other color.
				int nearState = neighbors[vals[i]].getState();
				neighbors[vals[i]].setNextState(5 * (1 - nearState / 5));
			}
		}
	}
}