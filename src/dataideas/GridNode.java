package dataideas;

/**
 * Represents the Grid nodes where the edges in a vector space meet.
 * 
 * @author William McDermott
 * @version 2016.11.19
 */
public class GridNode
{
    /**
     * The neighborhood of a GridNode, the cells directly adjacent to it.
     */
    private GridNode[] neighbors;

    /**
     * The dimensions of the current space this GridNode is in.
     */
    private final int dims;

    /**
     * Creates a new GridNode in two dimensions.
     * @param dims      The number of dimensions this node exists in.
     * @param neighbors The neighbors to this node.
     * @throws IllegalArgumentException
     */
    public GridNode(int dims, GridNode[] neighbors)
    {
        this.dims = dims;
        if (neighbors.length != 2 * dims)
        {
            throw new IllegalArgumentException("Incorrect array size "
                + "fed into GridNode: " + neighbors.length);
        }
        this.neighbors = neighbors;
    }
    
    /**
     * Gets the number of dimensions this GridNode exists in.
     */
    public int getDimensions()
    {
        return dims;
    }
}
