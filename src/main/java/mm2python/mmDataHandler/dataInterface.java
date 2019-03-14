/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler;

import mm2python.DataStructures.MetaDataStore;

/**
 *
 * @author bryant.chhun
 */
public interface dataInterface {

//    public String popData();
//
//    public String popMetaData();
//
//    public String viewData();
//
//    public String viewMetaData();

//    public boolean doesStoreExist(MetaDataStore store_);
//
//    public String getStorePath(MetaDataStore store_);

    public String getFile(String channel_name);

    public String getFile(MetaDataStore store);

    public MetaDataStore getStore(String channel_name);

//    public boolean storeByIndexExists(int time, int stage, int z);
//
//    public boolean removeByIndex();

    public boolean storeExists(String channel_name);

    public boolean fileExists(String channel_name);

    public boolean fileExists(MetaDataStore store);

//    public boolean removeByChannelName(String channel_name);
    
//    public Boolean isEmpty();
    
}
