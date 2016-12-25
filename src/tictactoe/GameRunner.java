package tictactoe;

/**
 * Runs the tic tac grow game, initializing all the parts.
 * 
 * @author William McDermott
 * @version 2016.12.24
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
        
        Board game = new Board(gameOrder);
        new GameManager(game, 
            new TestAIPlayer("X AI"),
            new TestAIPlayer("O AI")).run();
    }
}
