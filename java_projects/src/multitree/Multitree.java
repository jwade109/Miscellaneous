package multitree;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The Multitree is an object which stores data and relationships in and between
 * nodes. The structure of a Mulitree is determined by its constructor arguments
 * and is immutable once constructed, however the data inside may be freely
 * altered and the Node objects which make up the tree may be passed to other
 * classes.
 * 
 * @author Wade Foster
 * @version 2016.12.26
 *
 * @param <T> The data type to be stored.
 */
public class Multitree<T> implements Iterable<Node<T>>, Cloneable
{
    private Node<T> root;
    private int depth;
    private int width;

    /**
     * Constructs a new full, balanced, complete, and empty Multitree.
     * 
     * @param depth The depth of the tree. A depth of 0 will generate a tree
     *        which is only a root node.
     * @param width
     */
    public Multitree(int depth, int width)
    {
        if (depth < 0 || width < 0)
        {
            throw new IllegalArgumentException(
                    "Depth and width must be greater than 0");
        }
        this.depth = depth;
        this.width = width;
        fill(null);
    }

    /**
     * Gets the root Node of this Multitree.
     * 
     * @return The root Node.
     */
    public Node<T> root()
    {
        return root;
    }

    /**
     * Finds the Node at the given path.
     * 
     * @param path A series of integers encoding the path from the root. A
     *        zero-length array denotes the root of the tree.
     * @return The requested Node.
     */
    public Node<T> findNode(int[] path)
    {
        Node<T> current = root;
        boolean stop = false;
        for (int i = 0; i < path.length && current.hasChildren() && !stop; i++)
        {
            if (path[i] > -1)
            {
                current = current.getChild(path[i]);
            }
            else
            {
                stop = true;
            }
        }
        return current;
    }

    /**
     * Gets the path of a Node in the Multitree.
     * 
     * @param node The Node to find.
     * @return The path of the given Node.
     */
    public int[] findPath(Node<T> node)
    {
        TreeIterator iter = new TreeIterator();
        while (iter.hasNext())
        {
            int[] path = iter.path();
            if (iter.next() == node)
            {
                return path;
            }
        }
        throw new NoSuchElementException("Node cannot be found");
    }

    /**
     * Replaces all data within the Multitree with a given object.
     * 
     * @param data The data to fill the tree with.
     */
    public void fill(T data)
    {
        root = new Node<T>(depth, width, data, null);
    }

    /**
     * Gets the depth of this tree.
     * 
     * @return An Integer.
     */
    public int depth()
    {
        return depth;
    }

    /**
     * Gets the width of the first sublevel of this tree, which is also the
     * maximum number of children per Node.
     * 
     * @return An Integer.
     */
    public int width()
    {
        return width;
    }

    /**
     * Gets a String representation of this Multitree.
     * 
     * @return A String representation the Multitree's structure and contents.
     */
    public String toString()
    {
        return toString(root, 0);
    }

    /**
     * Get a clone of this Multitree.
     * 
     * @return An identical Multitree.
     */
    public Multitree<T> clone()
    {
        Multitree<T> clone = new Multitree<T>(depth, width);
        {
            for (Node<T> node : clone)
            {
                int[] path = clone.findPath(node);
                node.setData(this.findNode(path).getData());
            }
        }
        return clone;
    }
    
    /**
     * Helper method which recursively compiles a String representation of the
     * Multitree.
     * 
     * @param root The root Node for the iteration.
     * @param level The current level of the iteration.
     * @return A String representation of the subtree defined by the root Node.
     */
    private String toString(Node<T> root, int level)
    {
        StringBuilder out = new StringBuilder();
        if (root.getData() != null)
        {
            out.append(root.getData().toString());
        }
        else
        {
            out.append("null");
        }
        out.append("\n");
        if (root.hasChildren())
        {
            for (Node<T> child : root)
            {
                for (int i = 0; i < level + 1; i++)
                {
                    out.append("|");
                }
                out.append(" ");
                out.append(toString(child, level + 1));
            }
        }

        return out.toString();
    }

    /**
     * Gets an iterator for this Multitree.
     * 
     * @return A TreeIterator.
     */
    public TreeIterator iterator()
    {
        return new TreeIterator();
    }

    /**
     * An iterator which traverses the Multitree using a pre-order traversal.
     * 
     * @author Wade Foster
     * @version 2016.12.27
     */
    private class TreeIterator implements Iterator<Node<T>>
    {
        private int level;
        private int[] path;

        public TreeIterator()
        {
            level = 0;
            path = new int[] {};
        }

        public boolean hasNext()
        {
            return path != null;
        }

        public Node<T> next()
        {
            if (!hasNext())
            {
                throw new NoSuchElementException();
            }
            Node<T> out = findNode(path);
            incrementPath();
            return out;
        }
        
        private int[] path()
        {
            return path.clone();
        }
        
        private void incrementPath()
        {
            boolean max = true;
            for (int i = 0; i < path.length; i++)
            {
                if (path[i] < width - 1)
                {
                    max = false;
                }
            }
            if (max)
            {
                level++;
                path = new int[level];
            }
            else
            {
                path[0]++;
            }
            for (int i = 0; i < path.length - 1; i++)
            {
                if (path[i] == width)
                {
                    path[i] = 0;
                    path[i + 1]++;
                }
            }
            if (level > depth)
            {
                path = null;
            }
        }
    }
}
