package multitree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class HumanPlayer implements Player
{
    String name;
    Scanner systemInScan;

    public HumanPlayer(String name)
    {
        this.name = name;
        systemInScan = new Scanner(System.in);
    }

    /**
     * Gets a legal move from the human player given a TicTacGrow game.
     * 
     * @return The legal move, in path coordinates.
     */
    public int[] makeMove(TicTacGrow g)
    {
        ArrayList<int[]> legalMoves = g.getLegalMoves();

        System.out.println("[" + name + "] Please enter " + g.order()
                + " integer(s), all between 0 and 8, separated by spaces.");
        System.out.print("[" + name + "] >>> ");
        int[] path = null;

        boolean valid = false;
        while (!valid)
        {
            path = parse(systemInScan.nextLine(), g.order(), legalMoves);
            if (path == null)
            {
                System.out.println("[" + name + "] Path is invalid. Try again.");
                System.out.print("[" + name + "] >>> ");
            }
            else if (path.length == 0)
            {
                System.out.print("[" + name + "] >>> ");
            }
            else if (!contains(legalMoves, path))
            {
                System.out.println("[" + name
                        + "] Path specifies an illegal move. Try again.");
                System.out.print("[" + name + "] >>> ");
            }
            else
            {
                valid = true;
            }
        }
        System.out.println("[" + name + "] Path accepted.");
        return path;
    }

    /**
     * Parses an input String into a path of a specific length.
     * 
     * @return An int[] path.
     */
    private int[] parse(String line, int length, ArrayList<int[]> moves)
    {
        if (line.charAt(0) == 'M' || line.charAt(0) == 'm')
        {
            String index = line.substring(1, line.length());
            int i;
            try
            {
                i = Integer.parseInt(index);
            }
            catch (NumberFormatException e)
            {
                return moves.get(0);
            }
            if (i >= moves.size())
            {
                return null;
            }
            return moves.get(i);            
        }
        if (line.equals("?"))
        {
            for (int i = 0; i < moves.size(); i++)
            {
                int[] move = moves.get(i);
                System.out.print("(");
                for (int j = 0; j < move.length; j++)
                {
                    System.out.print(move[j]);
                    if (j < move.length - 1)
                    {
                        System.out.print(" ");
                    }
                }
                System.out.print(")");
                if (i < moves.size() - 1)
                {
                    System.out.print(", ");
                }
            }
            System.out.println();
            return new int[] {};
        }
        int[] path = new int[length];
        Scanner scan = new Scanner(line);
        for (int i = 0; i < length; i++)
        {
            try
            {
                path[i] = scan.nextInt();
            }
            catch (Exception e)
            {
                scan.close();
                return null;
            }
        }
        scan.close();
        return path;
    }

    /**
     * Determines whether a given int[] is contained in a given ArrayList.
     * 
     * @param list The list to check.
     * @param a The int[] to check for.
     * @return Whether the int[] is contained in the list.
     */
    private boolean contains(ArrayList<int[]> list, int[] a)
    {
        boolean contains = false;
        for (int[] c : list)
        {
            if (Arrays.equals(a, c))
            {
                contains = true;
            }
        }
        return contains;
    }

    /**
     * Gets a description of this Player.
     * 
     * @return A String.
     */
    public String description()
    {
        return "A human player named " + name;
    }

    /**
     * Gets the name of this Player.
     * 
     * @return A String.
     */
    public String name()
    {
        return name;
    }
}
