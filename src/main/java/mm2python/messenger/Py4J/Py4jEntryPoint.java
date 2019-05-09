/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.messenger.Py4J;

import mm2python.DataStructures.Maps;
import mm2python.DataStructures.MetaDataStore;
import mm2python.DataStructures.Queues;
import mm2python.mmDataHandler.dataInterface;
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
    
    //============== Data interface methods ====================//

    //TODO: inspect the data bit depth and pixel size, send this!

    @Override
    public String getFile(String channel_name) {
        String filepath = Maps.getNextFileForChannel(channel_name);
        reporter.set_report_area(true, false, "====== Retrieve file requested ======== ");
        reporter.set_report_area(true, false, "Filepath = "+filepath);
//        Constants.removeChanToMetaStoreMap(channel_name);
        Maps.removeChanToFilenameMap(channel_name);
        return filepath;
    }

    @Override
    public String getFile(MetaDataStore store) {
        return Maps.getFileFromMetaStore(store);
    }

    @Override
    /**
     * only returns the most recent store associated with that channel
     */
    public MetaDataStore getStore(String channel_name) {
        return Maps.getStore(channel_name);
    }

    @Override
    public boolean storeExists(String channel_name) {
        return Maps.nextImageExists(channel_name);
    }

    @Override
    public boolean fileExists(String channel_name) {
        return Maps.nextImageExists(channel_name);
    }

    @Override
    public boolean fileExists(MetaDataStore store) {
        return Maps.nextImageExists(store);
    }

    public boolean nextImageExists() {
        return Queues.nextImageExists();
    }

    public String getNextImage() {
        return Queues.getNextImage();
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

//    @Override
//    public boolean removeByChannelName(String channel_name) {
//
//    }

//    @Override
//    public boolean doesStoreExist(MetaDataStore store_) {
//        if(Constants.MetaDataToFileMap.containsKey(store_)){
//            return true;
//        } else{
//            return false;
//        }
//    }
//
//    @Override
//    public String retrieveStorePath(MetaDataStore store_) {
//        return Constants.MetaDataToFileMap.get(store_);
//    }



    //    @Override
//    public String popData() {
//        try {
//            return Constants.LBQ_data_queue.take();
//        } catch (InterruptedException ex) {
//            System.out.println("interrupted exception: popData interrupted while waiting for take");
//        }
//        return null;
//    }
//
//    @Override
//    public String popMetaData() {
//        try {
//            return Constants.LBQ_metadata_queue.take();
//        } catch (InterruptedException ex) {
//            System.out.println("interrupted exception: popData interrupted while waiting for take");
//        }
//        return null;
//    }
    
//    @Override
//    public Boolean isEmpty() {
//        return Constants.LBQ_data_queue.isEmpty();
//    }
//
//    @Override
//    public String viewData() {
//        return Constants.LBQ_data_queue.peek();
//    }
//
//    @Override
//    public String viewMetaData() {
//        return Constants.LBQ_metadata_queue.peek();
//    }

}
