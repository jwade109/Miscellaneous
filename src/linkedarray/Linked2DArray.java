package linkedarray;

public class Linked2DArray<T> implements XYArrayInterface<T>
{

    Node<T> origin;
    
    public Linked2DArray()
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
    public void setEntry(T data)
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

    private class Node<T>
    {
        Node<T> top;
        Node<T> bottom;
        Node<T> left;
        Node<T> right;
        T data;
        
        public Node()
        {
            data = null;
            top = null;
            bottom = null;
            left = null;
            right = null;
        }
        
        public Node(T data)
        {
            this.data = data;
            top = null;
            bottom = null;
            left = null;
            right = null;
        }
        
        public Node(T data, Node<T> top, Node<T> bottom, Node<T> left, Node<T> right)
        {
            this.data = data;
            this.top = top;
            this.bottom = bottom;
            this.left = left;
            this.right = right;
        }
        
        
    }
    
}
