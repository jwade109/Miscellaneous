package langton;

import java.util.Observable;
import chunk.ChunkArray;

public class LangtonGrid extends Observable
{
    private static ChunkArray<Boolean> state;
    private static ChunkArray<Integer> count;

    /**
     * Creates a new Board object.
     */
    public LangtonGrid()
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
        if (val != state.getEntry(x, y))
        {
            notifyObservers();
        }
        state.setEntry(val, x, y);
    }

    public int invertCell(int x, int y, int z)
    {
        state.setEntry(!getCellState(x, y), x, y);
        count.setEntry(getCellCount(x, y) + z, x, y);
        notifyObservers();
        return count.getEntry(x, y);
    }

    public int[] domain()
    {
        int[] stateDomain = state.domain();
        int[] countDomain = count.domain();
        int low = Math.min(stateDomain[0], countDomain[0]);
        int high = Math.max(stateDomain[1], countDomain[1]);
        return new int[] { low, high };
    }

    public int[] range()
    {
        int[] stateRange = state.range();
        int[] countRange = count.range();
        int low = Math.min(stateRange[0], countRange[0]);
        int high = Math.max(stateRange[1], countRange[1]);
        return new int[] { low, high };
    }
}
