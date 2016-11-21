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
public abstract class AbstractFilledVectorGrid<T>
{
    /**
     * The number of dimensions this grid has.
     */
    private final int dims;
    
    /**
     * The basis vectors for this vector space.
     */
    private int[][] basis;
    
    /**
     * The index of elements in each basis direction,
     * from the origin node.
     */
    private int[] dimBounds;
    
    /**
     * The reference to the origin of the vector field.
     */
    private final GridNode<T> origin;
    
    /**
     * Creates a new AbstractVectorGrid, in the default
     * of two dimensions.
     */
    public AbstractFilledVectorGrid()
    {
        this(2);
    }
    
    /**
     * Creates a new AbstractVectorGrid in dims dimensions.
     * @param dims  The number of dimensions for this object to use.
     */
    public AbstractFilledVectorGrid(int dims)
    {
        this.dims = dims;
        origin = new GridNode<T>(null, this.createEmptyList());
        basis = new int[2 * dims][dims];
        for (int i = 0; i < dims; i++)
        {
            // Initializing the dimension bounds.
            dimBounds[i] = 0;
            // Initializing the basis vectors.
            int[] basisVector = new int[dims];
            basisVector[i] = 1;
            int[] basisOpposite = new int[dims];
            basisOpposite[i] = -1;
            this.basis[2 * i] = basisVector;
            this.basis[2 * i + 1] = basisOpposite;
        }
    }
    
    /**
     * Returns the length dimensions of this VectorGrid. 
     * This means that many elements in the dimensions box may be null,
     * but that the box has no elements outside of those ranges.
     */
    public int[] getDims()
    {
        return dimBounds;
    }
    
    /**
     * Initializes an empty CustomLinkedList for a GridNode<T>.
     */
    private VectorLinkedList<GridNode<T>> createEmptyList()
    {
        VectorLinkedList<GridNode<T>> target =
            new VectorLinkedList<GridNode<T>>();
        for (int i = 0; i < dims; i++)
        {
            target.add(null);
        }
        return target;
    }
    
    /**
     * Gets an element at the specified vector coordinates.
     * @param vector    The vector direction to retrieve this element.
     * @throws IndexOutOfBoundsException
     */
    abstract public T getEntry(int[] dir);
    
    /**
     * Sets an element at the specified vector coordinates
     */
    abstract public void setEntry(T entry, int[] dir);
    
    /**
     * Clears the entire vector grid.
     */
    abstract public void clear();
    
    /**
     * Shrinks a direction of the grid towards the origin.
     * Note that all data in the removed area is lost upon a shrink.
     * @param dir   The direction to shrink inward.
     * @throws IllegalStateException    If the shrink would remove the origin.
     */
    abstract public void shrink(int[] dir);
    
    /**
     * Expands the direction of a grid away from the origin.
     * @param dir   The direction to expand outward.
     */
    abstract public void expand(int[] dir);
    
    /**
     * Tests if the grid contains any elements.
     * @return  Whether this grid is empty or not.
     */
    abstract public boolean isEmpty();
}
