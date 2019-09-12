/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.MPIMethod;

/**
 *
 * @author bryant.chhun
 */
public interface messengerInterface {
    
    public void startConnection(int port);
    
    public void startConnection();
    
    public void stopConnection(int port);
    
    public void stopConnection();
    
}
