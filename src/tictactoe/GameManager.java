package tictactoe;

import java.util.Observable;

/**
 * Runs an instance of the game, by calling methods on the players and board.
 * 
 * @author William McDermott
 * @version 2016.12.28
 */
public class GameManager extends Observable
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
            // this.printBoard();
            this.makeMove();
        }
        this.printBoard();
        this.printWinner();
    }
    
    /**
     * Asks a player to make a move, updating the game and applying the move
     * if it works, and fouling the player if it doesn't.
     */
    private void makeMove()
    {
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
        Coordinate coord = currentPlayer.move(gameBoard.clone());
        int[] thisMove = coord.getTreePath();
        this.printMove(thisMove, shape);
        // this.printBoard();
        if (!gameBoard.isValidMove(coord))
        {
            System.out.println(shape + " made an illegal move!");
            this.callFoulForShape(shape);
            this.makeMove();
            return; // so it doesn't keep executing after the recursion calls
        }
        gameBoard.move(thisMove, shape);
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
        System.out.println("[" + pair[0] + ", " + pair[1] + "].");
    }
    
    /**
     * Prints out the gameBoard for viewing.
     */
    public void printBoard()
    {
        System.out.println(gameBoard.toString());
    }
    
    /**
     * Prints out the winner of the game!
     * Could be more complicated later.
     */
    public void printWinner()
    {
        System.out.println(gameBoard.getWinner() + " has won the game!");
    }
}
