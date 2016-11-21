package vector;

/**
 * This abstract class represents an arbitrary-dimensional,
 * linked grid structure, in which generic elements can be added
 * list-like at the edges or middle of the vector space, which
 * expand when objects are added at the boundary. This object is not
 * sparse, so the elements inherently have dimensional neighbors,
 * in the basis vector directions from their location.
 * 
 * @author William McDermott
 * @version 2016.11.21
 * 
 * @param T     The type this vector grid holds as data.
 */
public class AbstractFilledVectorGrid<T>
{
    /**
     * Shrinks a direction of the grid towards the origin.
     * Note that all data in the removed area is lost upon a shrink.
     * @param dir   The direction to shrink inward.
     * @throws IllegalStateException    If the shrink would remove the origin.
     */
    public void shrink(int[] dir)
    {
        
    }
    
    /**
     * Expands the direction of a grid away from the origin.
     * @param dir   The direction to expand outward.
     */
    public void expand(int[] dir)
    {
        
    }
}