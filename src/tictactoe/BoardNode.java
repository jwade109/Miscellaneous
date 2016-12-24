package tictactoe;

/*
 * I am of the opinion that this should be a private class inside of Board
 */
/**
 * Represents the nodes in the tree that hold the state of who won them.
 * 
 * @author William McDermott
 * @version 2016.12.20
 */
public class BoardNode
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
}
