package tree;

public class Manager
{
    private Player one;
    private Player two;
    private TicTacGrow g;
    private boolean turn;

    public static void main(String[] args)
    {
        Manager m = new Manager(new RandomAI("Rando (X)"), new SmartAI("Smart (O)"), 2);
        while (!m.gameOver())
        {
            System.out.print(m.show() + "\n");
            m.turn();
        }
        System.out.println(m.show());
    }

    public Manager(Player player1, Player player2, int gameOrder)
    {
        one = player1;
        two = player2;
        turn = true;
        g = new TicTacGrow(gameOrder);
    }

    public Play get(int x, int y)
    {
        return g.get(x, y);
    }
    
    public int getWidth()
    {
        return g.size();
    }
    
    public String[] turn()
    {
        System.out.println(g.moveCount() + "-------------------------------\n");
        String[] out;
        int[] path;
        String play;
        if (turn)
        {
            path = one.makeMove(g.clone());
            play = "X";
            g.set(Play.X, path);
        }
        else
        {
            path = two.makeMove(g.clone());
            play = "O";
            g.set(Play.O, path);
        }
        out = new String[path.length + 1];
        out[0] = play;
        for (int i = 0; i < path.length; i++)
        {
            out[i + 1] = "" + path[i];
        }
        turn = !turn;
        return out;
    }

    public String show()
    {
        StringBuilder out = new StringBuilder();
        if (gameOver())
        {
            out.append("~ ");
            if (getWinner() == null)
            {
                out.append("Nobody");
            }
            else
            {
                out.append(getWinner().name());
            }
            out.append(" is the winner!\n");
            out.append(g.toString());
        }
        else
        {
            out.append(g.toString(g.order()) + "\n");
        }
        return out.toString();
    }

    public boolean gameOver()
    {
        return g.isOver();
    }

    public Player getWinner()
    {
        if (g.getWinner() == Play.X)
        {
            return one;
        }
        else if (g.getWinner() == Play.O)
        {
            return two;
        }
        return null;
    }
}
