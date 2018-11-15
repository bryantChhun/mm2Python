/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmDataHandler;

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
//    public String retrieveStorePath(MetaDataStore store_);

    public String retrieveFileByChannelName(String channel_name);

//    public boolean storeByIndexExists(int time, int stage, int z);
//
//    public boolean removeByIndex();

    public boolean storeByChannelNameExists(String channel_name);

//    public boolean removeByChannelName(String channel_name);
    
//    public Boolean isEmpty();
    
}
