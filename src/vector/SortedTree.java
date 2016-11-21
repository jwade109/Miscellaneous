package vector;

/**
 * A sorted BST that keeps items sorted in the structure.
 * TODO
 * 
 * @author William McDermott
 * @version 2016.11.21
 * @param T     The type the tree can hold.
 */
public class SortedTree<T extends Comparable<T>>
{
    /**
     * The top node in the tree.
     */
    private GridNode<T> node;
    
    /**
     * Creates a new SortedTree object.
     */
    public SortedTree()
    {
        node = new GridNode<T>(null, 2);
    }
}
