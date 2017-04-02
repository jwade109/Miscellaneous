package tree;

import java.util.ArrayList;

public class SmartAI implements Player
{
    private String name;
    
    public SmartAI(String name)
    {
        this.name = name;
    }
    
    /**
     * Returns a move given a TicTacGrow game.
     * 
     * @return A smart move, hopefully.
     */
    public int[] makeMove(TicTacGrow g)
    {
        ArrayList<int[]> moves = g.getLegalMoves();
        int sector = g.getLegalSector();
        System.out.println("[" + name + "] >> Legal moves: ");
        print(moves);

        ArrayList<int[]> winning = new ArrayList<int[]>();
        for (int[] move : moves)
        {
            TicTacGrow game = g.clone();
            Play player = g.getNextPlayer();
            game.set(player, move);
            if (game.getWinner() == player)
            {
                winning.add(move);
            }
        }

        ArrayList<int[]> globalCenter = new ArrayList<int[]>();
        for (int[] move : moves)
        {
            boolean centered = true;
            for (int a : move)
            {
                if (a != 4)
                {
                    centered = false;
                }
            }
            if (centered)
            {
                globalCenter.add(move);
            }
        }

        ArrayList<int[]> inSector = new ArrayList<int[]>();
        for (int[] move : moves)
        {
            if (move[move.length - 1] == sector)
            {
                inSector.add(move);
            }
        }

        ArrayList<int[]> centers = new ArrayList<int[]>();
        for (int[] move : moves)
        {
            if (move[move.length - 1] == 4)
            {
                centers.add(move);
            }
        }

        ArrayList<int[]> corners = new ArrayList<int[]>();
        for (int[] move : moves)
        {
            if (move[move.length - 1] % 2 == 0 && move[move.length - 1] != 4)
            {
                corners.add(move);
            }
        }
        
        ArrayList<int[]> safeCorners = new ArrayList<int[]>();
        for (int[] corner : corners)
        {
            TicTacGrow test = g.clone();
            test.set(g.getNextPlayer(), corner);
            if (test.getLegalSector() > -1)
            {
                safeCorners.add(corner);
            }
        }

        ArrayList<int[]> sides = new ArrayList<int[]>();
        for (int[] move : moves)
        {
            if (move[move.length - 1] % 2 == 1)
            {
                sides.add(move);
            }
        }
        
        ArrayList<int[]> safeSides = new ArrayList<int[]>();
        for (int[] side : sides)
        {
            TicTacGrow test = g.clone();
            test.set(g.getNextPlayer(), side);
            if (test.getLegalSector() > -1)
            {
                safeSides.add(side);
            }
        }

        System.out.println("winning moves:");
        print(winning);
        System.out.println("global center:");
        print(globalCenter);
        System.out.println("in sector:");
        print(inSector);
        System.out.println("other centers:");
        print(centers);
        System.out.println("corners:");
        print(corners);
        System.out.println("safe corners:");
        print(safeCorners);
        System.out.println("sides:");
        print(sides);
        System.out.println("safe sides:");
        print(safeSides);
        
        if (!winning.isEmpty())
        {
            return getRandom(winning);
        }
        if (!globalCenter.isEmpty())
        {
            return getRandom(globalCenter);
        }
        if (!inSector.isEmpty())
        {
            return getRandom(inSector);
        }
        if (!centers.isEmpty())
        {
            return getRandom(centers);
        }
        if (!corners.isEmpty())
        {
            return getRandom(corners);
        }
        if (!sides.isEmpty())
        {
            return getRandom(sides);
        }

        return moves.get(0);
    }

    /**
     * Picks a random int[] from the list.
     * 
     * @param The list of int[].
     * @return A random int[].
     */
    private int[] getRandom(ArrayList<int[]> list)
    {
        int index = (int) (Math.random() * list.size());
        return list.get(index);
    }

    private void print(ArrayList<int[]> list)
    {
        for (int i = 0; i < list.size(); i++)
        {
            System.out.print("(");
            int[] m = list.get(i);
            for (int j = 0; j < m.length; j++)
            {
                System.out.print(m[j]);
                if (j < m.length - 1)
                {
                    System.out.print(", ");
                }
            }
            System.out.print(")");
            if (i < list.size() - 1)
            {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    /**
     * Gets the description of this SmartAI.
     * 
     * @return A String.
     */
    public String description()
    {
        return "The smartest computer player you have ever seen.";
    }

    /**
     * Gets the name of this SmartAI.
     * 
     * @return A String.
     */
    public String name()
    {
        return name;
    }

}
