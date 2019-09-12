package org.mm2python.DataStructures.Exceptions;

/**
 *
 * @author bryant.chhun
 */
public class OSTypeException extends Exception {

    public OSTypeException(String pString)
    {
        super(pString);
    }

    public OSTypeException(String pErrorMessage, Throwable pThrowable)
    {
        super(pErrorMessage, pThrowable);
    }

}