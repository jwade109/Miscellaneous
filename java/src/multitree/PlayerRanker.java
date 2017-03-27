package tree;

import java.util.Comparator;

public class PlayerRanker implements Comparator<Player>
{
    private int order;
    private int maxGames;
    
    public PlayerRanker(int order, int maxGames)
    {
        this.order = order;
        this.maxGames = maxGames;
    }
    
    public int compare(Player p1, Player p2)
    {
        int[] record = new Tourney(p1, p2, order, maxGames).compete();
        System.out.println(p1.name() + "(" + record[0] + ") vs " + p2.name() + "(" + record[1] + ")");
        return record[1] - record[0];
    }
}
