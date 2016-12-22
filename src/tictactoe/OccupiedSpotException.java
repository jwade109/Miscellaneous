package tictactoe;

public class OccupiedSpotException extends IllegalStateException
{
    // No serializable variable included, because I don't know how it helps.
    
    public OccupiedSpotException()
    {
        super();
    }
    
    public OccupiedSpotException(String message)
    {
        super(message);
    }
}
