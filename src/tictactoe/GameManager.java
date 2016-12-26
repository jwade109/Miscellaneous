package tictactoe;

/**
 * Runs an instance of the game, by calling methods on the players and board.
 * 
 * @author William McDermott
 * @version 2016.12.25
 */
public class GameManager
{
    /**
     * A TicTacGrow to play.
     */
    private Board gameBoard;
    
    /**
     * The player who plays X.
     */
    private Player playerX;
    
    /**
     * The player who plays O.
     */
    private Player playerO;
    
    /**
     * The previous move made, which restricts the next move.
     */
    private int[] lastMove;
    
    /**
     * The number of moves that have been made.
     */
    private int moves;
    
    /**
     * Creates a new GameManager with two players and a board.
     */
    public GameManager(Board gameBoard, Player playerX, Player playerO)
    {
        this.gameBoard = gameBoard;
        this.playerX = playerX;
        this.playerO = playerO;
        lastMove = new int[0];
        moves = 0;
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
        
        if (moves % 2 == 0) 
        {
            currentPlayer = playerX;
        }
        else
        {
            currentPlayer = playerO;
        }
        int[] nextMove = currentPlayer.move(gameBoard, lastMove);
        if (!Board.isValidMove(nextMove, lastMove))
        {
            // handle an illegal move.
            // handle too many calls for another move or something.
            // ask this dummy for another move
            this.makeMove();
            return;
        }
        Board.setState(nextMove, )
        
        moves++;
    }
}
