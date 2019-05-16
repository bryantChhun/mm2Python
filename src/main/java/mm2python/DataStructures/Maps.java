package mm2python.DataStructures;

import mm2python.UI.reporter;

import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class Maps {
    private static HashMap<MetaDataStore, String> MetaStoreToFilenameMap;
    private static HashMap<String, MetaDataStore> chanToMetaStoreMap;
    private static HashMap<String, LinkedBlockingQueue<String>> chanToFilenameMap;

    public Maps() {
        if(MetaStoreToFilenameMap == null) {
            MetaStoreToFilenameMap = new HashMap<>();
        }
        if(chanToMetaStoreMap == null) {
            chanToMetaStoreMap = new HashMap<>();
        }
        if(chanToFilenameMap == null) {
            chanToFilenameMap = new HashMap<>();
        }
    }

    public static void putChanToFilenameMap(String channel, String filename) {
        try {
            if(chanToFilenameMap.containsKey(channel)){
                PathQueue.filenameByChannel1 = chanToFilenameMap.get(channel);
            } else {
                PathQueue.filenameByChannel1 = new LinkedBlockingQueue<>();
            }

            if(!PathQueue.filenameByChannel1.offer(filename, 100, TimeUnit.MILLISECONDS)){
                reporter.set_report_area(false, false, "LBQ timeout when offering new filename "+filename);
            }

            reporter.set_report_area(false, false,
                    String.format("placing filename by Channel: (chan, size) = (%s, %s)",
                    channel,
                    PathQueue.filenameByChannel1.size()));

            chanToFilenameMap.put(channel, PathQueue.filenameByChannel1);
        } catch (Exception iEX){
            reporter.set_report_area(false, false,
                    iEX.toString());
        }
    }

    public static void removeChanToFilenameMap(String channel) {
        PathQueue.filenameByChannel1 = chanToFilenameMap.get(channel);
        PathQueue.filenameByChannel1.poll();
        reporter.set_report_area(true, false,
                String.format("Removing filename from channel: (chan, new size) = (%s, %s) ",
                channel,
                PathQueue.filenameByChannel1.size()));
        chanToFilenameMap.put(channel, PathQueue.filenameByChannel1);
    }

    // Retrieval methods
    public static String getNextFileForChannel(String channel) {
        LinkedBlockingQueue<String> filenames = chanToFilenameMap.get(channel);
        reporter.set_report_area(true, false,
                String.format("filename size at Peek: %s %s %s", channel, filenames.size(), filenames));
        return filenames.peek();
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


    // =====================
    // MetaDataStore methods
    // =====================
    public static String getFileFromMetaStore(MetaDataStore store_) {
        return MetaStoreToFilenameMap.get(store_);
    }

    public static MetaDataStore getStore(String channel) {
        return chanToMetaStoreMap.get(channel);
    }

    public static void putMetaStoreToFilenameMap(MetaDataStore store_, String filename) {
        MetaStoreToFilenameMap.put(store_, filename);
    }

    public static void putChanToMetaStoreMap(String channel, MetaDataStore store_) {
        chanToMetaStoreMap.put(channel, store_);
    }

    public static void removeChanToMetaStoreMap(String channel) {
        chanToMetaStoreMap.remove(channel);
    }
}
