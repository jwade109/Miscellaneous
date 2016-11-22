package dataideas;

import vector.GridNode;

/**
 * A sorted BST that keeps items sorted in the structure.
 * TODO
 * 
 * @author William McDermott
 * @version 2016.11.21
 * @param T     The type the tree can hold.
 * @param K     The key type for a value.
 */
public class SortedTree<T, K extends Comparable<K>>
{
    /**
     * The top node in the tree.
     */
    private BSTNode<T, K> root;
    
    /**
     * Creates a new SortedTree object.
     */
    public SortedTree()
    {
        node = new GridNode<T>(null, 2);
    }
    
    /**
     * Searches for an element in the tree.
     * @return
     * Returns the element if found, 
     * else returns null
     */
    public T search(K key)
    {
        // TODO
        return null;
    }
    
    /**
     * Adds an element to the tree.
     * @param element   The element to be added to the tree.
     * @param key       The key to determine where to add that element.
     */
    public void add(T element, K key)
    {
        // TODO
    }
    
    /**
     * Removes an element from the tree.
     * @param element   The element to be removed from the tree.
     */
    public void remove(T element)
    {
        // TODO
    }
}
