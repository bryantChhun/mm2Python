/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.MicroManager.Exceptions;

/**
 *
 * @author bryant.chhun
 */
public class MicromanagerException extends Exception {
    public MicromanagerException(String pString)
    {
        super(pString);
    }

    public MicromanagerException(String pErrorMessage, Throwable pThrowable)
    {
        super(pErrorMessage, pThrowable);
    }

}
