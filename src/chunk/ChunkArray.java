package chunk;

import java.util.ArrayList;

/**
 * A data structure that contains a LinkedList of Chunks. Chunks are
 * encapsulated 2D generic arrays which store an x and y value indicating their
 * location in a plane, and are implemented as an inner class. This structure
 * uses Chunks to create a functionally infinite 2D array of generics.
 * 
 * @author Wade Foster
 * @version 2016.11.21
 * 
 * @author William McDermott
 * @version 2016.11.23
 * 
 * @param T Generic data type.
 */
public class ChunkArray<T>
{
    private final int chunkSize = 24;

    private ArrayList<Chunk> chunks;

    /**
     * Constructs a new ChunkArray.
     */
    public ChunkArray()
    {
        chunks = new ArrayList<Chunk>();
        chunks.add(new Chunk(0, 0));
    }

    /**
     * Gets the element at the specified coordinates.
     * 
     * @param x The x coordinate of the entry.
     * @param y The y coordinate of the entry.
     * @return A T object.
     */
    public T getEntry(int x, int y)
    {
        int[] clx = toCL(x);
        int[] cly = toCL(y);
        Chunk chunk = getChunk(clx[0], cly[0]);
        T data = chunk.getEntry(clx[1], cly[1]);
        // Adding this seems to yield concurrent modification exceptions
        // at high speed.
        // removeEmptyChunks();
        if (data == null && chunk.isEmpty())
        {
            chunks.remove(chunk);
        }
        return data;
    }

    /**
     * Sets the value at the given global coordinate to the given generic
     * object.
     * 
     * @param data The data to store.
     * @param x The x coordinate.
     * @param y The y coordinate.
     */
    public void setEntry(T data, int x, int y)
    {
        if (data == null)
        {
            throw new IllegalArgumentException("Data cannot be null");
        }
        int[] clx = toCL(x);
        int[] cly = toCL(y);
        getChunk(clx[0], cly[0]).setEntry(data, clx[1], cly[1]);
    }

    /**
     * Removes the entry stored at a given coordinate.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The removed object; null if no object was removed.
     */
    public T remove(int x, int y)
    {
        int[] clx = toCL(x);
        int[] cly = toCL(y);
        T data = getEntry(x, y);
        Chunk chunk = getChunk(clx[0], cly[0]);
        chunk.setEntry(null, clx[1], cly[1]);
        // We only remove THIS chunk where applicable, 
        // since we just removed an entry from it.
        /* Single chunk removal */
        if (chunk.isEmpty())
        {
            chunks.remove(chunk);
        }
        /* */
        /* Total chunk removal
        getChunk(clx[0], cly[0]).setEntry(null, clx[1], cly[1]);
        removeEmptyChunks();
        */
        return data;
    }

    /**
     * Gets the smallest and largest x-coordinates of the ChunkArray, such that
     * all entries x-coordinates will be contained in the domain.
     * 
     * @return the domain of the ChunkArray.
     */
    public int[] getDomain()
    {
        int min = 0;
        int max = 0;
        // No idea why, but this needs size() - 1.
        for (int i = 0; i < chunks.size() - 1; i++)
        {
            int x = chunks.get(i).getX();
            if (x < min)
            {
                min = x;
            }
            if (x > max)
            {
                max = x;
            }
        }
        return new int[] { min * chunkSize, (max + 1) * chunkSize };
    }

    /**
     * Gets the smallest and largest y-coordinates of the ChunkArray, such that
     * all entries' y-coordinates will be contained in the range.
     * 
     * @return the range of the ChunkArray.
     */
    public int[] getRange()
    {
        int min = 0;
        int max = 0;
        for (int i = 0; i < chunks.size(); i++)
        {
            int y = chunks.get(i).getY();
            if (y < min)
            {
                min = y;
            }
            if (y > max)
            {
                max = y;
            }
        }
        return new int[] { min * chunkSize, (max + 1) * chunkSize };
    }

    /**
     * Removes all entries in the ChunkArray.
     */
    public void clear()
    {
        chunks = new ArrayList<Chunk>();
    }

    /**
     * Gets a human-readable String representation of the contents of the
     * ChunkArray.
     * 
     * @return a String.
     */
    public String toString()
    {
        StringBuilder out = new StringBuilder("[");

        for (int i = 0; i < chunks.size(); i++)
        {
            out.append("[");
            out.append(chunks.get(i).getX());
            out.append(", ");
            out.append(chunks.get(i).getY());
            out.append(", ");
            out.append(chunks.get(i).getSize());
            out.append("], ");
        }
        // What's the purpose of this following statement?
        if (chunks.size() > 0)
        {
            out.delete(out.length() - 2, out.length());
        }
        out.append("]");
        return out.toString();
    }

    /**
     * A helper method which gets the chunk at a specified location. If the
     * Chunk does not already exist, this method creates a new one, adds it to
     * the ChunkArray, and returns it.
     * 
     * @param x The x coordinate of the Chunk.
     * @param y The y coordinate of the Chunk.
     * @return The Chunk at the specified coordinate.
     */
    private Chunk getChunk(int x, int y)
    {
        for (int i = 0; i < chunks.size() - 1; i++)
        {
            if (chunks.get(i) == null)
            {
                chunks.remove(i);
                continue;
            }
            if (chunks.get(i).getX() == x && chunks.get(i).getY() == y)
            {
                return chunks.get(i);
            }
        }
        Chunk c = new Chunk(x, y);
        chunks.add(c);
        return c;
    }
    
    /**
     * Helper method that removes empty chunks.
     */
    private void removeEmptyChunks()
    {
        for (int i = 0; i < chunks.size(); i++)
        {
            if (chunks.get(i) == null)
            {
                System.out.println("Null chunks!");
            }
            if (chunks.get(i).isEmpty())
            {
                chunks.remove(chunks.get(i));
                i--;
            }
        }
    }

    /**
     * Converts a global address (for example, 45) to a Chunk-Local address
     * (chunk 5, index 6).
     * 
     * @param a The global address.
     * @return an Integer array of length 2, containing the chunk address and
     *         local address.
     */
    private int[] toCL(int a)
    {
        int ac;

        if (a >= 0 && a < chunkSize)
        {
            ac = 0;
        }
        if (a >= chunkSize)
        {
            ac = a / chunkSize;
        }
        else
        {
            ac = 0;
            while (a < ac * chunkSize)
            {
                ac--;
            }
        }

        int al = a - ac * chunkSize;

        return new int[] { ac, al };
    }

    /**
     * Private implementation of a Chunk. Chunks are container classes for
     * generic arrays of a default size with stored location data. They are
     * created and discarded as needed by ChunkArray.
     * 
     * @author Wade Foster
     * @version 2016.11.21
     */
    private class Chunk
    {
        int x;
        int y;
        T[][] grid;
        int size;

        /**
         * Creates an empty Chunk at the specified location.
         * 
         * @param x The x coordinate.
         * @param y The y coordinate.
         */
        @SuppressWarnings("unchecked")
        public Chunk(int x, int y)
        {
            this.x = x;
            this.y = y;
            grid = (T[][]) new Object[chunkSize][chunkSize];
            size = 0;
        }

        /**
         * Gets this Chunk's x coordinate.
         * 
         * @return an Integer x.
         */
        public int getX()
        {
            return x;
        }

        /**
         * Gets this Chunk's y coordinate.
         * 
         * @return an Integer y.
         */
        public int getY()
        {
            return y;
        }

        /**
         * Returns the data stored at a specified coordinate.
         * 
         * @param x The x coordinate.
         * @param y The y coordinate.
         * @return The T object stored here.
         */
        public T getEntry(int x, int y)
        {
            return grid[x][y];
        }

        /**
         * Stores a generic object at the specified location.
         * 
         * @param data The T data to store.
         * @param x The x coordinate.
         * @param y The y coordinate.
         */
        public void setEntry(T data, int x, int y)
        {
            // These operations are negations, not if, else-if
            // Not sure what this does
            if (grid[x][y] == null && data != null)
            {
                size++;
            }
            else if (grid[x][y] != null && data == null)
            {
                size--;
            }
            // What if data == null and there is an element at [x][y]?
            grid[x][y] = data;
        }
        
        public int getSize()
        {
            return size;
        }

        /**
         * Checks if this chunk is not currently storing anything.
         * 
         * @return true if empty, false if it contains any data.
         */
        public boolean isEmpty()
        {
            return size == 0;
        }
    }
}
