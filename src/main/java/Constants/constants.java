/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mmDataHandler.MetaDataStore;

/**
 *
 * @author bryant.chhun
 */
public class constants {
    
    public static int current_window_count;
    public static String RAMDiskName;
    public static String PythonScriptPath;
    
    public static List<Integer> ports;
    


    public static HashMap<MetaDataStore, String> MetaDataToFileMap;

    public static HashMap<String, MetaDataStore> chanToStoreMap;

    public static HashMap<String, String> chanToFileMap;
    
//    private static HashMap<List<Integer>,String> HM_data;
//    private static ArrayList<String> data_list;
    //    public static ArrayDeque<String> data_queue;
//    public static ArrayDeque<String> metadata_queue;

//    public static BlockingQueue<String> BQ_data_queue;
//public static LinkedBlockingQueue<String> LBQ_data_queue;
//    public static LinkedBlockingQueue<String> LBQ_metadata_queue;


    //TODO: replace radio buttons with enums
    public static boolean py4JRadioButton;
    
    
    public constants() {
//        LBQ_data_queue = new LinkedBlockingQueue<>();
//        LBQ_metadata_queue = new LinkedBlockingQueue<>();
//        HM_data = new HashMap<>();
//        data_list = new ArrayList<>();
        if(ports == null) {
            ports = new ArrayList<>();
        }
        if(MetaDataToFileMap == null) {
            MetaDataToFileMap = new HashMap<>();
        }
        if(chanToStoreMap == null) {
            chanToStoreMap = new HashMap<>();
        }
        if(chanToFileMap == null) {
            chanToFileMap = new HashMap<>();
        }

        py4JRadioButton = false;
    }
    
    public static void resetAll() {
//        LBQ_data_queue = new LinkedBlockingQueue<>();
//        LBQ_metadata_queue = new LinkedBlockingQueue<>();
        ports = new ArrayList<>();
        py4JRadioButton = false;
        MetaDataToFileMap = new HashMap<>();
        chanToStoreMap = new HashMap<>();
    }

    public static void putMetaDataMap(MetaDataStore store_, String filename) {
        MetaDataToFileMap.put(store_, filename);
    }

    public static void putChanStoreMap(String channel, MetaDataStore store_) {
        chanToStoreMap.put(channel, store_);
    }

    public static void removeChanStoreMap(String channel) {
        chanToStoreMap.remove(channel);
    }

    public static void putChanToFileMap(String channel, String filename) {
        chanToFileMap.put(channel, filename);
    }

    public static void removeChanToFileMap(String channel) {
        chanToFileMap.remove(channel);
    }



    // wont' work as we need to hash each of the indexes
    // this simply stores a list of each index in the hash
//    public static void writeCoordToHashMap(Coords coord, String path){
//        List<Integer> meta = new ArrayList<>(4);
//        meta.add(coord.getTime());
//        meta.add(coord.getStagePosition());
//        meta.add(coord.getZ());
//        meta.add(coord.getChannel());
//        HM_data.put(meta, path);
//    }
    
}
