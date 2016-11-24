package cellular;

/**
 * The back end structure that represents the grid of Cell objects.
 * This object's sole purpose is to compute the next state of the automata,
 * for an entire grid of cells. Because the cells are bounded, this is actually
 * a bit difficult, since edges and corners need to be considered.
 * 
 * @author William McDermott
 * @version 2016.11.24
 */
public class Grid
{
    private Cell[][] tiles;
    private int generation;
    private Engine runner;


    /**
     * Creates a new Grid with the given dimensions, and the given
     * type of automata.
     */
    public Grid(int dimX, int dimY, String type)
    {
        generation = 0;
        runner = createEngine(type);
        tiles = new Cell[dimX][dimY];
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                tiles[i][j] = new Cell();
            }
        }
        this.intializeDebugDemo(dimX, dimY, type);
    }

    /**
     * The debug demos of the different automata.
     */
    public void intializeDebugDemo(int dimX, int dimY, String type)
    {
     // DEFAULT GRID SET UPS
        // Test requires no default set up.
        // DEBUG FOR LIFE
        // A GLIDER.
        if ("life".equals(type))
        {
            tiles[2][2] = new Cell(1);
            tiles[3][3] = new Cell(1);
            for (int i = 0; i < 3; i++)
            {
                tiles[i + 1][4] = new Cell(1);
            }
        }
        // DEBUG FOR LANGTON'S ANT
        // THE ANT ALONE.
        if ("ant".equals(type))
        {
            tiles[dimX / 2][dimY / 2] = new Cell(4);
        }
        // DEBUG FOR WIRE WORLD
        // A LOOPING WIRE
        if ("wire".equals(type))
        {
            for (int i = 0; i < 7; i++)
            {
                tiles[10 + i][10].setState(3);
                tiles[12 - i][12].setState(3);
                tiles[10][10 + i].setState(3);
                tiles[12][12 - i].setState(3);
            }
            tiles[11][10].setState(2);
            tiles[10][10].setState(1);
            tiles[10][11].setState(1);
        }
    }

    /**
     * Returns the state of a Cell.
     */
    public int getState(int x, int y)
    {
        return tiles[x][y].getState();
    }


    /**
     * Sets the state of a Cell.
     */
    public void setState(int x, int y, int state)
    {
        tiles[x][y].setState(state);
    }


    /**
     * Returns the X dimension of this Grid.
     */
    public int getDimX()
    {
        return tiles.length;
    }


    /**
     * Returns the Y dimension of this Grid.
     */
    public int getDimY()
    {
        return tiles[0].length;
    }


    /**
     * Sets the generation count.
     */
    public void setGeneration(int gen)
    {
        generation = gen;
    }


    /**
     * Gets the generation count.
     */
    public int getGeneration()
    {
        return generation;
    }


    public Engine createEngine(String type)
    {
        Profile profile = ProfileManager.getProfile(type);
        if (profile == null)
        {
            System.out.println("Did not find automata type!");
            System.exit(0);
        }
        return profile.getEngine();
    }


    /**
     * Calculates the next state of the simulation and then changes to it.
     */
    public void tick()
    {
        generation++;
        // Calculate the next state for each cell.
        cellNextStates();
        // Switch to the next state.
        for (int i = 0; i < tiles.length; i++)
        {
            for (int j = 0; j < tiles[i].length; j++)
            {
                tiles[i][j].setToNext();
            }
        }
    }


    /**
     * Calculates the next state for cells at the corners, edges, and center.
     */
    public void cellNextStates()
    {
        cornersNextState();
        edgesNextState();
        centerNextState();
    }


    /**
     * Calculates the next state for cells at the corners.
     */
    public void cornersNextState()
    {
        final int MaxX = tiles.length - 1;
        final int MaxY = tiles[0].length - 1;
        // For each corner, get the neighborhood and calculate the next state.
        // This iterates through the corners in a "Z" shape.
        for (byte dir = 0; dir < 4; dir++)
        {
            Cell[] neighbors = new Cell[9];
            // Define the beginning of the 2x2 square neighborhood.
            int x = MaxX - 1;
            int y = MaxY - 1;
            // Min and max bit magic.
            if (dir % 2 == 0)
            {
                x = 0;
            }
            if (dir / 2 == 0)
            {
                y = 0;
            }
            // This formula will give where the first cell in
            // the neighbor array should start.
            byte offset = (byte)((1 - dir / 2) * 3 + (1 - dir % 2));
            // For each element in the square, add the cells
            // into the neighborhood.
            for (byte i = 0; i < 4; i++)
            {
                byte lSig = (byte)(i % 2);
                byte mSig = (byte)(i / 2);
                neighbors[offset + i + mSig] = tiles[x + lSig][y + mSig];
            }
            runner.computeNextState(neighbors);
        }
    }


    /**
     * Calculates the next state for cells at the edges.
     * This method is so horrible to look at I apologize if you can read
     * this source code. Also it's currently broken, in a subtle way.
     */
    public void edgesNextState()
    {
        Cell[] neighbors = new Cell[9];
        // For each edge
        for (int dir = 0; dir < 4; dir++)
        {
            int x = 0;
            int y = 0;
            final int xMax = tiles.length - 1;
            final int yMax = tiles[0].length - 1;
            int length = 0;
            switch (dir)
            {
                case 0:
                    length = xMax;
                    y = 0;
                    break;
                case 1:
                    length = yMax;
                    x = 0;
                    break;
                case 2:
                    length = xMax;
                    y = yMax;
                    break;
                case 3:
                    length = yMax;
                    x = xMax;
                    break;
                default:
                    System.out.println("ERROR");
            }
            // For each cell on the given edge
            for (int edge = 1; edge < length; edge++)
            {
                // Iterate along the edge.
                // For each adjacent cell, add to this cell's neighborhood.
                for (int i = -1; i < 2; i++)
                {
                    for (int j = -1; j < 2; j++)
                    {
                        // I am deeply sorry for anyone reading this source
                        // code, if only because of this loop's content.
                        if (x + i < 0 || x + i > tiles.length - 1 || y + j < 0
                            || y + j > tiles[0].length - 1)
                        {
                            neighbors[3 * i + j + 4] = null;
                        }
                        else
                        {
                            if (dir % 2 == 0)
                            {
                                neighbors[3 * i + j + 4] = tiles[x + edge + i][y
                                    + j];
                            }
                            else
                            {
                                neighbors[3 * i + j + 4] = tiles[x + i][y + edge
                                    + j];
                            }
                        }
                    }
                }
                runner.computeNextState(neighbors);
            }
        }
    }


    /**
     * Calculates the next state for cells in the center.
     */
    public void centerNextState()
    {
        Cell[] neighbors = new Cell[9];
        for (int x = 1; x < tiles.length - 1; x++)
        {
            for (int y = 1; y < tiles[x].length - 1; y++)
            {
                for (int i = -1; i < 2; i++)
                {
                    for (int j = -1; j < 2; j++)
                    {
                        neighbors[3 * i + j + 4] = tiles[x + i][y + j];
                    }
                }
                runner.computeNextState(neighbors);
            }
        }
    }
}
