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
 * @version 2016.12.23
 */
public class Board
{
    /**
     * The reference to the head node.
     */
    private BoardNode game;
    
    /**
     * The order of this tic tac toe game.
     */
    private int order;
    
    /**
     * Whether or not won subgrids restrict movement.
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
    }
    
    /**
     * Gets the order of this grid.
     * @return  The depth of subgrids this board goes to.
     */
    public int getOrder()
    {
        return order;
    }
    
    /**
     * Gets if the free move is true or not.
     * @return  Whether or not this board considers free moves.
     * The first implementation will not consider a lack of free moves.
     */
    public boolean getFreeMove()
    {
        return FREE_MOVE;
    }
    
    /**
     * Sets the state for a cell or subgrid in the tree.
     * 
     * @param shape     The specified enum to put in.
     * @param place     Where the state should be changed.
     */
    public void setState(int[] place, PlayEnum shape)
    {
        BoardNode cell = this.getCell(game, place);
        cell.setState(shape);
        // Board.updateWinnersRecursive(game, place);
    }
    
    //THE FOLLOWING METHOD DOES NOT AGREE WITH THE FREE_MOVE OPTION
    
    /**
     * Gets a state of a cell safely, by navigating down the tree 
     * only where not null, and creating cells where they should exist.
     * @param coordinates   The coordinates of the cell to get.
     * @param base          The cell to start at, and to look down 
     * through coordinates at.
     * @return              The state of the cell to change.
     */
    public BoardNode getCell(BoardNode base, int[] coordinates)
    {
        BoardNode current = base;
        for (int i = 0; i < coordinates.length; i++)
        {
            BoardNode next = current.getChild(coordinates[i]);
            if (i == coordinates.length - 1 && next == null)
            {
                current.setChild(coordinates[i], new BoardNode());
            }
            else if (next == null)
            {
                current.setChild(coordinates[i], new BoardNode(PlayEnum.U));
            }
            current = current.getChild(coordinates[i]);
        }
        return current;
    }
    
    /**
     * Checks the win condition after a move recursively.
     * @param coords    The coordinates where the move needs to be checked.
     * @param current   The node to settle the win condition of.
     */
    private static PlayEnum updateWinnersRecursive(
        BoardNode current, int[] coords)
    {
        // recursive base case
        if (coords.length == 0 || current.getState() != PlayEnum.U)
        {
            return current.getState();
        }
        // truncating the first coordinate
        int[] newCoords = new int[coords.length - 1];
        for (int i = 1; i < coords.length; i++)
        {
            newCoords[i - 1] = coords[i];
        }
        // getting what the child's state is, where the move was
        PlayEnum movedState = Board.updateWinnersRecursive(
            current.getChild(coords[0]), newCoords);
        // nothing changed, since U is the default, so we return this state.
        if (movedState == PlayEnum.U)
        {
            return current.getState();
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
        // Testing subGrid is not null
        if (subGrid == null)
        {
            throw new IllegalArgumentException();
        }
        // Testing that the subGrid has children.
        try
        {
            subGrid.getChild(0);
        }
        catch (IllegalStateException ex)
        {
            System.out.println("No children to subGrid");
            return subGrid.getState();
        }
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
        // No one was a winner here
        return PlayEnum.U;
    }
    
    /**
     * Tests if the board is won by a player.
     * @return  The current win state of the entire board.
     */
    public PlayEnum isWonBy()
    {
        return game.getState();
    }
    
    /**
     * Clears the entire board, and erases the state of the game.
     */
    public void clear()
    {
        clearSubgrid(new int[0]);
    }
    
    /**
     * Clears a subgrid of the board, or the entire board.
     * @param location  The subgrid whose base node has this pair,
     * it will clear every subNode of this one.
     */
    public void clearSubgrid(int[] location)
    {
        BoardNode base = this.getCell(game, location);
        PlayEnum baseState = base.getState();
        base = new BoardNode();
        base.setState(baseState);
    }
}
