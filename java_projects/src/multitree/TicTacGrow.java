package tree;

import java.util.ArrayList;

/**
 * The TicTacGrow class is an object which implements a recursive interpretation
 * of Ultimate Tic Tac Toe. It permits the players to make only legal moves, and
 * keeps track of which player is allowed to move. As such, this object is
 * mutable in only legal ways, and no other class need enforce any rules to
 * prevent illegal moves.
 * 
 * @author Wade Foster
 * @version 2016.12.28
 */
public class TicTacGrow implements Cloneable
{
    private Multitree<Play> m;
    private int boardWidth;
    private int legalSector;
    private Play nextPlayer;
    private int moveCount;

    /**
     * Gets a new TicTacGrow game. An order of 1 indicates a standard tic tac
     * toe game; an order of 2 indicates ultimate tic tac toe. Increasing order
     * TicTacGrow games generate recursively increasingly complex games.
     * 
     * @param order The order of the game.
     */
    public TicTacGrow(int order)
    {
        order = Math.max(1, order);
        m = new Multitree<Play>(order, 9);
        m.fill(Play.U);
        boardWidth = (int) Math.pow(3, order);
        legalSector = -1;
        moveCount = 0;
        nextPlayer = Play.X;
    }

    /**
     * Gets the size of the TicTacGrow board, that is, the number of cells
     * across it.
     * 
     * @return The size of the board.
     */
    public int size()
    {
        return boardWidth;
    }

    /**
     * Gets the order of this TicTacGrow game.
     * 
     * @return The order.
     */
    public int order()
    {
        return m.depth();
    }

    /**
     * Gets which sector of this TicTacGrow game is currently legal, which is a
     * function of the last move played on this board. In some cases, the legal
     * sector might be -1, which indicates that the entire board is legal.
     * 
     * @return Which sector of this game is currently legal.
     */
    public int getLegalSector()
    {
        return legalSector;
    }

    /**
     * Gets the number of moves that have been played.
     * 
     * @return The move count.
     */
    public int moveCount()
    {
        return moveCount;
    }

    /**
     * Get the next player to go.
     * 
     * @return Whoever's turn it is.
     */
    public Play getNextPlayer()
    {
        return nextPlayer;
    }

    /**
     * Checks if the game has been won by anyone yet.
     * 
     * @return Whether the game is over.
     */
    public boolean isOver()
    {
        update();

        if (getLegalMoves().size() == 0 ^ m.root().getData() != Play.U)
        {
            System.out.println(getLegalMoves().size());
            System.out.println(m.root().getData());
            throw new IllegalStateException("Root does not reflect game!");
        }

        return getLegalMoves().size() == 0;
    }

    /**
     * Gets the current winner of the game.
     * 
     * @return The current winning player.
     */
    public Play getWinner()
    {
        update();
        return m.root().getData();
    }

    /**
     * Gets the value stored in the cell at a given Cartesian coordinate.
     * 
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return The value stored at that location.
     */
    public Play get(int x, int y)
    {
        validate(x);
        validate(y);
        int[] path = toTree(new int[] {x, y}, m.depth());
        return get(path);
    }

    /**
     * Sets the specified cell to a certain move.
     * 
     * @param move The move to set.
     * @param x The x coordinate.
     * @param y The y coordinate.
     * @return Whether the operation is successful.
     */
    public boolean set(Play move, int x, int y)
    {
        validate(x);
        validate(y);

        int[] path = toTree(new int[] {x, y}, m.depth());
        return set(move, path);
    }

    /**
     * Gets the value stored in the cell, or subgame, at a given path.
     * 
     * @param path The path which describes the cell.
     * @return The value at that location.
     */
    public Play get(int[] path)
    {
        validate(path, false);
        return m.findNode(path).getData();
    }

    /**
     * Sets the specified cell to a certain move.
     * 
     * @param move The move to set.
     * @param path The path to the desired node.
     * @return Whether the operation is successful.
     */
    public boolean set(Play move, int[] path)
    {
        if (isOver())
        {
            return false;
        }
        validate(path, true);
        validate(move);
        if (move == Play.U)
        {
            throw new IllegalArgumentException(
                    "Cannot play an undetermined move");
        }
        Node<Play> cell = m.findNode(path);
        if (!isAvailable(cell))
        {
            return false;
        }
        cell.setData(move);
        if (size() > 3)
        {
            legalSector = path[path.length - 1];
        }
        update();
        moveCount++;
        return true;
    }

    /**
     * Gets the path of every cell which is available to be played in.
     * 
     * @return The paths of all legal moves.
     */
    public ArrayList<int[]> getLegalMoves()
    {
        update();
        ArrayList<int[]> coords = new ArrayList<int[]>();
        for (Node<Play> node : getLevel(m.depth()))
        {
            if (isAvailable(node))
            {
                coords.add(m.findPath(node));
            }
        }
        return coords;
    }

    /**
     * Gets a String representation of this TicTacGrow.
     * 
     * @return A String.
     */
    public String toString()
    {
        update();
        StringBuilder out = new StringBuilder();
        for (int d = 1; d <= m.depth(); d++)
        {
            if (m.depth() > 1)
            {
                out.append("~ Level " + d + ":\n");
            }
            out.append(toString(d));
        }
        out.append("\n");
        return out.toString();
    }

    /**
     * Gets a String which reflects the inner structure of the TicTacGrow game.
     * 
     * @return A String for debugging.
     */
    public String toDebugString()
    {
        return m.toString();
    }

    /**
     * Gets a clone of this TicTacGrow board.
     * 
     * @return A clone of this TicTacGrow.
     */
    public TicTacGrow clone()
    {
        TicTacGrow clone = new TicTacGrow(m.clone(), legalSector, moveCount, nextPlayer);
        clone.update();        
        return clone;
    }

    /**
     * Constructs a new TicTacGrow game given an underlying Multitree.
     * 
     * @param m The Multitree to use.
     * @param legalSector The current legalSector.
     */
    private TicTacGrow(Multitree<Play> m, int legalSector, int moveCount, Play nextPlayer)
    {
        this(m.depth());
        this.m = m;
        this.legalSector = legalSector;
        this.moveCount = moveCount;
        this.nextPlayer = nextPlayer;
    }

    /**
     * Updates the state of every Node in the game to reflect the state of their
     * subgames, and updates the value of legalSector. Iterates through Nodes
     * from the bottom up in order to ensure all of them are updated correctly
     * with just one pass.
     */
    private void update()
    {
        for (int i = m.depth(); i >= 0; i--)
        {
            for (Node<Play> node : getLevel(i))
            {
                node.setData(Converter.winner(node));
            }
        }
        if (legalSector > -1)
        {
            if (m.root().getChild(legalSector).getData() != Play.U)
            {
                legalSector = -1;
            }
        }
    }

    /**
     * Gets the String representation of a specific level of the game.
     * 
     * @param level The level to represent.
     * @return A String representation of the game.
     */
    public String toString(int level)
    {
        StringBuilder out = new StringBuilder();
        ArrayList<Node<Play>> nodes = getLevel(level);
        int width = (int) Math.pow(3, level);
        for (int y = 0; y < width; y++)
        {
            int lineWidth = 0;
            for (int x = 0; x < width; x++)
            {
                int[] car = new int[] {x, y};
                int[] path = Converter.toTreeCoordinates(car, level);
                int index = Converter.compressToTreeIndex(path);
                Play data = nodes.get(index).getData();
                Node<Play> temp = nodes.get(index);
                if (data == Play.U)
                {
                    if (isAvailable(temp))
                    {
                        out.append("_");
                    }
                    else
                    {
                        out.append("•");
                    }
                }
                else
                {
                    out.append(data);
                }
                out.append(" ");
                lineWidth += 2;
                if (x % 3 == 2 && x < width - 1)
                {
                    out.append("| ");
                    lineWidth += 2;
                }
            }
            if (y % 3 == 2 && y < width - 1)
            {
                out.append("\n");
                for (int i = 0; i < lineWidth - 1; i++)
                {
                    out.append("-");
                }
            }
            if (level < m.depth() || y < width - 1)
            {
                out.append("\n");
            }
        }
        return out.toString();
    }

    /**
     * Gets every node from a specific level of the game.
     * 
     * @param level The desired level. 0 indicates the root level.
     * @return An ArrayList of Nodes.
     */
    private ArrayList<Node<Play>> getLevel(int level)
    {
        ArrayList<Node<Play>> nodes = new ArrayList<Node<Play>>();
        nodes.add(m.root());
        for (int i = 0; i < level; i++)
        {
            ArrayList<Node<Play>> gen = new ArrayList<Node<Play>>();
            for (Node<Play> parent : nodes)
            {
                gen.addAll(parent.getChildren());
            }
            nodes = gen;
        }
        return nodes;
    }

    /**
     * Converts an Cartesian ordered coordinate pair (x, y) into an address in
     * recursive tree space.
     * 
     * @param cartesian The Cartesian coordinates, (x, y).
     * @return A tree-space vector of a defined dimension.
     */
    private int[] toTree(int[] cartesian, int dim)
    {
        if (cartesian.length != 2)
        {
            throw new IllegalArgumentException(
                    "Cartesian coordinates must be 2 dimensional");
        }
        int[] target = new int[dim];
        int x = cartesian[0];
        int y = cartesian[1];
        for (int i = dim - 1; i >= 0; i--)
        {
            target[i] = x % 3 + 3 * (y % 3);
            x = x / 3;
            y = y / 3;
        }
        return target;
    }

    /**
     * Throws an exception if a cartesian coordinate is outside of legal bounds.
     * 
     * @param coord The coordinate to validate.
     */
    private void validate(int coord)
    {
        if (coord > boardWidth - 1 || coord < 0)
        {
            throw new IndexOutOfBoundsException("Coordinate (" + coord
                    + ") must be between 0 and " + (boardWidth - 1));
        }
    }

    /**
     * Throws an exception if a tree path is outside of legal bounds.
     * 
     * @param path The path to validate.
     */
    private void validate(int[] path, boolean isCell)
    {
        boolean outOfBounds = false;
        StringBuilder error = new StringBuilder("(");
        for (int i = 0; i < path.length; i++)
        {
            if (path[i] > 8 || path[i] < 0)
            {
                outOfBounds = true;
            }
            error.append(path[i]);
            if (i < path.length - 1)
            {
                error.append(", ");
            }
        }
        error.append(")");
        if (outOfBounds)
        {
            throw new IndexOutOfBoundsException(
                    "Path must be between 0 and 8: " + error.toString());
        }
        if (isCell && path.length != m.depth())
        {
            throw new IllegalArgumentException(
                    "Path does not specify a cell: " + error.toString());
        }
    }

    /**
     * Throws an exception if a player makes a move out of turn.
     * 
     * @param move The move that was made.
     */
    private void validate(Play move)
    {
        if (move != nextPlayer)
        {
            throw new IllegalArgumentException("It's " + nextPlayer
                    + "'s turn - not your turn, " + move + "!");
        }
        if (move == Play.X)
        {
            nextPlayer = Play.O;
        }
        else if (move == Play.O)
        {
            nextPlayer = Play.X;
        }
    }

    /**
     * Checks if all parents of this node are not locked, and the cell does not
     * already contain a player's move.
     * 
     * @return True if this cell is available to play on.
     */
    private boolean isAvailable(Node<Play> cell)
    {
        if (cell.hasChildren())
        {
            return false;
        }
        if (cell.getData() != Play.U)
        {
            return false;
        }
        if (m.findPath(cell)[0] != legalSector && legalSector > -1)
        {
            return false;
        }
        while (cell != m.root())
        {
            cell = cell.getParent();
            if (cell.getData() != Play.U)
            {
                return false;
            }
        }
        return true;
    }
}
