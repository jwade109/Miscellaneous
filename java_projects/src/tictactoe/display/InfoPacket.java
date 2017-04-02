package tictactoe.display;

/**
 * Represents a packet of information sent out of a game, for use in the
 * update() method of the attached displays.
 * 
 * This class holds two pieces of data: the data the observable is reporting,
 * and a selector for which method should be called in the observer.
 * These are handled by a Packet Manager, which then calls the appropriate 
 * methods on the display.
 * 
 * @author William McDermott
 * @version 2016.01.05
 */
public class InfoPacket
{
    /**
     * Assigns this packet's data to a method in the receiver.
     */
    private final int selector;
    
    /**
     * Data for the receiving observer to use.
     */
    private final Object data;
    
    /**
     * Uses the specified data to make a new InfoPacket.
     * 
     * @param selector  The method receiving data.
     * @param data      The data used in that method.
     */
    public InfoPacket(int selector, Object data)
    {
        this.selector = selector;
        this.data = data;
    }
    
    /**
     * Retrieves the selector integer, which is assigned to a method.
     * @return  The selector for which method to call.
     */
    public int getSelector()
    {
        return selector;
    }
    
    /**
     * Retrieves the data needed for a method.
     * @return  The data for the selected method to run.
     */
    public Object getData()
    {
        return data;
    }
}
