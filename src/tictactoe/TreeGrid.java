package tictactoe;

import java.util.Iterator;

/**
 * The equivalent of the pen and paper, this records moves in the game.
 * This class is a replacement for the Board class, in conjunction with
 * the TicTacGrow class. It holds the tree with 9 children for each node,
 * essential to the function of the game, and methods for getting and 
 * setting PLayEnum objects in the tree. Finally, it also has the internal
 * BoardNode class and TreeIterator class, so that objects can be
 * manipulated in the tree.
 * 
 * @author William McDermott
 * @version 2016.12.26
 */
public class TreeGrid implements Iterable<PlayEnum>
{
    /**
     * The topNode of the simulation.
     */
    private BoardNode root;
    
    /**
     * The order of boards in this object, also the height.
     */
    private int order;

    /**
     * Whether or not won subgrids restrict movement.
     * True if a subGrid prevents the opponent from moving.
     */
    private static final boolean FREE_MOVE = true;
    
    public TreeGrid()
    {
        this(1);
    }
    
    public TreeGrid(int order)
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
     * Gets if the free move applies to this game or not.
     * 
     * @return Whether or not this board considers free moves. The first
     *         implementation will not consider a lack of free moves.
     */
    public boolean getFreeMove()
    {
        return FREE_MOVE;
    }
    
    /**
     * Creates or follows a path to a cell, and returns it to the class.
     * 
     * @param path  The tree coordinate of where the cell is
     * @return      The cell found at that coordinate.
     */
    private BoardNode getCell(int[] path)
    {
        BoardNode current = root;
        for (int i = 0; i < path.length; i++)
        {
            if (!current.hasChildren())
            {
                break;
            }
            BoardNode next = current.getChild(path[i]);
            if (i == path.length - 1 && next == null)
            {
                next = new BoardNode();
            }
            else if (next == null)
            {
                next = new BoardNode(PlayEnum.U);
            }
            current.setChild(path[i], next);
            current = next;
        }
        return current;
    }
    
    /**
     * Sets the path of a cell to a certain state.
     * 
     * @param path  The path to the specified cell.
     * @return      The state of the cell that is located.
     */
    public PlayEnum getState(int[] path)
    {
        return this.getCell(path).getState();
    }
    
    /**
     * Sets the state of a cell at the specified space.
     * 
     * @param path      The path to the specified cell.
     * @param shape     The shape to insert in the space.
     */
    public void setState(int[] path, PlayEnum shape)
    {
        this.getCell(path).setState(shape);
    }
    
    /**
     * Gets the states of a subGrid at the specified location.
     * 
     * @param place     The location of the root node of the grid.
     * @return          An array of children under the root.
     */
    public PlayEnum[] getSubGrid(int[] place)
    {
        PlayEnum[] target = new PlayEnum[9];
        /*
        System.out.println("getSubGrid Debug!");
        for (int i = 0; i < place.length; i++)
        {
            System.out.print(place[i] + ", ");
        }
        System.out.println();
        System.out.println(this.getCell(place).getState());
        */
        Iterator<BoardNode> iter = this.getCell(place).iterator();
        if (iter == null)
        {
            return null;
        }
        for (int i = 0; iter.hasNext(); i++)
        {
            BoardNode next = iter.next();
            if (next == null)
            {
                target[i] = null;
            }
            else
            {
                target[i] = next.getState();
            }
        }
        return target;
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
            return getCell(treePath).getState();
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
            if (subGrids == null)
            {
                return null;
            }
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
