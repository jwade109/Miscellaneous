package tictactoe;

/**
 * Stores an abstract coordinate for a method to use.
 * 
 * 
 * @author William McDermott
 * @version 2016.12.27
 */
public class Coordinate
{
    /**
     * A tree path (length order) to the relevant cell.
     */
    private final int[] treePath;
    
    /**
     * A cartesian coordinate (length 2) to the relevant cell.
     */
    private final int[] cartesian;
    
    /**
     * A single index for the tree.
     */
    private final int index;
    
    /** 
     * Can these constructor calls be chained in such a way as to minimize
     * calls to the Converter methods?
     */
    
    /**
     * Creates a new Coordinate from an int[] array of tree adjacencies.
     * 
     * @param treePath  The path to the specified coordinate.
     */
    public Coordinate(int[] treePath)
    {
        this.treePath = treePath;
        this.index = Converter.compressToTreeIndex(treePath);
        this.cartesian = Converter.toCartesianCoordinates(treePath);
    }
    
    /**
     * Creates a new Coordinate from an int[] of two cartesian coordinates.
     * 
     * @param cartesian     An x and y coordinate to find a grid location.
     * @param order         The order of the tree being dealt with.
     */
    public Coordinate(int[] cartesian, int order)
    {
        if (cartesian.length != 2)
        {
            throw new IllegalArgumentException("Improper cartesian coordinate,"
                + " length = " + cartesian.length);
        }
        this.treePath = Converter.toTreeCoordinates(cartesian, order);
        this.index = Converter.compressToTreeIndex(treePath);
        this.cartesian = cartesian;
    }
    
    /**
     * Creates a new Coordinate from an int[] array of tree adjacencies.
     * 
     * @param index     The index location of a cell, 
     * just as the iterator enumerates them.
     */
    public Coordinate(int index, int order)
    {
        this.treePath = Converter.expandToTreeCoordinates(index, order);
        this.index = index;
        this.cartesian = Converter.toCartesianCoordinates(treePath);
    }
    
    /**
     * Gets the treePath associated with this Coordinate.
     * 
     * @return  The tree path to the specified place.
     */
    public int[] getTreePath()
    {
        return treePath;
    }
    
    /**
     * Gets the cartesian coordinates associated with this Coordinate.
     * 
     * @return  The cartesian array to the specified place.
     */
    public int[] getCartesian()
    {
        return cartesian;
    }
    
    /**
     * Gets the tree index associated with this Coordinate.
     * 
     * @return  The index to the specified coordinate, as the iterator uses.
     */
    public int getTreeIndex()
    {
        return index;
    }
}
