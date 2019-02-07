package mm2python.mmEventHandler.Exceptions;


public class NotImplementedException extends Exception{

    public NotImplementedException(String pString)
    {
        super(pString);
    }

    public NotImplementedException(String pErrorMessage, Throwable pThrowable)
    {
        super(pErrorMessage, pThrowable);
    }

}
