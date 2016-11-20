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
     * @return The dimensions of this GridNode.
     */
    public int getDimensions()
    {
        return dims;
    }
    
    /**
     * Gets the neighbors of this GridNode.
     * @return  The neighborhood of this GridNode.
     */
    public GridNode[] getNeighbors()
    {
        return neighbors;
    }
    
    /**
     * Gets a neighbor of this GridNode in a specific direction.
     * @param vector    The direction to add the GridNode.
     * @return          One neighbor of this GridNode.
     */
    public GridNode getNeighbor(int[] vector)
    {
        return neighbors[this.vectorToPosition(vector)];
    }
    
    /**
     * Sets the neighborhood of this GridNode.
     * @param neighbors     The new neighbors of this GridNode.
     * @throws IllegalArgumentException
     */
    public void setNeighborhood(GridNode[] neighbors)
    {
        if (neighbors == null
            || this.neighbors.length != neighbors.length)
        {
            throw new IllegalArgumentException();
        }
        this.neighbors = neighbors;
    }
    
    /**
     * Sets a specific neighbor based on a basis vector for direction.
     * @param neighbor  A GridNode to set as a neighbor.
     * @param vector    The direction vector to add the neighbor
     * @throws IllegalArgumentException
     */
    public void setNeighbor(GridNode neighbor, int[] vector)
    {
        if (neighbor == null
            || vector.length != neighbors.length)
        {
            throw new IllegalArgumentException();
        }
        neighbors[this.vectorToPosition(vector)] = neighbor;
    }
    
    /**
     * Turns a vector into a value in the array.
     * @param vector    A vector to process into a position.
     * @return          The position to return.
     * @throws IllegalArgumentException
     */
    private int vectorToPosition(int[] vector)
    {
        if (vector == null)
        {
            throw new IllegalArgumentException();
        }
        int position = 0;
        for (int i = 0; i < vector.length; i++)
        {
            if (vector[i] == 1)
            {
                position += (int) Math.pow(2, i) + 0.01;
            }
        }
        return position;
    }
}
