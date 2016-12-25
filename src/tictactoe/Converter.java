package tictactoe;

/**
 * Contains a number of converter methods, which are helpful when considering
 * recursive nonatrees using various coordinate systems, defined below.
 * 
 * Tree Space: A coordinate system which defines a given node using a row
 * vector, where each natural number represents the path from one level of the
 * tree to the next. It takes n coordinates to uniquely locate the leaf node of
 * a nonatree of order n; for example, 2 coordinates are sufficient to uniquely
 * locate every cell in an order 2 nonatree, while an order 4 nonatree would
 * require 4 coordinates. In all circumstances, tree space coordinates range
 * between 0 and 8 inclusive.
 * 
 * Cartesian Space: A coordinate system which ONLY identifies the leaf nodes of
 * a tree, and maps all of them to points on the x-y plane. As such, an x and y
 * ordered pair is always sufficient to uniquely define any leaf node. Valid
 * Cartesian coordinates will range between 0 and 3^n-1 inclusive, where n is the
 * order of the nonatree, in all cases.
 * 
 * Tree Index: An index uniquely maps every leaf node to a single natural
 * number, which by design describes the order by with TreeIterator will
 * traverse the leaves. For any nonatree, the index of all leaves ranges between
 * 0 and 9^n-1 inclusive, where n is the tree's order.
 * 
 * @author Wade Foster
 * @version 2016.12.25
 */
public class Converter
{
    public static void main(String[] args)
    {
<<<<<<< HEAD
=======
        for (int i = 0; i < 9; i++)
        {
            for (int j = 0; j < 9; j++)
            {
                int[] coordinates = Converter.toTreeCoordinates(new int[]{j, i}, 2);
                System.out.println("(" + coordinates[0] + ", " + coordinates[1] + ")");
            }
        }
>>>>>>> origin/master
    }

    /**
     * Tree Index -> Tree Space
     * 
     * Maps a 1-dimensional number to an n-dimensional coordinate using the
     * non-ordinal grid system.
     * 
     * @param index 1-dimensional coordinate to be mapped to n dimensions.
     * @param dim The number of dimensions to map into.
     * @return An n-dimensional vector.
     */
    public static int[] expandToTreeCoordinates(int index, int dim)
    {
        if (index > Math.pow(9, dim) - 1)
        {
            int[] bad = new int[dim];
            for (int i = 0; i < dim; i++)
            {
                bad[i] = -1;
            }
            return bad;
        }

        int[] path = new int[dim];
        for (int i = 0; i < dim; i++)
        {
            path[i] = index / (int) Math.pow(9, dim - i - 1);
            index -= path[i] * 9;
        }
        return path;
    }

    /**
     * Tree Space -> Tree Index
     * 
     * Maps a tree space coordinate to a 1-dimensional number using the
     * nonaordinal grid system. This method defines the order in which leaf
     * nodes will be traversed by a TreeIterator, and imposes an order on cells
     * in any game of TicTacGrow.
     * 
     * @param path The n-dimensional coordinate to map into one dimension.
     * @return A one-to-one mapping from n dimensional tree space into a
     *         1-dimensional location.
     */
    public static int compressToTreeIndex(int[] tree)
    {
        for (int i = 0; i < tree.length; i++)
        {
            if (tree[i] > 8)
            {
                return -1;
            }
        }
        int sum = 0;
        for (int i = 0; i < tree.length; i++)
        {
            sum += Math.pow(9, tree.length - i - 1) * tree[i];
        }
        return sum;
    }

    /**
     * Cartesian Space -> Tree Space
     * 
     * Converts an Cartesian ordered coordinate pair (x, y) into an address in
     * recursive tree space.
     * 
     * @param cartesian The Cartesian coordinates, (x, y).
     * @return A tree-space vector of a defined dimension.
     */
    public static int[] toTreeCoordinates(int[] cartesian, int dim)
    {
        if (cartesian.length != 2)
        {
            throw new IllegalArgumentException(
                    "Cartesian coordinates must be 2 dimensional");
        }
        int[] target = new int[dim];
        int x = cartesian[0];
        int y = cartesian[1];
        for (int i = dim - 1; i >= 0; i--)
        {
            target[i] = x % 3 + 3 * (y % 3);
            x = x / 3;
            y = y / 3;
        }
        return target;
    }

    /**
     * Tree Space -> Cartesian Space
     * 
     * Converts tree space coordinates of arbitrary dimension to Cartesian
     * two-dimensional coordinates.
     * 
     * @param tree The tree space vector.
     * @return An int[] of length two, containing (x, y).
     */
    public static int[] toCartesianCoordinates(int[] tree)
    {
        int dim = tree.length;
        int x = 0;
        int y = 0;
        for (int i = 0; i < dim; i++)
        {
            int multiplier = (int) Math.pow(3, dim - 1 - i);
            x += (tree[i] % 3) * multiplier;
            y += (tree[i] / 3) * multiplier;
        }
        return new int[] {x, y};
    }
}
