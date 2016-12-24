package tictactoe;

public class BoardTesting
{
    public static void main(String[] args)
    {
        Board b = new Board(0);
        int index = 0;
        for (PlayEnum p : b)
        {
            System.out.println(index + " " + p);
            index++;
        }
    }

    /**
     * Maps a 1 dimensional number to an n-dimensional coordinate using the nonaordinal grid system
     * @param index
     * @param order
     * @return
     */
    private static int[] map(int index, int order)
    {
        if (order > Math.pow(9, order))
        {
            return new int[] {-1};
        }
        
        int[] path = new int[order];
        for (int i = 0; i < order; i++)
        {
            path[i] = index / (int) Math.pow(9, order - i - 1);
            index -= path[i] * 9;
        }
        return path;
    }

    /**
     * Maps an n-dimensional coordinate to a 1 dimensional number using the nonaordinal grid system
     * @param path
     * @return
     */
    private static int map(int[] path)
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
}
