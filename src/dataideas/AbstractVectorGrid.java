package dataideas;

/**
 * This abstract class represents an arbitrary-dimensional,
 * linked grid structure, in which generic elements can be added
 * list-like at the edges or middle of the vector space, which
 * expand when objects are added at the boundary. This object is not
 * sparse, so the elements inherently have dimensional neighbors,
 * in the basis vector directions from their location.
 * 
 * @author William McDermott
 * @version 2016.11.20
 * 
 * @param T     The type this vector grid holds as data.
 */
public abstract class AbstractVectorGrid<T>
{
    /**
     * The basis vectors for this vector space.
     */
    private int[][] basis;
    
    /**
     * The origin node reference.
     */
    private final GridNode<T> origin;
    
    /**
     * The number of dimensions this grid has.
     */
    private final int dims;
    
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
        origin = new GridNode<T>(null, new CustomLinkedList<GridNode<T>>());
        basis = new int[2 * dims][dims];
        for (int i = 0; i < dims; i++)
        {
            int[] basisVector = new int[dims];
            basisVector[i] = 1;
            int[] basisOpposite = new int[dims];
            basisOpposite[i] = -1;
            this.basis[2 * i] = basisVector;
            this.basis[2 * i + 1] = basisOpposite;
        }
    }
    
    /**
     * Gets an element at the specified vector coordinates.
     * @param vector    The vector direction to retrieve this element.
     * @throws IndexOutOfBoundsException
     */
    abstract public T getEntry(int[] direction);
    
    /**
     * Clears the entire vector grid.
     */
    abstract public void clear();
    
    /**
     * Returns the box dimensions of this VectorGrid. 
     * This means that many of the included elements may be null
     */
    abstract public int[] getDims();
}
