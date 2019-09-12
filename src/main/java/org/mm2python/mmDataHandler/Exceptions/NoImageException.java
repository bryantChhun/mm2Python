/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.mmDataHandler.Exceptions;

/**
 *
 * @author bryant.chhun
 */
public class NoImageException extends Exception{

    public NoImageException(String pString)
    {
        super(pString);
    }

    public NoImageException(String pErrorMessage, Throwable pThrowable)
    {
        super(pErrorMessage, pThrowable);
    }

}
