package tictactoe.game;

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
 * @version 2017.01.04
 */
public class TreeGrid implements Iterable<PlayEnum>, Cloneable
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
    private BoardNode getCellSafe(int[] path)
    {
        BoardNode current = root;
        for (int i = 0; i < path.length; i++)
        {
            if (!current.hasChildren())
            {
                break;
            }
            BoardNode next = current.getChild(path[i]);
            if (i == order - 1 && next == null)
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
     * Gets the state of a cell in the tree and returns it.
     * 
     * @param path  The path to the specified cell.
     * @return      Null if not initialized, or the state of the cell.
     */
    public BoardNode getCell(int[] path)
    {
        BoardNode current = root;
        for (int i = 0; i < path.length; i++)
        {
            if (current == null || !current.hasChildren())
            {
                break;
            }
            current = current.getChild(path[i]);
        }
        return current;
    }
    
    /**
     * Gets a cell to a certain state, with the specified path.
     * This method cannot return null.
     * 
     * @param path  The path to the specified cell.
     * @return      The state of the cell that is located.
     */
    public PlayEnum getStateSafe(int[] path)
    {
        return this.getCellSafe(path).getState();
    }
    
    /**
     * Gets the state of the cell, returning null if the tree structure is empty.
     * 
     * @param path  The path to the specified cell.
     * @return      null if the tree is not initialized there,
     * the PlayEnum state of the cell otherwise.
     */
    public PlayEnum getState(int[] path)
    {
        BoardNode node = this.getCell(path);
        if (node == null)
        {
            return null;
        }
        return node.getState();
    }
    
    /**
     * Sets the state of a cell at the specified space.
     * 
     * @param path      The path to the specified cell.
     * @param shape     The shape to insert in the space.
     * @return          True if the state was set, false if it was not initialized.
     */
    public boolean setState(int[] path, PlayEnum shape)
    {
        BoardNode node = this.getCellSafe(path);
        if (node != null)
        {
            node.setState(shape);
            return true;
        }
        return false;
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
        BoardNode parent = this.getCell(place);
        if (parent == null)
        {
            return null;
        }
        for (int i = 0; i < 9; i++)
        {
            BoardNode next = parent.getChild(i);
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
     * Clears a subgrid of this board, rendering it null after it is won.
     * 
     * @param place     The place to clear of its moves.
     */
    public void clearSubgrid(int[] place)
    {
        BoardNode cell = this.getCell(place);
        if (cell != null)
        {
            PlayEnum state = cell.getState();
            cell = new BoardNode();
            cell.setState(state);
        }
    }
    
    /**
     * Writes this object to a human readable string.
     * @return  A string representation of this object.
     */
    @Override
    public String toString()
    {
        StringBuilder str = new StringBuilder("Board:\n");
        for (int i = 1; i < order + 1; i++)
        {
            str.append(this.layerToString(i));
        }
        return str.toString();
    }
    
    /**
     * Returns a layer of the board in string format.
     * @return one layer of the board.
     */
    public String layerToString(int layerIndex)
    {
        int cartesianMax = (int) (Math.pow(3, layerIndex) + 0.001);
        StringBuilder str = new StringBuilder("Layer: ");
        str.append(layerIndex);
        str.append("\n");
        for (int i = 0; i < (int) (Math.pow(9, layerIndex) + 0.001); i++)
        {
            int[] cartesian = new int[2];
            cartesian[0] = i % cartesianMax;
            cartesian[1] = i / cartesianMax;
            int[] place = Converter.toTreeCoordinates(cartesian, layerIndex);
            PlayEnum state = this.getState(place);
            if (state == null || state == PlayEnum.U)
            {
                str.append(" ");
            }
            else
            {
                str.append(state);
            }
            // vertical separating bars, more at each major divide
            for (int power = 3;
                i % power == power - 1 
                    && power < cartesianMax + 1;
                power *= 3)
            {
                str.append("|");
            }
            if (i % 3 != 2)
            {
                str.append(" ");
            }
            // horizontal separating bars, more at each major divide
            for (int power = cartesianMax; i % power == power - 1; power *= 3)
            {
                if (power != cartesianMax)
                {
                    for (int j = 0; j < (cartesianMax / 3); j++)
                    {
                        str.append("-----");
                    }
                    int secondPower = 1;
                    for (int j = 0; j < layerIndex - 1; j++)
                    {
                        for (int k = 0; k < secondPower; k++)
                        {
                            str.append("-");
                        }
                        secondPower *= 3;
                    }
                    // The last number of hyphens, minus the order.
                    for (int k = 0; k < secondPower - layerIndex; k++)
                    {
                        str.append("-");
                    }
                    for (int j = 0; j < layerIndex; j++)
                    {
                        str.append("|");
                    }
                }
                str.append("\n");
            }
        }
        return str.toString();
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
            // this is not really an iterator because of the expansion.
            // it would be better if we appended and removed indices from an array,
            // and recursively went around or something
            int[] treePath = Converter.expandToTreeCoordinates(index, order);
            index++;
            // This next line also makes it not much of an iterator
            BoardNode node = getCell(treePath);
            if (node == null)
            {
                return null;
            }
            return node.getState();
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
    public class BoardNode // implements Iterable<BoardNode>
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
                return null;
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

        /*
        @Override
        public Iterator<BoardNode> iterator()
        {
            if (subGrids == null)
            {
                return null;
            }
            return new NodeIterator();
        }
        
        /*
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
        */
    }
}
