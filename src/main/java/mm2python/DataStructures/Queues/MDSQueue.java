package mm2python.DataStructures.Queues;

import mm2python.DataStructures.MetaDataStore;
import mm2python.UI.reporter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Datastructure to represent a linear queue of filenames
 *  filenames represent memory mapped files
 */
public class MDSQueue {

    private static final LinkedBlockingQueue<MetaDataStore> mdsQueue = new LinkedBlockingQueue<>();

    public static void resetQueue() {
        mdsQueue.clear();
    }

    // methods to add/remove MetaDataStores from lbq
    public static void putMDS(MetaDataStore mds_) {
        try {
            mdsQueue.offer(mds_, 20, TimeUnit.MILLISECONDS);
        } catch (InterruptedException iex) {
            reporter.set_report_area(false, false,
                    "interrupted exception placing MDS in queue : "+ iex.toString());
        }
    }

    public static boolean nextMDSExists() {
        return !mdsQueue.isEmpty();
    }

    public static MetaDataStore getNextMDS() {
        return mdsQueue.peek();
    }

    public static void removeHeadMDS() {
        mdsQueue.poll();
    }
}
