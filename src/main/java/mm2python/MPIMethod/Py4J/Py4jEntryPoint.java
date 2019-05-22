/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.MPIMethod.Py4J;

import mm2python.DataStructures.Builders.MDSParamBuilder;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.Constants;
import mm2python.DataStructures.Maps.MDSMap;
import mm2python.DataStructures.MetaDataStore;
import mm2python.DataStructures.Queues.MDSQueue;
import mm2python.mmDataHandler.DataPathInterface;
import mm2python.mmDataHandler.DataMapInterface;
import mmcorej.CMMCore;
import org.micromanager.Studio;

import java.util.ArrayList;


/**
 * 
 * @author bryant.chhun
 */
public class Py4jEntryPoint implements DataMapInterface, DataPathInterface {
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

    public int getBitDepth() {return (int)Constants.bitDepth;}

    public int getHeight() {return (int) Constants.height;}

    public int getWidth() {return (int) Constants.width;}
    
    //============== Data Map interface methods ====================//

    // Map retrieval interface methods
    @Override
    public ArrayList<String> getFilesByChannelName(String channelName) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getFilenamesByParams(params);
    }

    @Override
    public ArrayList<String> getFilesByChannelIndex(int channelIndex) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getFilenamesByParams(params);
    }

    @Override
    public ArrayList<String> getFilesByZ(int z) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getFilenamesByParams(params);
    }

    @Override
    public ArrayList<String> getFilesByPosition(int pos) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getFilenamesByParams(params);
    }

    @Override
    public ArrayList<String> getFilesByTime(int time) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getFilenamesByParams(params);
    }

    @Override
    public ArrayList<MetaDataStore> getMetaByChannelName(String channelName) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getMDSByParams(params);
    }

    @Override
    public ArrayList<MetaDataStore> getMetaByChannelIndex(int channelIndex) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getMDSByParams(params);
    }

    @Override
    public ArrayList<MetaDataStore> getMetaByZ(int z) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getMDSByParams(params);
    }

    @Override
    public ArrayList<MetaDataStore> getMetaByPosition(int pos) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getMDSByParams(params);
    }

    @Override
    public ArrayList<MetaDataStore> getMetaByTime(int time) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSMap m = new MDSMap();
        return m.getMDSByParams(params);
    }

    // MDSQueue retrieval interface methods
    @Override
    public MetaDataStore getLastMetaByChannelName(String channelName) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastMDSByParam(params);
    }

    @Override
    public MetaDataStore getLastMetaByChannelIndex(int channelIndex) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastMDSByParam(params);
    }

    @Override
    public MetaDataStore getLastMetaByZ(int z) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastMDSByParam(params);
    }

    @Override
    public MetaDataStore getLastMetaByPosition(int pos) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastMDSByParam(params);
    }

    @Override
    public MetaDataStore getLastMetaByTime(int time) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastMDSByParam(params);
    }

    @Override
    public MetaDataStore getFirstMetaByChannelName(String channelName) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getFirstMDSByParam(params);
    }

    @Override
    public MetaDataStore getFirstMetaByChannelIndex(int channelIndex) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getFirstMDSByParam(params);
    }

    @Override
    public MetaDataStore getFirstMetaByZ(int z) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getFirstMDSByParam(params);
    }

    @Override
    public MetaDataStore getFirstMetaByPosition(int pos) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getFirstMDSByParam(params);
    }

    @Override
    public MetaDataStore getFirstMetaByTime(int time) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getFirstMDSByParam(params);
    }

    public MetaDataStore getLastMeta() {
        MDSQueue m = new MDSQueue();
        return m.getLastMDS();
    }

    public MetaDataStore getFirstMeta() {
        MDSQueue m = new MDSQueue();
        return m.getFirstMDS();
    }

    // extras
    /**
     * get the parameter builder to search on custom subset of parameters
     *  useful if you want to search on 2 or more parameters
     * @return : MDSParamBuilder object
     */
    @Override
    public MDSParamBuilder getParameterBuilder() {
        return new MDSParamBuilder();
    }

    /**
     * get a list of MetaDataStore objects that match the supplied parameters
     *  note: to extract on unhashed parameters, you must filter the results externally
     *      i.e.: arraylist.get(0).windowname == "specific window name"
     * @param mdsp : MDSParameters object
     * @return : Arraylist of MDS (or list in python)
     */
    @Override
    public ArrayList<MetaDataStore> getMetaByParameters(MDSParameters mdsp) {
        return new MDSMap().getMDSByParams(mdsp);
    }

    //============== Data Path interface methods ====================//

    // Path retrieval interface methods

    public String getLastFileByChannelName(String channelName) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by channel
    public String getLastFileByChannelIndex(int channelIndex) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by z
    public String getLastFileByZ(int z) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by p
    public String getLastFileByPosition(int pos) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by t
    public String getLastFileByTime(int time) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by channel_name
    public String getFirstFileByChannelName(String channelName) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by channel
    public String getFirstFileByChannelIndex(int channelIndex) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by z
    public String getFirstFileByZ(int z) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by p
    public String getFirstFileByPosition(int pos) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by t
    public String getFirstFileByTime(int time) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }



//    @Override
//    /**
//     * only returns the most recent store associated with that channel
//     */
//    public MetaDataStore getStore(String channel_name) {
//        return Maps.getStore(channel_name);
//    }
//
//    @Override
//    public boolean storeExists(String channel_name) {
//        return Maps.nextImageExists(channel_name);
//    }
//
//    @Override
//    public boolean fileExists(String channel_name) {
//        return Maps.nextImageExists(channel_name);
//    }
//
//    @Override
//    public boolean fileExists(MetaDataStore store) {
//        return Maps.nextImageExists(store);
//    }
//
//    public boolean nextImageExists() {
//        return PathQueue.nextPathExists();
//    }
//
//    public String getNextImage() {
//        return PathQueue.getNextPath();
//    }


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
