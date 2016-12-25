package tictactoe;

import java.util.Iterator;

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
 * @version 2016.12.24
 */
public class Board implements Cloneable, Iterable<PlayEnum>
{
    /*
     * Something I'm noticing about this entire thing is the parent nodes seem to store
     * U values. It would be easier to differentiate between leaf and parent nodes by making parents
     * only store null values. Since parents are only a structure to facilitate what is, in essense,
     * a 2D array implemented recursively, they should not store any value which might accidentally
     * be read or interpreted.
     */
    
    /**
     * The reference to the head node.
     */
    private BoardNode game; // kinda think this should be called root, game is very confusing
    
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
     * @return          Whether the move was successful. A move is not 
     * successful if the occupied space has a state other than U.
     */
    public boolean setState(int[] place, PlayEnum shape)
    {
        BoardNode cell = this.getCell(game, place);
        if (cell.getState() != PlayEnum.U)
        {
            return false;
        }
        cell.setState(shape);
        Board.updateWinnersRecursive(game, place);
        return true;
    }
    
    /**
     * Gets the state of a particular cell in the Board.
     * 
     * @param path The tree-space coordinates of a particular cell.
     * @return a PlayEnum.
     */
    public PlayEnum getState(int[] path)
    {
        if (path.length != order)
        {
            throw new IllegalArgumentException("Operation requires " + order + "-dimensional coordinates");
        }
        return getCell(game, path).getState();
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
    private BoardNode getCell(BoardNode base, int[] coordinates)
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
        // recursive base case - this eventually succeeds, because there
        // is a move at the location coords.
        if (current.getState() != PlayEnum.U)
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
        // Note the moved state is set in the recursive step.
        PlayEnum target = Board.isSubGridWon(current);
        current.setState(target);
        if (target != PlayEnum.U)
        {
            Board.clearSubgrid(current);
        }
        return target;
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
            return PlayEnum.U;
        }
        // Testing if the subGrid is itself won.
        if (subGrid.getState() != PlayEnum.U)
        {
            return subGrid.getState();
        }
        // Getting the states of all 9 cells
        PlayEnum[][] states = new PlayEnum[3][3];
        for (int i = 0; i < 9; i++)
        {
            if (subGrid.getChild(i) == null)
            {
                states [i / 3][i % 3] = PlayEnum.U;
            }
            else
            {
                states[i / 3][i % 3] = subGrid.getChild(i).getState();
            }
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
     * Note this can return PlayEnum.U if the board is not yet won.
     */
    public PlayEnum isWonBy()
    {
        return game.getState();
    }
    
    /**
     * Tests if the game is over, and no player can make a move.
     * @return  False if a player can still make a move, and true otherwise.
     */
    public boolean isGameOver()
    {
        return Board.isGameOverRecursive(game);
    }
    
    /**
     * Tests if the subGame is over, and no player can move.
     * @param   node    The node to consider finished or not.
     * @return  Whether this subGame cannot have progress made on it.
     */
    private static boolean isGameOverRecursive(BoardNode node)
    {
        if (node.getState() != PlayEnum.U)
        {
            return true;
        }
        else if (!node.hasChildren())
        {
            return false;
        }
        // Now we have an undetermined node with children
        // Test recursively if every child is finished
        for (int i = 0; i < 9; i++)
        {
            if (!Board.isGameOverRecursive(node.getChild(i)))
            {
                return false;
            }
        }
        // Since every node was solved, we return true.
        return true;
    }
    
    /**
     * Clears the entire board, and erases the state of the game.
     */
    public void clear()
    {
        Board.clearSubgrid(game); // TRIGGERED
    }
    
    // DIAMETRICALLY OPPOSED
    /**
     * Clears a subgrid of the board, or the entire board.
     * @param location  The subgrid whose base node has this pair,
     * it will clear every subNode of this one.
     * 
     * This method might not be necessary.
     */
    public void clearSubgrid(int[] location)
    {
        Board.clearSubgrid(this.getCell(game, location));
    }
    
    // DIAMETRICALLY OPPOSED TO THIS OPERATION
    /**
     * Clears a subgrid of the board.
     * @param node The node to clear children of.
     */
    private static void clearSubgrid(BoardNode node)
    {
        PlayEnum baseState = node.getState();
        node = new BoardNode();
        node.setState(baseState);
    }

    @Override
    public TreeIterator iterator()
    {
        return new TreeIterator();
    }
    
    private class TreeIterator implements Iterator<PlayEnum>
    {
        private int index;
        
        public TreeIterator()
        {
            index = 0;
        }
        
        @Override
        public boolean hasNext()
        {
            return index < Math.pow(9, order);
        }

        @Override
        public PlayEnum next()
        {
            index++;
            System.out.println(index - 1);
            return getState(map(index - 1, order));
        }
        
        private int[] map(int index, int order)
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
    }
}
