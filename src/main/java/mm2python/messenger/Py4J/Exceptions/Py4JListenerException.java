/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.messenger.Py4J.Exceptions;

/**
 *
 * @author bryant.chhun
 */
public class Py4JListenerException extends Exception{
    
    public Py4JListenerException(String pString)
    {
        super(pString);
    }

    public Py4JListenerException(String pErrorMessage, Throwable pThrowable)
    {
        super(pErrorMessage, pThrowable);
    }

}
