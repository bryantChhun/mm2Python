/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.MPIMethod.Py4J;

import org.mm2python.DataStructures.Builders.MDSParamBuilder;
import org.mm2python.DataStructures.Builders.MDSParameters;
import org.mm2python.DataStructures.Constants;
import org.mm2python.DataStructures.Maps.MDSMap;
import org.mm2python.DataStructures.MetaDataStore;
import org.mm2python.DataStructures.Queues.MDSQueue;
import org.mm2python.MPIMethod.zeroMQ.zeroMQ;
import org.mm2python.UI.reporter;
import org.mm2python.mmDataHandler.DataPathInterface;
import org.mm2python.mmDataHandler.DataMapInterface;
import mmcorej.CMMCore;
import org.micromanager.Studio;
import org.zeromq.ZContext;
import org.zeromq.ZMQ;

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
     * constructor
     * @param mm_: the parent studio object.
     */
    Py4JEntryPoint(Studio mm_){
        mm = mm_;
        mmc = mm_.getCMMCore();
        listener = new Py4JListener();

    }

    /**
     * retrieve the micro-manager GUI object
     * @return : Studio
     */
    public Studio getStudio() {
        return mm;
    }

    /**
     * retrieve the micro-manager CORE object
     * @return : CMMCore
     */
    public CMMCore getCMMCore() {
        return mmc;
    }

    /**
     * retrieve the py4j listener object (for implementing java interfaces from python)
     * @return : Py4JListener
     */
    public Py4JListener getListener() {
        return listener;
    }

    /**
     * current camera's image bitdepth
     * @return : int
     */
    public int getBitDepth() {return (int)Constants.bitDepth;}

    /**
     * current camera's image height
     * @return : int
     */
    public int getHeight() {return (int) Constants.height;}

    /**
     * current camera's image width
     * @return : int
     */
    public int getWidth() {return (int) Constants.width;}

    //============== provide utility functions for system management ====//

    /**
     * utility for resetting the queue of acquired images
     * useful when scripting acquisition
     */
    public void clearQueue(){
        MDSQueue.resetQueue();
    }

    /**
     * query if acquired image queue is empty
     * @return : boolean
     */
    public boolean isQueueEmpty(){
        MDSQueue m = new MDSQueue();
        return m.isQueueEmpty();
    }

    /**
     * clears the MDSMap store
     *  the MDSMap store holds all acquired metadata in no particular order
     */
    public void clearMaps() {
        MDSMap.clearData();
    }

    /**
     * query if MDSMap is empty
     * @return : boolean
     */
    public boolean isMapEmpty() {
        return MDSMap.isEmpty();
    }

    /**
     * clear both MDSQueue and MDSMaps
     */
    public void clearAll() {
        MDSQueue.resetQueue();
        MDSMap.clearData();
    }

    // TESTER METHOD
    public String ping() {
        return "ping from py4j";
    }

    //============== zmq data retrieval methods ==================================//

    /**
     * send the last image (as determined by MDS Queue) out via zeroMQ port
     * todo: remove or refactor this.. remember, meta might not be available!
     */
    public boolean getLastImage() {
        try {
            MetaDataStore mds = this.getLastMeta();
//            Object rawpixels = mds.getImage();
            Object rawpixels = mds.getDataProvider().getImage(mds.getCoord()).getRawPixels();

            zeroMQ.send(rawpixels);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * send the image (as determined by supplied MDS) out via zeroMQ port
     * @param mds
     */
    public boolean getImage(MetaDataStore mds) {
        try {
//            Object rawpixels = mds.getImage();
            Object rawpixels = mds.getDataProvider().getImage(mds.getCoord()).getRawPixels();

            zeroMQ.send(rawpixels);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * send the first image (as determined by MDS Queue) out via zeroMQ port
     */
    public void getFirstImage() {
        MetaDataStore mds = this.getFirstMeta();
        Object rawpixels = mds.getImage();
        zeroMQ.send(rawpixels);
    }

    /**
     * get the zeroMQ port
     * @return : String
     */
    public String getZMQPort() {
        return zeroMQ.getPort();
    }

    public ZMQ.Socket getZMQSocket() {
        return zeroMQ.socket;
    }

    public ZContext getZMQContext() {
        return zeroMQ.getContext();
    }

    //============== Data Map interface methods ====================//
    //== For retrieving MetaDataStore objects and Filenames ======================//

    // Map retrieval interface methods

    public MetaDataStore getLastMeta() {
        MDSQueue m = new MDSQueue();
        if(m.isQueueEmpty()) {
            return null;
        }
        MetaDataStore meta = m.getLastMDS();
        reporter.set_report_area(String.format("\nLastMeta called (t, z): (%d, %d)", meta.getTime(), meta.getZ()));
        return meta;
    }

    public MetaDataStore getFirstMeta() {
        // todo: be sure meta exists
        MDSQueue m = new MDSQueue();
        return m.getFirstMDS();
    }

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

    // =====================================
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

    public void removeFirstMetaByChannelName(String channelName) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        m.removeFirstMDSByParam(params);
    }

    public void removeLastMetaByChannelName(String channelName) throws IllegalAccessException {
        MDSParameters params = new MDSParamBuilder().channel_name(channelName).buildMDSParams();
        MDSQueue m = new MDSQueue();
        m.removeFirstMDSByParam(params);
    }


    // ==============================================================
    // Methods to return metadatastores based on arbitrary parameters //
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
