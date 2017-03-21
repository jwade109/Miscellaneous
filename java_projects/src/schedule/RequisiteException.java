package schedule;

public class RequisiteException extends RuntimeException
{
    private static final long serialVersionUID = 1L;
    
    public RequisiteException()
    {
        super();
    }
    
    public RequisiteException(String message)
    {
        super(message);
    }
}