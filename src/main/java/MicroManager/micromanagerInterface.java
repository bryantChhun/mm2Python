/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MicroManager;

import MicroManager.Exceptions.MicromanagerException;

/**
 * methods to implement for micromanager control of specific devices
 * @author bryant.chhun
 */
public interface micromanagerInterface {
    
    public void setLCA(float value) throws MicromanagerException;
    
    public void setLCB(float value) throws MicromanagerException;
    
}
