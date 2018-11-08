/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messenger.Py4J;

import MicroManager.micromanagerInterface;
import mmDataHandler.dataInterface;
import Constants.constants;
import mmcorej.CMMCore;
import org.micromanager.Studio;

/**
 * 
 * @author bryant.chhun
 */
public class Py4jEntryPoint implements micromanagerInterface, dataInterface {
    private static Studio mm;
    private static CMMCore mmc;
    private static Py4JListener listener;


    /**
     * 
     * @param mm_: the parent studio object.
     */
    public Py4jEntryPoint(Studio mm_){
        mm = mm_;
        mmc = mm_.getCMMCore();
        listener = new Py4JListener();
    }
    
    public Studio getStudio() {
        return mm;
    }
    
    public CMMCore getCMMCore() {
        return mmc;
    }
    
    public Py4JListener getListener() {
        return listener;
    }
    
    // ======================= Micromanager interface methods ==========//
    
    @Override
    public void setLCA(float value) {
//        try{
//            mmc.setProperty("LCA", "propertyName", value);
//        } catch (Exception ex) {
//            mm.logs().logError(ex);
//            System.out.println(ex);
//        }
    }
    
    @Override
    public void setLCB(float value) {
//        try{
//            mmc.setProperty("LCB", "propertyName", value);
//        } catch (Exception ex) {
//            mm.logs().logError(ex);
//            System.out.println(ex);
//        }
    }
    
    //============== Data interface methods ====================//
    
    @Override
    public String popData() {
        try {
            return constants.LBQ_data_queue.take();
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception: popData interrupted while waiting for take");
        }
        return null;
    }
    
    @Override
    public String popMetaData() {
        try {
            return constants.LBQ_metadata_queue.take();
        } catch (InterruptedException ex) {
            System.out.println("interrupted exception: popData interrupted while waiting for take");
        }
        return null;
    }
    
    @Override
    public Boolean isEmpty() {
        return constants.LBQ_data_queue.isEmpty();
    }
    
    @Override
    public String viewData() {
        return constants.LBQ_data_queue.peek();
    }
    
    @Override
    public String viewMetaData() {
        return constants.LBQ_metadata_queue.peek();
    }

}
