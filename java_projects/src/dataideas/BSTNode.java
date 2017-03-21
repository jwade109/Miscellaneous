package dataideas;

/**
 * 
 * @author William McDermott
 *
 * @param <T> The data for the node to hold.
 * @param <K> The key to sort this by.
 */
public class BSTNode<T, K extends Comparable<K>> implements Comparable<BSTNode<T, K>>
{
    /**
     * The data for this object to hold.
     */
    private T data;
    
    /**
     * The key this object can be sorted by.
     */
    private K key;
    
    
    
    /**
     * Creates a new BSTNode with a key value pair.
     */
    public BSTNode(T data, K key)
    {
        this.data = data;
        this.key = key;
    }
    
    /**
     * Sets the data for this node.
     * @param data  The data to add to the BSTNode.
     */
    public void setData(T data)
    {
        this.data = data;
    }
    
    /**
     * Sets the key for this node.
     */
    public void setKey(K key)
    {
        this.key = key;
    }
    
    /**
     * Compares this node with another based on its key value.
     */
    @Override
    public int compareTo(BSTNode<T, K> otherNode)
    {
        return this.compareTo(otherNode);
    }
}
