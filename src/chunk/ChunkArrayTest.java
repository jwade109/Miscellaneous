package chunk;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class ChunkArrayTest extends TestCase
{
    /**
     * A ChunkArray for the tests.
     */
    private ChunkArray<Integer> array;

    /**
     * Sets up the test suite. Called before each method.
     */
    @Before
    public void setUp()
    {
        array = new ChunkArray<Integer>();

        // 1st chunk
        array.setEntry(101, 0, 0);
        array.setEntry(102, 1, 1);
        // 2nd chunk
        array.setEntry(103, 49, 49);
        array.setEntry(104, 51, 51);
        // 3rd chunk
        array.setEntry(105, 201, 201);
        // 4th chunk
        array.setEntry(10, -10, -8);
        // 5th chunk
        array.setEntry(11, -200, -400);
        // 6th chunk
        array.setEntry(56, -120, 34);
        // 7th chunk
        array.setEntry(70, 57, -21);
    }

    /**
     * Tests getEntry().
     */
    @Test
    public void testGetEntry()
    {
        // Normal cases.
        // Asserting twice to ensure that getEntry() does not change the
        // contents of the array.
        for (int i = 0; i < 2; i++)
        {
            assertEquals(101, (int) array.getEntry(0, 0));
            assertEquals(102, (int) array.getEntry(1, 1));
            assertEquals(103, (int) array.getEntry(49, 49));
            assertEquals(104, (int) array.getEntry(51, 51));
            assertEquals(105, (int) array.getEntry(201, 201));
            assertEquals(10, (int) array.getEntry(-10, -8));
            assertEquals(11, (int) array.getEntry(-200, -400));
            assertEquals(56, (int) array.getEntry(-120, 34));
            assertEquals(70, (int) array.getEntry(57, -21));

            // No value in a slot.
            assertNull(array.getEntry(-5, -10));
            assertNull(array.getEntry(100000, 7000000));
        }

        // testing that getEntry() doesn't create a new chunk needlessly.
        String state = array.toString();
        array.getEntry(80000, 90000);
        assertEquals(state, array.toString());
    }

    /**
     * Tests setEntry().
     */
    @Test
    public void testSetEntry()
    {
        // testing that setEntry() actually changes the array.
        array.setEntry(7, 56, 99);
        array.setEntry(33, -101, -100);
        assertEquals(7, (int) array.getEntry(56, 99));
        assertEquals(33, (int) array.getEntry(-101, -100));

        // testing overwriting.
        array.setEntry(100, 56, 99);
        assertEquals(100, (int) array.getEntry(56, 99));

        // testing IllegalArgumentException
        Exception e = null;
        try
        {
            array.setEntry(null, 0, 0);
        }
        catch (IllegalArgumentException ex)
        {
            e = ex;
        }
        assertNotNull(e);
    }

    /**
     * Tests the remove() method.
     */
    @Test
    public void testRemove()
    {
        assertEquals(101, (int) array.remove(0, 0));
        assertEquals(102, (int) array.remove(1, 1));
        assertEquals(103, (int) array.remove(49, 49));
        assertEquals(104, (int) array.remove(51, 51));

        assertNull(array.remove(100, 100));
    }

    /**
     * Tests the clear() method.
     */
    public void testClear()
    {
        assertEquals(9, array.size());
        array.clear();
        assertNull(array.getEntry(0, 0));
        assertNull(array.getEntry(1, 1));
        assertEquals(0, array.size());
        assertEquals("ChunkArray contains 0 elements in 0 chunks.",
                array.toString());
    }
    
    /**
     * Tests domain().
     */
    public void testDomain()
    {
        int[] domain = array.domain();
        assertEquals(-216, domain[0]);
        assertEquals(216, domain[1]);
        
        array.setEntry(67, 312, 99);
        assertEquals(11, (int) array.remove(-200, -400));

        domain = array.domain();
        assertEquals(-120, domain[0]);
        assertEquals(336, domain[1]);
    }
    
    /**
     * Tests range().
     */
    public void testRange()
    {
        int[] range = array.range();
        assertEquals(-408, range[0]);
        assertEquals(216, range[1]);
        
        assertEquals(105, (int) array.remove(201, 201));
        array.setEntry(77, -300, -500);

        range = array.range();
        assertEquals(-504, range[0]);
        assertEquals(72, range[1]);
    }
    
    /**
     * Tests toString().
     */
    public void testToString()
    {
        assertEquals("ChunkArray contains 9 elements in 7 chunks.",
                array.toString());
        array.getEntry(51, 51);
        array.getEntry(400, 400);
        assertEquals("ChunkArray contains 9 elements in 7 chunks.",
                array.toString());
        array.setEntry(8, 333, 888);
        assertEquals("ChunkArray contains 10 elements in 8 chunks.",
                array.toString());
    }
}
