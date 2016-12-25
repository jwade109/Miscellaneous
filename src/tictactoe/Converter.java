package tictactoe;

public class Converter
{
    public static void main(String[] args)
    {
        
    }

    /**
     * Maps a 1-dimensional number to an n-dimensional coordinate using the
     * nonaordinal grid system.
     * 
     * @param index 1-dimensional coordinate to be mapped to n dimensions.
     * @param dim The number of dimensions to map into.
     * @return An n-dimensional vector.
     */
    public static int[] transform(int index, int dim)
    {
        if (dim > Math.pow(9, dim))
        {
            return new int[] {-1};
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
     * Maps an n-dimensional coordinate to a 1-dimensional number using the
     * nonaordinal grid system.
     * 
     * @param path The n-dimensional coordinate to map into one dimension.
     * @return A one-to-one mapping from n dimensions into a 1-dimensional
     *         coordinate.
     */
    public static int transform(int[] path)
    {
        for (int i = 0; i < path.length; i++)
        {
            if (path[i] > 8)
            {
                return -1;
            }
        }
        int sum = 0;
        for (int i = 0; i < path.length; i++)
        {
            sum += Math.pow(9, path.length - i - 1) * path[i];
        }
        return sum;
    }

    /**
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
        return null;
    }

    /**
     * Converts tree-space coordinates of arbitrary dimension to Cartesian
     * two-dimensional coordinates.
     * 
     * @param tree The tree-space vector.
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
            x += unitCoords(tree[i])[0] * multiplier;
            y += unitCoords(tree[i])[1] * multiplier;
        }
        return new int[] {x, y};
    }

    /**
     * Maps the 9 squares in a Tic-Tac-Toe grid to unit coordinates between (0,
     * 0) and (2, 2).
     * 
     * @param n The TTT square to map.
     * @return The unit coordinate, (x, y).
     */
    private static int[] unitCoords(int n)
    {
        int x = n % 3;
        int y = n / 3;
        return new int[] {x, y};
    }
}
