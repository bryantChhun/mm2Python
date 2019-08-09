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
public class Py4JEntryPoint implements DataMapInterface, DataPathInterface {
    private static Studio mm;
    private static CMMCore mmc;
    private static Py4JListener listener;

    /**
     * 
     * @param mm_: the parent studio object.
     */
    Py4JEntryPoint(Studio mm_){
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

    //============== provide utility functions for system management ====//

    public void clearQueue(){
        MDSQueue m = new MDSQueue();
        m.resetQueue();
    }
    
    //============== Data Map interface methods ====================//
    //== For retrieving MetaDataStore objects ======================//

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

    // Methods to return metadatastores based on arbitrary parameters//
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
    //== For retrieving Filepaths to mmap files =====================//


    // Path retrieval interface methods

    @Override
    public String getLastFileByChannelName(String channelName) throws IllegalAccessException{
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by channel
    @Override
    public String getLastFileByChannelIndex(int channelIndex) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by z
    @Override
    public String getLastFileByZ(int z) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by p
    @Override
    public String getLastFileByPosition(int pos) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get last file by t
    @Override
    public String getLastFileByTime(int time) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by channel_name
    @Override
    public String getFirstFileByChannelName(String channelName) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by channel
    @Override
    public String getFirstFileByChannelIndex(int channelIndex) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel(channelIndex).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by z
    @Override
    public String getFirstFileByZ(int z) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().z(z).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by p
    @Override
    public String getFirstFileByPosition(int pos) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().position(pos).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

    // get first file by t
    @Override
    public String getFirstFileByTime(int time) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().time(time).buildMDSParams();
        MDSQueue m = new MDSQueue();
        return m.getLastFilenameByParam(params);
    }

}
