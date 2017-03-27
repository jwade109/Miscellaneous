package genetic;

public class SpeciesMismatchException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public SpeciesMismatchException()
    {
        super();
    }
    
    public SpeciesMismatchException(String message)
    {
        super(message);
    }
    
}