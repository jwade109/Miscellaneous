package tictactoe;

/**
 * This class represents the area of tic tac toe cells that players play on.
 * Stored as a full tree where each parent has 9 children, each separate 
 * Grid in the board stores its own information about which spaces have been 
 * filled, and whether it is won by someone or not. Each leaf is an object with
 * an enum containing who has played in it, or that no one played in it.
 * 
 * Actually, let me start implementing and then figure out what I am saying...
 * 
 * @author William McDermott
 * @version 2016.12.21
 */
public class Board
{
    /**
     * The reference to the head node.
     */
    private BoardNode game;
    
    /**
     * The number of moves that have been made,
     * where one move is counted for each player.
     */
    private int move;
    
    /**
     * The order of this tic tac toe game.
     */
    private int order;
    
    /**
     * Whether or not the
     */
    private static final boolean FREE_MOVE = true;
    
    /**
     * Creates a new board of the default order.
     */
    public Board()
    {
        this(1);
    }
    
    /**
     * Creates a new board of a given order.
     * @param order     The order to make the board.
     */
    public Board(int order)
    {
        game = new BoardNode(PlayEnum.U);
        this.order = order;
        move = 0;
    }
    
    /**
     * Makes a move for the specified player,
     * at the specified coordinates.
     * 
     * This is like setting a state in the tree.
     * 
     * @param shape     The specified player's enum.
     * @param place     Where the player just moved.
     */
    public void move(PlayEnum shape, int[] place) 
        throws OccupiedSpotException
    {
        if (shape == PlayEnum.U || place.length != order)
        {
            throw new IllegalArgumentException("Invalid Move!");
        }
        BoardNode cell = this.getCell(place);
        if (cell.getState() != PlayEnum.U)
        {
            throw new OccupiedSpotException();
        }
        cell.setState(shape);
        move++;
        this.updateWinners(place);
    }
    
    //THE FOLLOWING METHOD DOES NOT AGREE WITH THE FREE_MOVE OPTION... I think.
    
    /**
     * Gets a state of a cell safely, by navigating down the tree 
     * only where not null, and creating cells where they should exist.
     * @param coordinates   The coordinates of the cell to get.
     * @return              The state of the cell to change.
     */
    private BoardNode getCell(int[] coordinates)
    {
        BoardNode current = game;
        for (int i = 0; i < order; i++)
        {
            BoardNode next = current.getChild(coordinates[i]);
            if (i != order - 1 && next == null)
            {
                current.setChild(coordinates[i], new BoardNode(PlayEnum.U));
            }
            else if (next == null)
            {
                current.setChild(coordinates[i], new BoardNode());
            }
            current = current.getChild(coordinates[i]);
        }
        return current;
    }
    
    /**
     * After a move at the specified coordinates, tests if the game is won.
     * @param coords    The coordinates where the last move was made.
     */
    private void updateWinners(int[] coords)
    {
        updateWinnersRecursive(game, coords);
    }
    
    /**
     * Checks the win condition after a move recursively.
     * @param coords    The coordinates where the move needs to be checked.
     * @param index     The index of how deep in the game board the method is.
     * @param current   The node to settle the win condition of.
     */
    private static PlayEnum updateWinnersRecursive(
        BoardNode current, int[] coords)
    {
        if (coords.length == 0 || 
            (FREE_MOVE && current.getState() != PlayEnum.U))
        {
            return current.getState();
        }
        // truncating the first coordinate.
        int[] newCoords = new int[coords.length - 1];
        for (int i = 1; i < coords.length; i++)
        {
            newCoords[i - 1] = coords[i];
        }
        // getting what the child's state is, where the move was
        PlayEnum movedState = updateWinnersRecursive(
            current.getChild(coords[0]), newCoords);
        if (movedState == PlayEnum.U)
        {
            return PlayEnum.U;
        }
        current.getChild(coords[0]).setState(movedState);
        return Board.isSubGridWon(current);
    }
    
    /**
     * Tests whether a subgrid is won.
     * @return  Whether this 9x9 grid, disregarding depth, is won or not.
     */
    private static PlayEnum isSubGridWon(BoardNode subGrid)
    {
        // Getting the states of all 9 cells
        PlayEnum[][] states = new PlayEnum[3][3];
        for (int i = 0; i < 9; i++)
        {
            states[i / 3][i % 3] = subGrid.getChild(i).getState();
        }
        // Testing the states
        for (PlayEnum p : new PlayEnum[]{PlayEnum.X, PlayEnum.O})
        {
            // Rows and columns
            for (int i = 0; i < 3; i++)
            {
                boolean hasWonRow = true;
                boolean hasWonColumn = true;
                for (int j = 0; j < 3; j++)
                {
                    if (states[i][j] != p)
                    {
                        hasWonRow = false;
                    }
                    if (states[j][i] != p)
                    {
                        hasWonColumn = false;
                    }
                }
                if (hasWonRow || hasWonColumn)
                {
                    return p;
                }
            }
            // Diagonals
            boolean hasWonMain = true;
            boolean hasWonOff = true;
            for (int i = 0; i < 3; i++)
            {
                if (states[i][i] != p)
                {
                    hasWonMain = false;
                }
                if (states[i][2 - i] != p)
                {
                    hasWonOff = false;
                }
            }
            if (hasWonMain || hasWonOff)
            {
                return p;
            }
        }
        return PlayEnum.U;
    }
    
    /**
     * Tests if the board is won by a player.
     */
    public PlayEnum isWonBy()
    {
        return game.getState();
    }
    
    /**
     * Clears the board of stuff, for a new game.
     */
    public void clear()
    {
        game = new BoardNode(PlayEnum.U);
        move = 0;
    }
    
    // Need a toArray() for the front end too, or at least something
    // it can manage.
}
