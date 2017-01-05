package tictactoe.display;

import java.util.Observer;

/**
 * This abstract class designates the abilities a DisplayManager
 * class should have, in whatever medium of display it uses.
 *
 * TODO
 * 
 * @author William McDermott
 * @version 2016.01.05
 */
public interface DisplayManagerInterface extends Observer
{
    /**
     * Displays the beginning of the game.
     */
    void displayStart();
    
    /**
     * Displays the entire board, or updates the entire board for display.
     */
    void displayBoard();
    
    /**
     * Updates the display at one position of the board,
     * a certain level and a certain cell.
     */
    void displayMove();
    
    /**
     * Updates the display after a player fouls.
     */
    void displayFoul();
    
    /**
     * Updates the display after a winner is found.
     */
    void displayWinner();
}
