package tictactoe;

import java.util.Iterator;
import tictactoe.BoardNode.NodeIterator;

/**
 * This class represents the area of tic tac toe cells that players play on.
 * Stored as a full tree where each parent has 9 children, each separate Grid in
 * the board stores its own information about which spaces have been filled, and
 * whether it is won by someone or not. Each leaf is an object with an enum
 * containing who has played in it, or that no one played in it.
 * 
 * Actually, let me start implementing and then figure out what I am saying...
 * 
 * @author William McDermott
 * @version 2016.12.24
 * 
 * @author Wade Foster
 * @version 2016.12.25
 */
public class Board implements Cloneable, Iterable<PlayEnum>
{
    /**
     * The reference to the head node.
     */
    private BoardNode root;

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
     * 
     * @param order The order to make the board.
     */
    public Board(int order)
    {
        root = new BoardNode(PlayEnum.U);
        this.order = order;
    }

    /**
     * Gets the order of this grid.
     * 
     * @return The depth of subgrids this board goes to.
     */
    public int getOrder()
    {
        return order;
    }

    /**
     * Gets if the free move is true or not.
     * 
     * @return Whether or not this board considers free moves. The first
     *         implementation will not consider a lack of free moves.
     */
    public boolean getFreeMove()
    {
        return FREE_MOVE;
    }

    /**
     * Sets the state for a cell or subgrid in the tree.
     * 
     * @param shape The specified enum to put in.
     * @param place Where the state should be changed.
     * @return Whether the move was successful. A move is not successful if the
     *         occupied space has a state other than U.
     */
    /*
     * Edits as of 2016.12.25: This method should only apply to leaf nodes, as
     * they are the only nodes with a notion of state WHICH IS ACCESSIBLE BY
     * OTHER CLASSES. All other nodes are structural, and have no public notion
     * of state. Therefore, the operation should only accept Cartesian
     * coordinates, since external classes have no business using tree space
     * coordinates.
     * 
     * Additionally, since Board has no business in restricting whether an edit
     * to it is valid, leaf nodes can be changed from anything to anything,
     * excluding null.
     */
    public boolean setState(int x, int y, PlayEnum shape)
    {
        if (shape == null)
        {
            throw new IllegalArgumentException("Shape must not be null");
        }
        int max = (int) (Math.pow(3, order) - 1);
        if (x > max || y > max || x < 0 || y < 0)
        {
            throw new IllegalArgumentException(
                    "Coordinates out of legal bounds");
        }

        int[] treePath = Converter.toTreeCoordinates(new int[] {x, y}, order);
        BoardNode cell = getCell(root, treePath);
        cell.setState(shape);
        return true;
    }

    /**
     * Gets the state of a particular cell in the Board.
     * 
     * @param path The Cartesian coordinates of a particular cell.
     * @return a PlayEnum.
     */
    /*
     * Changed the parameter to Cartesian coordinates, since this is a public
     * method and external classes should only deal in Cartesian coordinates,
     * not tree space coordinates.
     */
    public PlayEnum getState(int x, int y)
    {
        int max = (int) (Math.pow(3, order) - 1);
        if (x > max || x < 0 || y > max || y < 0)
        {
            throw new IllegalArgumentException(
                    "Coordinates must be between 0 and " + max);
        }
        int[] treePath = Converter.toTreeCoordinates(new int[] {x, y}, order);
        return getCell(root, treePath).getState();
    }

    // THE FOLLOWING METHOD DOES NOT AGREE WITH THE FREE_MOVE OPTION

    /**
     * Gets a state of a cell safely, by navigating down the tree only where not
     * null, and creating cells where they should exist.
     * 
     * @param treePath The tree space coordinates of the cell to get.
     * @param base The cell to start at, and to look down through coordinates
     *        at.
     * @return The state of the cell to change.
     */
    private BoardNode getCell(BoardNode root, int[] treePath)
    {
        BoardNode current = root;
        for (int i = 0; i < treePath.length; i++)
        {
            BoardNode next = current.getChild(treePath[i]);
            if (i == treePath.length - 1 && next == null)
            {
                current.setChild(treePath[i], new BoardNode());
            }
            else if (next == null)
            {
                current.setChild(treePath[i], new BoardNode(PlayEnum.U));
            }
            current = current.getChild(treePath[i]);
        }
        return current;
    }

    /**
     * Checks the win condition after a move recursively.
     * 
     * @param coords The coordinates where the move needs to be checked.
     * @param current The node to settle the win condition of.
     */
    /*
     * This method seems like a massive violation of encapsulation and quite
     * possibly a war crime. Such a checker method belongs in TicTacGrow, but
     * these recursive methods would be rather difficult to implement without
     * direct access to the node structure. More to the point, without access to
     * the node structure, the Board class is a glorified 2D array without the
     * benefits of recursion.
     */
    private static PlayEnum updateWinnersRecursive(BoardNode current,
            int[] coords)
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
        PlayEnum movedState = Board
                .updateWinnersRecursive(current.getChild(coords[0]), newCoords);
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
     * 
     * @return Whether this 9x9 grid, disregarding depth, is won or not.
     */
    /*
     * Another method which I can't really discern and may have weapons of mass
     * destruction.
     */
    private static PlayEnum isSubGridWon(BoardNode subGrid)
    {
        // Testing subGrid is not null
        if (subGrid == null)
        {
            return PlayEnum.U;
        }
        // Testing if the subGrid is itself won
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
                states[i / 3][i % 3] = PlayEnum.U;
            }
            else
            {
                states[i / 3][i % 3] = subGrid.getChild(i).getState();
            }
        }
        // Testing the states
        for (PlayEnum p : new PlayEnum[] {PlayEnum.X, PlayEnum.O})
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
     * 
     * @return The current win state of the entire board. Note this can return
     *         PlayEnum.U if the board is not yet won.
     */
    public PlayEnum getWinner()
    {
        return getWinner(root);
    }

    /**
     * A recursive operation which returns the winner of any arbitrary subgame,
     * including leaf nodes.
     * 
     * @param root The root node of the subgame, which can be a leaf node.
     * @return The winner of that subgame.
     */
    private PlayEnum getWinner(BoardNode root)
    {
        if (!root.hasChildren())
        {
            return root.getState();
        }

        return PlayEnum.U;
    }

    /**
     * Tests if the game is over, and no player can make a move.
     * 
     * @return False if a player can still make a move, and true otherwise.
     */
    public boolean isGameOver()
    {
        /*
         * return Board.isGameOverRecursive(root);
         *
         * I think this might be sufficient, considering that structural nodes
         * store the winning symbol of their subgame, and the root node's
         * subgame is the entire game, and if the root node is storing anything
         * other than Undetermined, someone has won the game, and so the game
         * must be over. QED.
         */
        /** I like the QED. */
        return getWinner() != PlayEnum.U;
    }

    /**
     * Tests if the subGame is over, and no player can move.
     * 
     * @param node The node to consider finished or not.
     * @return Whether this subGame cannot have progress made on it.
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
        Board.clearSubgrid(root); // TRIGGERED
    }

    // DIAMETRICALLY OPPOSED
    /**
     * Clears a subgrid of the board, or the entire board.
     * 
     * @param location The subgrid whose base node has this pair, it will clear
     *        every subNode of this one.
     * 
     *        This method might not be necessary.
     */
    public void clearSubgrid(int[] location)
    {
        Board.clearSubgrid(this.getCell(root, location));
    }

    // DIAMETRICALLY OPPOSED TO THIS OPERATION
    /**
     * Clears a subgrid of the board.
     * 
     * @param node The node to clear of children.
     */
    private static void clearSubgrid(BoardNode node)
    {
        PlayEnum baseState = node.getState();
        node = new BoardNode();
        node.setState(baseState);
    }

    /**
     * Gets a new Iterator for Board.
     * 
     * @return A TreeIterator.
     */
    @Override
    public TreeIterator iterator()
    {
        return new TreeIterator();
    }

    /**
     * A TreeIterator traverses every leaf node in a Board object, which is a
     * nonatree structure which represents a 2D array. As such, the TreeIterator
     * traverses a fundamentally two-dimensional object one-dimensionally.
     * 
     * @author Wade Foster
     * @version 2016.12.25
     */
    private class TreeIterator implements Iterator<PlayEnum>
    {
        /**
         * Stores the location of the TreeIterator along the traversal in a
         * one-dimensional coordinate.
         */
        private int index;

        /**
         * Constructs a new TreeIterator.
         */
        public TreeIterator()
        {
            index = 0;
        }

        /**
         * Checks whether the traversal is finished.
         * 
         * @return Whether there is another element in the iteration.
         */
        @Override
        public boolean hasNext()
        {
            return index < Math.pow(9, order);
        }

        /**
         * Gets the next element in the traversal.
         * 
         * @return A PlayEnum.
         */
        @Override
        public PlayEnum next()
        {
            int[] treePath = Converter.expandToTreeCoordinates(index, order);
            index++;
            return getCell(root, treePath).getState();
        }
    }
    
    /**
     * Represents the nodes in the tree that hold the state of who won them.
     * 
     * @author William McDermott
     * @version 2016.12.24
     * 
     * @author Wade Foster
     * @version 2016.12.25
     */
    public class BoardNode implements Iterable<BoardNode>
    {
        /**
         * Represents the 9 grids under this one.
         */
        private final BoardNode[] subGrids;
        
        /**
         * Who controls this board.
         */
        private PlayEnum state;
        
        /**
         * Creates a new BoardNode with no children that is unoccupied.
         */
        public BoardNode()
        {
            subGrids = null;
            state = PlayEnum.U;
        }
        
        /**
         * Creates a new BoardNode with empty children and the specified state.
         * @param state     The state of who has won this grid.
         */
        public BoardNode(PlayEnum state)
        {
            subGrids = new BoardNode[9];
            this.state = state;
        }
        
        /**
         * Gets the state of this board.
         * @return          The state of who won this grid.
         */
        public PlayEnum getState()
        {
            return state;
        }
        
        /**
         * Sets the state of this board.
         * @param state     The new state for the board.
         */
        public void setState(PlayEnum state)
        {
            this.state = state;
        }
        
        /**
         * Tests if this node has children, that is, it is a cell.
         */
        public boolean hasChildren()
        {
            return subGrids != null;
        }
        
        /**
         * Gets the specified child of this BoardNode.
         * @param index     The index of this child.
         * @return  
         */
        public BoardNode getChild(int index)
        {
            if (subGrids == null)
            {
                throw new IllegalStateException("Non-Existant Grid Access!");
            }
            return subGrids[index];
        }
        
        /**
         * Sets the specified child of this BoardNode.
         * @param index     The index where the child goes.
         * @param child     The BoardNode child to set as a child.
         */
        public void setChild(int index, BoardNode child)
        {
            if (subGrids == null)
            {
                throw new IllegalStateException("Non-Existant Grid Access!");
            }
            subGrids[index] = child;
        }

        @Override
        public Iterator<BoardNode> iterator()
        {
            return new NodeIterator();
        }
        
        private class NodeIterator implements Iterator<BoardNode>
        {
            private int index;
            
            public NodeIterator()
            {
                index = 0;
            }

            @Override
            public boolean hasNext()
            {
                return index < 9;
            }

            @Override
            public BoardNode next()
            {
                BoardNode node = subGrids[index];
                index++;
                return node;
            }
            
        }
    }
}
