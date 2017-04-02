package tictactoe.display;

import tictactoe.player.Player;
import tictactoe.game.Coordinate;
import tictactoe.game.PlayEnum;

/**
 * This abstract class designates the abilities a DisplayManager
 * class should have, in whatever medium of display it uses.
 *
 * TODO
 * 
 * @author William McDermott
 * @version 2016.01.05
 */
public interface Display
{
    /**
     * Displays the beginning of the game.
     */
    void displayStart(String playerNames);
    
    /**
     * Displays the entire board, or updates the entire board on the display.
     */
    void displayBoard(); // not sure how to do this
    
    /**
     * Updates the display at one position of the board,
     * a certain level and a certain cell.
     */
    void displayMove(Coordinate move, PlayEnum shape);
    
    /**
     * Updates the display to show the move number, where each player's move
     * counts as one move.
     */
    void displayMoveCount(int moves);
    
    /**
     * Updates the display to show the next move reference location.
     */
    void displayNextMove(Coordinate nextMove);
    
    /**
     * Updates the display after a certain player fouls.
     */
    void displayFoul(Player fouled);
    
    /**
     * Updates the display when the foul count changes.
     */
    void displayFoulCount(int fouls, PlayEnum shape);
    
    /**
     * Updates the display after a certain player wins (or the game ties).
     */
    void displayWinner(Player winner);
}
