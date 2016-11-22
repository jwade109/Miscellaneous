package langton;

import dataideas.ChunkArray;

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

    // returns the value in a given cell
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

    // a method which sets the cells to any integer value
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

    // prints the board onto the console in a visually unappealing way.
    public String toString()
    {
        throw new UnsupportedOperationException();
    }

    public String toCountString()
    {
        throw new UnsupportedOperationException();
    }

    public int getLargestCount()
    {
        throw new UnsupportedOperationException();
    }

}
