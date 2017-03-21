package tree;

public class Tourney
{
    private Player one;
    private Player two;
    private int games;
    private int order;

    public Tourney(Player one, Player two, int order, int games)
    {
        this.one = one;
        this.two = two;
        this.games = games;
        this.order = order;
    }

    public int[] compete()
    {
        int[] record = new int[2];
        int iter = 0;
        while (iter < games)
        {
            Player first;
            Player second;
            if (iter % 2 == 0)
            {
                first = one;
                second = two;
            }
            else
            {
                first = two;
                second = one;
            }
            Manager m = new Manager(first, second, order);
            while (!m.gameOver())
            {
                m.turn();
            }
            Player winner = m.getWinner();
            if (winner == one)
            {
                record[0] += 2;
            }
            else if (winner == two)
            {
                record[1] += 2;
            }
            if (winner == null)
            {
                record[0]++;
                record[1]++;
            }
            iter++;
        }
        return record;
    }
}
