/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler;

import mm2python.DataStructures.Builders.MDSParamBuilder;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.MetaDataStore;

import java.util.ArrayList;

/**
 *
 * @author bryant.chhun
 */
public interface DataMapInterface {


    // methods to retrieve from map
    //  these call MDSMap
    //  return arraylists of filenames
    //  return arraylists of MDS objects
    // get files by channel_name
    public ArrayList<String> getFilesByChannelName(String channelName) throws IllegalAccessException;

    // get files by channel
    public ArrayList<String> getFilesByChannelIndex(int channelIndex) throws IllegalAccessException;

    // get files by z
    public ArrayList<String> getFilesByZ(int z) throws IllegalAccessException;

    // get files by p
    public ArrayList<String> getFilesByPosition(int pos) throws IllegalAccessException;

    // get files by t
    public ArrayList<String> getFilesByTime(int time) throws IllegalAccessException;

    // get meta by channel_name
    public ArrayList<MetaDataStore> getMetaByChannelName(String channelName) throws IllegalAccessException;

    // get meta by channel
    public ArrayList<MetaDataStore> getMetaByChannelIndex(int channelIndex) throws IllegalAccessException;

    // get meta by z
    public ArrayList<MetaDataStore> getMetaByZ(int z) throws IllegalAccessException;

    // get meta by p
    public ArrayList<MetaDataStore> getMetaByPosition(int pos) throws IllegalAccessException;

    // get meta by t
    public ArrayList<MetaDataStore> getMetaByTime(int time) throws IllegalAccessException;


    //  these call MDSQueue
    //  return a single MDS
    // get last meta by channel_name
    public MetaDataStore getLastMetaByChannelName(String channelName) throws IllegalAccessException;

    // get last meta by channel
    public MetaDataStore getLastMetaByChannelIndex(int channelIndex) throws IllegalAccessException;

    // get last meta by z
    public MetaDataStore getLastMetaByZ(int z) throws IllegalAccessException;

    // get last meta by p
    public MetaDataStore getLastMetaByPosition(int pos) throws IllegalAccessException;

    // get last meta by t
    public MetaDataStore getLastMetaByTime(int time) throws IllegalAccessException;

    // get first meta by channel_name
    public MetaDataStore getFirstMetaByChannelName(String channelName) throws IllegalAccessException;

    // get first meta by channel
    public MetaDataStore getFirstMetaByChannelIndex(int channelIndex) throws IllegalAccessException;

    // get first meta by z
    public MetaDataStore getFirstMetaByZ(int z) throws IllegalAccessException;

    // get first meta by p
    public MetaDataStore getFirstMetaByPosition(int pos) throws IllegalAccessException;

    // get first meta by t
    public MetaDataStore getFirstMetaByTime(int time) throws IllegalAccessException;

    // extras
    //  get parameter builder
    public MDSParamBuilder getParameterBuilder();

    //  get by parameters
    public ArrayList<MetaDataStore> getMetaByParameters(MDSParameters mdsp);

}
