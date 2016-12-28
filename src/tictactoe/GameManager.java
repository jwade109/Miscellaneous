package tictactoe;

/**
 * Runs an instance of the game, by calling methods on the players and board.
 * 
 * @author William McDermott
 * @version 2016.12.28
 */
public class GameManager
{
    /**
     * A TicTacGrow to play.
     */
    private TicTacGrow gameBoard;
    
    /**
     * The player who plays X.
     */
    private Player playerX;
    
    /**
     * The player who plays O.
     */
    private Player playerO;
    
    /**
     * The number of times X can make us cross.
     */
    private int xFouls;
    
    /**
     * The number of times O can think in circles.
     */
    private int oFouls;
    
    /**
     * Creates a new GameManager with two players and a board.
     */
    public GameManager(TicTacGrow gameBoard, Player playerX, Player playerO)
    {
        this.gameBoard = gameBoard;
        this.playerX = playerX;
        this.playerO = playerO;
        this.printGameStarted();
        xFouls = 50;
        oFouls = 50;
    }
    
    /**
     * Runs the game until a winner is found!
     */
    public void run()
    {
        while (!gameBoard.isGameOver())
        {
            this.makeMove();
        }
    }
    
    /**
     * Asks a player to make a move, updating the game and applying the move
     * if it works.
     */
    private void makeMove()
    {
        /*
         * I'm gonna write some code here which may not work exactly just yet,
         * but I think it's a valid way to approach this conceptually
         * 
         * Player objects would need a single method called takeTurn(), which would take
         * a TicTacGrow object as an argument and return the same object modified by one move.
         * This is necessary because Players do not (should not) store references of the game
         * they are playing. This allows the Manager to manage when Players take their turn,
         * and allows TicTacGrow to ensure Players make valid moves.
         */
        
        /**
         * I'm going to use java doc comments to respond because it looks nice.
         * You use the green ones, so the opposite color thing happens.
         * We should really use a chat though rather than this lol.
         * 
         * So maybe I am wrong about this, but if we feed the players a board,
         * won't they be able to do whatever the heck they want with it? Like move 5 times?
         * I originally wrote some stuff with Observables, but I realized composition
         * made more sense to me for how they all got the stuff put together.
         * 
         * I think I have updated some things, lke deleting TicTacGrow,
         * so we need to make sure that we are on the same page, and not just 
         * because one is right or wrong, I think that we have different
         * good ideas and we need to converse so it is cohesive.
         * 
         * Also, I'm unsure whether we should have this method execute for each player,
         * or one player every other time. This way seems strange to me.
         */
        /** X moves first in my mind, 
         * also I think using % 2 on "moves" is easier. */
        
        Player currentPlayer;
        PlayEnum shape;
        if (gameBoard.getMoves() % 2 == 0) 
        {
            currentPlayer = playerX;
            shape = PlayEnum.X;
        }
        else
        {
            currentPlayer = playerO;
            shape = PlayEnum.O;
        }
        int[] thisMove = currentPlayer.move(gameBoard);
        this.printMove(thisMove, shape);
        if (!gameBoard.isValidMove(thisMove)) // board split later
        {
            System.out.println(shape + " made an illegal move!");
            this.callFoulForShape(shape);
            this.makeMove();
            return; // so it doesn't keep executing after the recursion calls
        }
        gameBoard.move(thisMove, shape);
        this.printBoard();
    }
    
    /**
     * Removes a foul from a player, for moving in the wrong location.
     * @param shape     The dumb player that moved wrong.
     */
    private void callFoulForShape(PlayEnum shape)
    {
        if (shape == PlayEnum.X)
        {
            xFouls--;
            if (xFouls == 0)
            {
                throw new IllegalStateException("X has fouled out!");
            }
        }
        else
        {
            oFouls--;
            if (oFouls == 0)
            {
                throw new IllegalStateException("O has fouled out!");
            }
        }
    }
    
    /**
     * Prints out the player names and the board state.
     */
    private void printGameStarted()
    {
        System.out.println("X is represented by player " + playerX.getName());
        System.out.println("O is represented by player " + playerO.getName());
        System.out.println("Begin!");
    }
    
    /**
     * Prints a move that just occurred to System.out,
     * so that a game can be viewed.
     * 
     * @param thisMove  The move just made.
     * @param shape     The shape just placed.
     */
    private void printMove(int[] thisMove, PlayEnum shape)
    {
        System.out.print(shape + " is moving at tree coordinate [");
        for (int i = 0; i < thisMove.length - 1; i++)
        {
            System.out.print(thisMove[i] + ", ");
        }
        System.out.print(thisMove[thisMove.length - 1] + "]");
        System.out.println(" and cartesian coordinate ");
        int[] pair = Converter.toCartesianCoordinates(thisMove);
        System.out.println("[" + pair[0] + ", " + pair[1] + "].\n\n");
    }
    
    /**
     * Prints out the gameBoard for viewing.
     */
    public void printBoard()
    {
        System.out.println(gameBoard.toString());
    }
}
