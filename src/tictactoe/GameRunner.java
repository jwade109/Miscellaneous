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
        // boolean player1AI = true;
        // boolean player2AI = true;
        
        if (args.length > 0)
        {
            gameOrder = Integer.valueOf(args[0]);
            // player1AI = Boolean.valueOf(args[1]);
            // player2AI = Boolean.valueOf(args[2]);
        }
        
        Board game = new Board(gameOrder);
        new GameManager(game, 
            new AIPlayer("X AI"), // worrying about names later
            new AIPlayer("O AI")).run();
    }
}