package dataideas;

/**
 * Represents the Grid nodes where the edges in a graph meet.
 * These nodes represent a versatile kind of node that can have
 * any number of neighbors, and can have client code access them
 * at any point. GridNodes keep track of a CustomLinkedList of their
 * neighbors, which allows them to be used in graphs or in more 
 * organized structures as well.
 * 
 * @author William McDermott
 * @version 2016.11.21
 * 
 * @param T     The type of data this GridNode holds.
 */
public class GridNode<T>
{
    /**
     * The data contained in this GridNode.
     */
    private T data;
    
    /**
     * The neighborhood of a GridNode, the cells directly adjacent to it.
     */
    private VectorLinkedList<GridNode<T>> neighbors;
    
    /**
     * Creates a new GridNode with no neighbors.
     */
    public GridNode(T data)
    {
        this(data, new VectorLinkedList<GridNode<T>>());
    }

    /**
     * Creates a new GridNode in n dimensions.
     * @param data      The object to be held by this node.
     * @param neighbors The neighbors to this node.
     * @throws IllegalArgumentException
     */
    public GridNode(T data, VectorLinkedList<GridNode<T>> neighbors)
    {
        this.data = data;
        this.neighbors = neighbors;
    }
    
    /**
     * Gets the data of this GridNode.
     * @return  This GridNode's data.
     */
    public T getData()
    {
        return data;
    }

    /**
     * Sets the data of this GridNode.
     * @param   The new data for the GridNode to hold
     */
    public void setData(T data)
    {
        this.data = data;
    }
    
    /**
     * Gets the neighbors of this GridNode.
     * @return  The neighborhood of this GridNode.
     */
    public VectorLinkedList<GridNode<T>> getNeighbors()
    {
        return neighbors;
    }
    
    /**
     * Gets a neighbor of this GridNode in a specific direction.
     * @param vector    The direction to add the GridNode.
     * @return          One neighbor of this GridNode.
     */
    public GridNode<T> getNeighbor(int[] vector)
    {
        return this.getNeighbor(this.vectorToPosition(vector));
    }
    
    /**
     * Gets a neighbor of this GridNode in a specific direction.
     * @param position  The encoded direction to add the GridNode.
     * @return          One neighbor of this GridNode.
     */
    public GridNode<T> getNeighbor(int position)
    {
        return neighbors.getEntry(position);
    }
    
    /**
     * Sets the neighborhood of this GridNode.
     * @param neighbors     The new neighbors of this GridNode.
     * @throws IllegalArgumentException
     */
    public void setNeighborhood(VectorLinkedList<GridNode<T>> neighbors)
    {
        if (neighbors == null)
        {
            throw new IllegalArgumentException("Null neighbor list " +
                "put into setNeighborhood(). Use an empty list.");
        }
        this.neighbors = neighbors;
    }
    
    /**
     * Sets a specific neighbor based on a basis vector for direction.
     * @param neighbor  A GridNode to set as a neighbor.
     * @param vector    The direction vector to add the neighbor
     */
    public void setNeighbor(GridNode<T> neighbor, int[] vector)
    {
        this.setNeighbor(neighbor, this.vectorToPosition(vector));
    }
    
    /**
     * Sets a specific neighbor based on an integer position for direction.
     * @param neighbor  A GridNode to set as a neighbor.
     * @param position    The encoded direction to add the neighbor.
     */
    public void setNeighbor(GridNode<T> neighbor, int position)
    {
        neighbors.replace(position, neighbor);
    }
    
    /**
     * Turns a vector into a value in the array.
     * @param vector    A basis vector to process into a position.
     * @return          The position in the neighbors list.
     * @throws IllegalArgumentException
     */
    private int vectorToPosition(int[] vector)
    {
        if (vector == null)
        {
            throw new IllegalArgumentException("Vector is null!");
        }
        // Perhaps adding a check for n-1 zeroes would be helpful.
        int position = -1;
        for (int i = 0; i < vector.length; i++)
        {
            if (vector[i] == 1)
            {
                position = 2 * i;
                break;
            }
            if (vector[i] == -1)
            {
                position = 2 * i + 1;
                break;
            }
        }
        // Illegal vector.
        if (position == -1)
        {
            throw new IllegalArgumentException("Not a basis vector or "
                + "its opposite!");
        }
        return position;
    }
}
