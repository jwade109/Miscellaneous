package multitree;

public class Manager
{
    private Player one;
    private Player two;
    private TicTacGrow g;
    private boolean turn;

    public static void main(String[] args) throws Exception
    {
        for (int i = 0; i < 10; i++)
        {
        Manager m = new Manager(new RandomAI(), new RandomAI(), 3);
        while (!m.gameOver())
        {
            // System.out.print(m.show());
            m.turn();
        }
        System.out.println(m.show());
        }
    }

    public Manager(Player player1, Player player2, int gameOrder)
    {
        one = player1;
        two = player2;
        turn = true;
        g = new TicTacGrow(gameOrder);
    }

    public void turn() throws Exception
    {
        if (turn)
        {
            g.set(Play.X, one.makeMove(g));
        }
        else
        {
            g.set(Play.O, two.makeMove(g));
        }
        turn = !turn;
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
        }
        out.append(g.toString());
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
