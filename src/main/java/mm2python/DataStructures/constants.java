/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.DataStructures;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import mm2python.UI.reporter;
import mm2python.DataStructures.Exceptions.OSTypeException;
import sun.awt.OSInfo;

/**
 *
 * @author bryant.chhun
 */
public class constants {
    /**
     * Constants contains data maps for reference by the messenger service
     *  It also contains methods for OS-level constants, like temp file paths.
     */

    public static String tempFilePath;

    public static List<Integer> ports;

    private static LinkedBlockingQueue<String> liveQueue;

    private static LinkedBlockingQueue<String> filenameByChannel1;

    private static HashMap<MetaDataStore, String> MetaStoreToFilenameMap;

    // maps
    private static HashMap<String, MetaDataStore> chanToMetaStoreMap;

    private static HashMap<String, LinkedBlockingQueue<String>> chanToFilenameMap;

    public static boolean py4JRadioButton;

    public constants() {
        filenameByChannel1 = new LinkedBlockingQueue<>();
        liveQueue = new LinkedBlockingQueue<>();

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

    public static String getOS() throws OSTypeException {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return("win");
        } else if (OS.indexOf("mac") >= 0) {
            return("mac");
        } else {
            throw new OSTypeException("OS type is not implemented.  Must use Mac or Windows");
        }
    }
    
    public static void resetAll() {
        MetaStoreToFilenameMap = new HashMap<>();
        chanToMetaStoreMap = new HashMap<>();
        chanToFilenameMap = new HashMap<>();
        filenameByChannel1 = new LinkedBlockingQueue<>();
        liveQueue = new LinkedBlockingQueue<>();
    }

    // map population and depopulation methods

    /**
     * map a metastore to its corresponding memory-mapped file
     * @param store_ MetaDataStore
     * @param filename memory mapped file path
     */
    public static void putMetaStoreToFilenameMap(MetaDataStore store_, String filename) {
        MetaStoreToFilenameMap.put(store_, filename);
    }

    public static void putChanToMetaStoreMap(String channel, MetaDataStore store_) {
        chanToMetaStoreMap.put(channel, store_);
    }

    public static void removeChanToMetaStoreMap(String channel) {
        chanToMetaStoreMap.remove(channel);
    }

    public static void putChanToFilenameMap(String channel, String filename) {
        try {
            if(chanToFilenameMap.containsKey(channel)){
                filenameByChannel1 = chanToFilenameMap.get(channel);
            } else {
                filenameByChannel1 = new LinkedBlockingQueue<>();
            }

            if(!filenameByChannel1.offer(filename, 100, TimeUnit.MILLISECONDS)){
                reporter.set_report_area(false, false, "LBQ timeout when offering new filename "+filename);
            }

            reporter.set_report_area(false, false,
                    String.format("placing filename by Channel: (chan, size) = (%s, %s)",
                    channel,
                    filenameByChannel1.size()));

            chanToFilenameMap.put(channel, filenameByChannel1);
        } catch (Exception iEX){
            reporter.set_report_area(false, false,
                    iEX.toString());
        }
    }

    public static void removeChanToFilenameMap(String channel) {
        filenameByChannel1 = chanToFilenameMap.get(channel);
        filenameByChannel1.poll();
        reporter.set_report_area(true, false,
                String.format("Removing filename from channel: (chan, new size) = (%s, %s) ",
                channel,
                filenameByChannel1.size()));
        chanToFilenameMap.put(channel, filenameByChannel1);
    }


    // Retrieval methods
    public static String getNextFileForChannel(String channel) {
        LinkedBlockingQueue<String> filenames = chanToFilenameMap.get(channel);
        reporter.set_report_area(true, false,
                String.format("filename size at Peek: %s %s %s", channel, filenames.size(), filenames));
        return filenames.peek();
    }

    public static String getFileFromMetaStore(MetaDataStore store_) {
        return MetaStoreToFilenameMap.get(store_);
    }

    public static MetaDataStore getStore(String channel) {
        return chanToMetaStoreMap.get(channel);
    }


    // Data check methods
    public static boolean nextImageExists(String channel) {
        // check that both key and corresponding LBQ are not empty
        return chanToFilenameMap.containsKey(channel) &&
                chanToFilenameMap.get(channel).size() != 0;
    }

    public static boolean nextImageExists(MetaDataStore store) {
        // check that both key and corresponding LBQ are not empty
        return MetaStoreToFilenameMap.containsKey(store);
    }

    public static void putNextImage(String path) {
        try {
            liveQueue.put(path);
        } catch(Exception e) {
            reporter.set_report_area(false, false,
                    e.toString());
        }
    }

    public static boolean nextImageExists() {
        return !liveQueue.isEmpty();
    }

    public static String getNextImage() {
        return liveQueue.poll();
    }
}
