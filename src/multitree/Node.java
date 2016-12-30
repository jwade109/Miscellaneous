package multitree;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * An inner class implementation of a tree node, which has a specific number of
 * children and a generic data type.
 * 
 * @author Wade Foster
 * @version 2016.12.26
 */
public class Node<T> implements Iterable<Node<T>>
{
    private ArrayList<Node<T>> children;
    private Node<T> parent;
    private T data;
    private int width;

    /**
     * Constructs a Node at a specific level and containing a specific T object.
     * A Node's level describes how many levels will be constructed below it.
     * For example, a Node of level 0 will have 0 generations of children
     * beneath it, while a Node of level 2 will have 2 generations. All nodes,
     * if they have any children, will always have width children.
     * 
     * @param level The number of generations below this Node.
     */
    public Node(int level, int width, T data, Node<T> parent)
    {
        children = new ArrayList<Node<T>>();
        this.parent = parent;
        this.data = data;
        this.width = width;
        if (level > 0)
        {
            for (int i = 0; i < width; i++)
            {
                children.add(new Node<T>(level - 1, width, data, this));
            }
        }
    }

    /**
     * Sets the data for this Node.
     * 
     * @param data The data to be stored by this Node.
     */
    public void setData(T data)
    {
        this.data = data;
    }

    /**
     * Gets this Node's data.
     * 
     * @return A generic T object.
     */
    public T getData()
    {
        return data;
    }

    /**
     * Gets the nth child of this Node. Throws an error
     * 
     * @param index The index of the desired child Node.
     * @return The child Node, if it exists.
     */
    public Node<T> getChild(int index)
    {
        if (!hasChildren())
        {
            throw new NoSuchElementException("This node has no children");
        }
        if (index >= 0 && index < width)
        {
            return children.get(index);
        }
        throw new IllegalArgumentException("No child node at index " + index);
    }

    /**
     * Gets all the children of this Node.
     * 
     * @return A list of Nodes.
     */
    public ArrayList<Node<T>> getChildren()
    {
        return children;
    }

    /**
     * Gets this Node's parent Node.
     * 
     * @return The parent Node.
     */
    public Node<T> getParent()
    {
        return parent;
    }

    /**
     * Checks whether this Node has any children.
     * 
     * @return True if it has children, false if not.
     */
    public boolean hasChildren()
    {
        return children.size() > 0;
    }

    /**
     * Gets a String representation of this Node.
     * 
     * @return A String.
     */
    public String toString()
    {
        StringBuilder out = new StringBuilder("Node containing ");
        if (data == null)
        {
            out.append("null data");
        }
        else
        {
            out.append(data.toString());
        }
        out.append(" and ");
        if (children == null)
        {
            out.append("0");
        }
        else
        {
            out.append(children.size());
        }
        out.append(" children");
        return out.toString();
    }

    /**
     * Gets an Iterator for the children of this Node.
     * 
     * @return An Iterator.
     */
    public Iterator<Node<T>> iterator()
    {
        return children.iterator();
    }
}
