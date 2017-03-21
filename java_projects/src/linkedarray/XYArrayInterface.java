package linkedarray;

public interface XYArrayInterface<T>
{    
    public T getEntry(int x, int y);
    
    public void setEntry(int x, int y, T data);
    
    public T remove();
    
    public void clear();
    
    public int size();
}
