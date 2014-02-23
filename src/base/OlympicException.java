package base;

public class OlympicException extends Exception{
	private static final long serialVersionUID = 1L;

	public OlympicException() {}

    public OlympicException(String message, Exception nestedException)
    {
       super(message, nestedException);
    }

}
