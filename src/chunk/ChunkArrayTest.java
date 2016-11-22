package chunk;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ChunkArrayTest
{
    /**
     * A ChunkArray for the tests.
     */
    private ChunkArray<Integer> array;
    
    /**
     * Sets up the test suite.
     * Called before each method.
     */
    @Before
    public void setUp()
    {
        array = new ChunkArray<Integer>();
        array.setEntry(101, 0, 0);
        array.setEntry(102, 1, 1);
        array.setEntry(103, 49, 49);
        array.setEntry(104, 51, 51);
        array.setEntry(105, 201, 201);
        // The following lines should create empty chunks.
        array.getEntry(500, 500);
        array.getEntry(501, 501);
        // Now a non empty chunk is after them in the list.
        array.setEntry(106, 50, 0);
        array.setEntry(107, -50, 0);
        array.getEntry(10000, 10000);
        array.setEntry(108, 0, -50);
    }

    /**
     * Tests getEntry().
     */
    @Test
    public void testGetEntry()
    {
        // Normal cases.
        assertEquals(101, (int) array.getEntry(0, 0));
        assertEquals(102, (int) array.getEntry(1, 1));
        assertEquals(103, (int) array.getEntry(49, 49));
        assertEquals(104, (int) array.getEntry(51, 51));
        assertEquals(105, (int) array.getEntry(201, 201));
        assertEquals(106, (int) array.getEntry(50, 0));
        assertEquals(107, (int) array.getEntry(-50, 0));
        assertEquals(108, (int) array.getEntry(0, -50));
        
        // No value in a slot.
        assertNull(array.getEntry(-5, -10));
        
        // Testing for the linkedlist length bug.
        assertNull(array.getEntry(502, 502));
    }
    
    /**
     * Tests setEntry().
     */
    @Test
    public void testSetEntry()
    {
        array.setEntry(-101, -100, -100);
        array.setEntry(-102, -101, -100);
        assertEquals(-101, (int) array.getEntry(-100, -100));
        assertEquals(-102, (int) array.getEntry(-101, -100));
        // testing overwriting.
        array.setEntry(100, -100, -100);
        assertEquals(100, (int) array.getEntry(-100, -100));
    }
    
    /**
     * Tests the clear() method.
     */
    public void testClear()
    {
        array.clear();
        assertNull(array.getEntry(0, 0));
        assertNull(array.getEntry(1, 1));
    }
}
