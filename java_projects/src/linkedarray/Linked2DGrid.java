package linkedarray;

public class Linked2DGrid<T> implements XYArrayInterface<T>
{

    Node<T> origin;
    
    public Linked2DGrid()
    {
        origin = new Node<T>();
    }
    
    @Override
    public T getEntry(int x, int y)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setEntry(int x, int y, T data)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public T remove()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void clear()
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int size()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    private class Node<E>
    {
        Node<E> top;
        Node<E> bottom;
        Node<E> left;
        Node<E> right;
        E data;
        
        public Node()
        {
            data = null;
            top = null;
            bottom = null;
            left = null;
            right = null;
        }
        
        public Node(E data)
        {
            this.data = data;
            top = null;
            bottom = null;
            left = null;
            right = null;
        }
        
        public Node(E data, Node<E> top, Node<E> bottom,
        		Node<E> left, Node<E> right)
        {
            this.data = data;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }
        
        
    }
    
}
