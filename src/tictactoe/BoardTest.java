package tictactoe;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Tests the Board class for efficiency and stuff.
 * 
 * @author William McDermott (willm97)
 * @version  2016.12.22
 */
public class BoardTest
{
    /**
     * Basic tic tac toe game.
     */
    private Board game1;
    
    /**
     * Ultimate tic tac toe.
     */
    private Board game2;
    
    /**
     * Sets up the test suite.
     * Called before each method.
     */
    @Before
    public void setUp()
    {
        game1 = new Board();
        game2 = new Board(2);
    }
    
    /**
     * Deletes past test references.
     * Called after each method.
     */
    @After
    public void tearDown()
    {
        game1 = null;
        game2 = null;
    }
    
    /**
     * Tests the getOrder() method.
     * Also tests that I can run JUnit4.
     */
    @Test
    public void testGetOrder()
    {
        assertEquals(1, game1.getOrder());
        assertEquals(2, game2.getOrder());
    }
    
    /**
     * 
     */
    @Test
    public void testMove()
    {
        
    }
}
