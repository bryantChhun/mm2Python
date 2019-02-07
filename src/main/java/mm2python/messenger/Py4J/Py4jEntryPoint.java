/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.messenger.Py4J;

import mm2python.mmDataHandler.dataInterface;
import mm2python.DataStructures.constants;
import mmcorej.CMMCore;
import org.micromanager.Studio;

import mm2python.UI.reporter;


/**
 * 
 * @author bryant.chhun
 */
public class Py4jEntryPoint implements dataInterface {
    private static Studio mm;
    private static CMMCore mmc;
    private static Py4JListener listener;

    /**
     * 
     * @param mm_: the parent studio object.
     */
    Py4jEntryPoint(Studio mm_){
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

    public String classtest() throws Exception {
        reporter.set_report_area(true, false, "classtest called");
        Class<?> py = Class.forName("mm2python.messenger.Py4J.Py4JListenerInterface");
        return py.toString();
    }

    public String getClassPath() {
        ClassLoader loader = Py4jEntryPoint.class.getClassLoader();
        return loader.getResource("").toString();
    }


    public String getSystemPath() {
        return System.getProperty("java.class.path");
    }
    
    //============== Data interface methods ====================//

    //TODO: inspect the data bit depth and pixel size, send this!

    @Override
    public String retrieveFileByChannelName(String channel_name) {
        String filepath = constants.getNextFileForChannel(channel_name);
        reporter.set_report_area(true, false, "====== Retrieve file requested ======== ");
        reporter.set_report_area(true, false, "Filepath = "+filepath);
//        constants.removeChanToMetaStoreMap(channel_name);
        constants.removeChanToFilenameMap(channel_name);
        return filepath;
    }

//    @Override
//    public boolean storeByIndexExists(int time, int stage, int z){
//
//    }
//
//    @Override
//    public boolean removeByIndex(){
//
//    }

    @Override
    public boolean storeByChannelNameExists(String channel_name) {
        return constants.nextImageExists(channel_name);
    }

//    @Override
//    public boolean removeByChannelName(String channel_name) {
//
//    }

//    @Override
//    public boolean doesStoreExist(MetaDataStore store_) {
//        if(constants.MetaDataToFileMap.containsKey(store_)){
//            return true;
//        } else{
//            return false;
//        }
//    }
//
//    @Override
//    public String retrieveStorePath(MetaDataStore store_) {
//        return constants.MetaDataToFileMap.get(store_);
//    }



    //    @Override
//    public String popData() {
//        try {
//            return constants.LBQ_data_queue.take();
//        } catch (InterruptedException ex) {
//            System.out.println("interrupted exception: popData interrupted while waiting for take");
//        }
//        return null;
//    }
//
//    @Override
//    public String popMetaData() {
//        try {
//            return constants.LBQ_metadata_queue.take();
//        } catch (InterruptedException ex) {
//            System.out.println("interrupted exception: popData interrupted while waiting for take");
//        }
//        return null;
//    }
    
//    @Override
//    public Boolean isEmpty() {
//        return constants.LBQ_data_queue.isEmpty();
//    }
//
//    @Override
//    public String viewData() {
//        return constants.LBQ_data_queue.peek();
//    }
//
//    @Override
//    public String viewMetaData() {
//        return constants.LBQ_metadata_queue.peek();
//    }

}