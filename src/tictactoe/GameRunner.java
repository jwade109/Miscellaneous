package tictactoe;

/**
 * Runs the tic tac grow game, initializing all the parts.
 * 
 * @author William McDermott
 * @version 2017.01.01
 */
public class GameRunner
{
    /**
     * Runs the game.
     */
    public static void main(String[] args)
    {
        int gameOrder = 2;
        
        if (args.length > 0)
        {
            gameOrder = Integer.valueOf(args[0]);
        }
        
        // I choose to run it a certain number of iterations just because
        int xWins = 0;
        int oWins = 0;
        int runs = 10000;
        for (int i = 0; i < runs; i++)
        {
            System.out.println("RUN NUMBER: " + (i + 1));
            Player p = new GameManager(new TicTacGrow(gameOrder), 
                new LegalRandomAIPlayer("X AI"),
                new LegalRandomAIPlayer("O AI ")).run();
            if (p.getName().length() == 4)
            {
                xWins++;
            }
            else if (p.getName().length() == 5)
            {
                oWins++;
            }
        }
        System.out.println("X won " + xWins + " times!");
        System.out.println("O won " + oWins + " times!");
        System.out.println("Game tied " + (runs - xWins - oWins) + " times!");
    }
}
