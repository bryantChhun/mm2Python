/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.MPIMethod.Py4J;

import java.util.ArrayList;
import java.util.List;

import mm2python.MPIMethod.Py4J.Exceptions.Py4JListenerException;

/**
 *
 * @author bryant.chhun
 */
public class Py4JListener {
    
    private static List<Py4JListenerInterface> listeners = new ArrayList<Py4JListenerInterface>();

//    public Py4JListener() {
//        System.out.println("constructing the listener");
//    }
    
    //================ Listener methods =================================//
    
    public static void registerListener(Py4JListenerInterface listener) throws Py4JListenerException {
        try{
            listeners.add(listener);
        } catch (Exception ex) {
            throw new Py4JListenerException("Py4J Exception while registering listener: "+ex.toString());
        }
    }

    public static void notifyAllListeners() throws Py4JListenerException {
        try{
            listeners.stream().forEach((listener) -> {
//                Object returnValue = listener.notify(this);
                Object returnValue = listener.notify("this string is the notification");
                System.out.println(returnValue);
            });
        } catch (Exception ex) {
            throw new Py4JListenerException("Py4J Exception while notifying all listeners: "+ex.toString());
        }
    }

    @Override
    public String toString() {
        return "<py4jEntryPoint> instance";
    }
    
}
