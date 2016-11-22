package langton;

import chunk.ChunkArray;

public class Board
{
    private static ChunkArray<Boolean> state;
    private static ChunkArray<Integer> count;

    /**
     * Creates a new Board object.
     */
    public Board()
    {
        state = new ChunkArray<Boolean>();
        count = new ChunkArray<Integer>();
    }

    public boolean getCellState(int x, int y)
    {
        if (state.getEntry(x, y) == null)
        {
            return false;
        }
        return state.getEntry(x, y);
    }

    public int getCellCount(int x, int y)
    {
        if (count.getEntry(x, y) == null)
        {
            return 0;
        }
        return count.getEntry(x, y);
    }

    public void setCellState(boolean val, int x, int y)
    {
        state.setEntry(val, x, y);
    }

    public int invertCell(int x, int y, int z)
    {
        state.setEntry(!getCellState(x, y), x, y);

        count.setEntry(getCellCount(x, y) + z, x, y);

        return count.getEntry(x, y);
    }

    public int[] domain()
    {
        int[] stateDomain = state.getDomain();
        int[] countDomain = count.getDomain();
        int low = Math.min(stateDomain[0], countDomain[0]);
        int high = Math.max(stateDomain[1], countDomain[1]);
        return new int[] { low, high };
    }
    
    public int[] range()
    {
        int[] stateRange = state.getRange();
        int[] countRange = count.getRange();
        int low = Math.min(stateRange[0], countRange[0]);
        int high = Math.max(stateRange[1], countRange[1]);
        return new int[] { low, high };
    }
}
