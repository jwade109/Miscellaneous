package tictactoe;

/**
 * Runs the tic tac grow game, initializing all the parts.
 * 
 * @author William McDermott
 * @version 2016.12.26
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
        
        for (int i = 0; i < 100; i++)
        {
            System.out.println("RUN NUMBER: " + (i + 1));
            new GameManager(new TicTacGrow(gameOrder), 
                new LegalRandomAIPlayer("X AI"),
                new LegalRandomAIPlayer("O AI")).run();
        }
    }
}
