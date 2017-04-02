package vector;

/**
 * The abstract class that entails both filled and sparse vector grids.
 * TODO
 * 
 * @author William McDermott
 * @version 2016.11.21
 * @param 
 */
public abstract class AbstractVectorGrid<T>
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
    public AbstractVectorGrid()
    {
        this(2);
    }
    
    /**
     * Creates a new AbstractVectorGrid in dims dimensions.
     * @param dims  The number of dimensions for this object to use.
     */
    public AbstractVectorGrid(int dims)
    {
        this.dims = dims;
        origin = new GridNode<T>(null, 0);
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
     * Gets an element at the specified vector coordinates.
     * @param vector    The vector direction to retrieve this element.
     * @throws IndexOutOfBoundsException
     */
    abstract public T getEntry(int[] dir);
    
    /**
     * Sets an element at the specified vector coordinates.
     * @param entry     The entry to include.
     * @param dir       The direction of the entry.
     */
    abstract public void setEntry(T entry, int[] dir);
    
    /**
     * Clears the entire vector grid.
     */
    abstract public void clear();
    
    /**
     * Tests if the grid contains any elements.
     * @return  Whether this grid is empty or not.
     */
    abstract public boolean isEmpty();
}

