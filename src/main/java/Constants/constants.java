/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import mmDataHandler.MetaDataStore;
import org.micromanager.Studio;

/**
 *
 * @author bryant.chhun
 */
public class constants {

    private static Studio mm;
    
    public static int current_window_count;
    public static String RAMDiskName;
    public static String PythonScriptPath;
    
    public static List<Integer> ports;

    private static LinkedBlockingQueue<String> filenameByChannel1;
//    private static LinkedBlockingQueue<String> filenameByChannel;
//    private static LinkedBlockingQueue<String> filenameByChannel;
//    private static LinkedBlockingQueue<String> filenameByChannel;

    private static HashMap<MetaDataStore, String> MetaStoreToFilenameMap;

    // TODO: we could also replace hashmap with a simple arraylist.  Each index could represent channel index.
    // but this would prevent us from using channel names to retreive data.
    private static HashMap<String, MetaDataStore> chanToMetaStoreMap;

    // every channel has a list of strings.  List position represents next value that was entered.
    //TODO: consider concurrency issue here. Writing memmap image is aysnc event, while populating this array must be done synchronously.
    //TODO: write atomic update to this string...one that checks vs other processes and also checks vs other channels.
    private static HashMap<String, LinkedBlockingQueue<String>> chanToFilenameMap;


    //TODO: replace radio buttons with enums
    public static boolean py4JRadioButton;

    public constants(Studio mm_) {
        mm = mm_;
        filenameByChannel1 = new LinkedBlockingQueue<>();

        //TODO do we want to allow clearing of values by calling the constructor?  or should we clear using methods?
        if(ports == null) {
            ports = new ArrayList<>();
        }
        if(MetaStoreToFilenameMap == null) {
            MetaStoreToFilenameMap = new HashMap<>();
        }
        if(chanToMetaStoreMap == null) {
            chanToMetaStoreMap = new HashMap<>();
        }
        if(chanToFilenameMap == null) {
            chanToFilenameMap = new HashMap<>();
        }

        py4JRadioButton = false;
    }
    
    public static void resetAll() {
//        ports = new ArrayList<>();
//        py4JRadioButton = false;
        MetaStoreToFilenameMap = new HashMap<>();
        chanToMetaStoreMap = new HashMap<>();
        chanToFilenameMap = new HashMap<>();
        filenameByChannel1 = new LinkedBlockingQueue<>();
    }

    // Every MetaDataStore is kept.  Its corresponding mmap filename is registered.
    public static void putMetaStoreToFilenameMap(MetaDataStore store_, String filename) {
        MetaStoreToFilenameMap.put(store_, filename);
    }

    // map should contain only ONE instance of each channel
    public static void putChanToMetaStoreMap(String channel, MetaDataStore store_) {
        chanToMetaStoreMap.put(channel, store_);
    }

    public static void removeChanToMetaStoreMap(String channel) {
        chanToMetaStoreMap.remove(channel);
    }

    // map should contain only ONE instance of each channel
    public static void putChanToFilenameMap(String channel, String filename) {
        try {
            if(chanToFilenameMap.containsKey(channel)){
                filenameByChannel1 = chanToFilenameMap.get(channel);
            } else {
                filenameByChannel1 = new LinkedBlockingQueue<>();
            }
            filenameByChannel1.offer(filename, 100, TimeUnit.MILLISECONDS);
            System.out.println("placing filename by Channel, size = "+filenameByChannel1.size());
            chanToFilenameMap.put(channel, filenameByChannel1);
        } catch (InterruptedException iEX){
            mm.logs().logMessage("interrupted exception while writing to LBQ filename by channel "+iEX);
        }
        mm.logs().logMessage(String.format("writing to chan-filename map: %s /t %s", channel, filename));
    }

    public static void removeChanToFilenameMap(String channel) {
        filenameByChannel1 = chanToFilenameMap.get(channel);
        filenameByChannel1.poll();
        System.out.println("REMOVING FILENAME MAP = "+chanToFilenameMap.size());
        chanToFilenameMap.put(channel, filenameByChannel1);
        mm.logs().logMessage(String.format("removing top of chan-filename map: %s", channel));
    }

    public static String getNextFileForChannel(String channel) {
        LinkedBlockingQueue<String> filenames = chanToFilenameMap.get(channel);
        System.out.println("getting file for channel.  Filename is null? = "+filenames);
        System.out.println("length of LBQ = "+filenames.size());
        return filenames.peek();
    }

    public static boolean nextImageExists(String channel) {
        // check that both key and corresponding LBQ are not empty
        return chanToFilenameMap.containsKey(channel) &&
                chanToFilenameMap.get(channel).size() != 0;
    }
    
}
