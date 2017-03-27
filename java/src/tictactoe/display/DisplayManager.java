package tictactoe.display;

import java.util.Observable;
import java.util.Observer;

/**
 * Controls when to call display methods based on info packets it receives.
 * 
 * This class holds a display object, and manages different calls to it 
 * using received InfoPackets as instructions on what to call.
 * This helps to further decouple the display from the simulation of the 
 * program.
 * 
 * @author William McDermott
 * @version 2016.01.05
 */
public class DisplayManager implements Observer
{
    /**
     * The Display object to receive method calls.
     */
    private Display view;

    /**
     * Creates a new DisplayManager with the specified Display.
     * @param view  The display to initialize this object with.
     */
    public DisplayManager(Display view)
    {
        this.view = view;
    }
    
    /**
     * 
     * @param o     The expected InfoPacket of data.
     * @param arg   
     */
    @Override
    public void update(Observable theManager, Object arg)
    {
        // TODO Auto-generated method stub
    }
}
