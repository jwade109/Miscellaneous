package schedule;

public class DuplicateItemException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public DuplicateItemException()
    {
        super();
    }

    public DuplicateItemException(String message)
    {
        super(message);
    }
}
