package mm2python.DataStructures.Queues;

import mm2python.DataStructures.Builders.MDSParamObject;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.MetaDataStore;
import mm2python.UI.reporter;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class PathQueue {

    private static final LinkedBlockingDeque<String> pathQueue = new LinkedBlockingDeque<>();

    public static void resetQueue() {
        pathQueue.clear();
    }

    // methods to add/remove MetaDataStores from lbd
    public void putPath(String filename) {
        try {
            boolean result = pathQueue.offer(filename, 20, TimeUnit.MILLISECONDS);
            if(!result){
                reporter.set_report_area(false, false, "failure to offer filepath into PathQueue: "+filename);
            }
        } catch (InterruptedException iex) {
            reporter.set_report_area(false, false,
                    "interrupted exception offering filepath in pathQueue : "+ iex.toString());
        }
    }

    public boolean isPathQueueEmpty() {
        return !pathQueue.isEmpty();
    }

    public String getFirstFile() {
        try {
            return pathQueue.takeFirst();
        } catch(InterruptedException iex) {
            reporter.set_report_area(false, false, "interrrupted while taking first from PathQueue: "+iex.toString());
            return null;
        }
    }

    public String getLastFile() {
        try {
            return pathQueue.takeLast();
        } catch(InterruptedException iex) {
            reporter.set_report_area(false, false, "interrrupted while taking last from PathQueue: "+iex.toString());
            return null;
        }
    }



//    private static LinkedBlockingQueue<String> liveQueue;
////    public static LinkedBlockingQueue<String> filenameByChannel1;
//
//    public PathQueue() {
////        filenameByChannel1 = new LinkedBlockingQueue<>();
//        liveQueue = new LinkedBlockingQueue<>();
//    }
//
//    public static void resetQueues() {
////        filenameByChannel1 = new LinkedBlockingQueue<>();
//        liveQueue = new LinkedBlockingQueue<>();
//    }
//
//    // methods to add/retrieve paths from liveQueue
////    public static void putPath(String path) {
////        try {
////            liveQueue.offer(path, 20, TimeUnit.MILLISECONDS);
////        } catch(Exception e) {
////            reporter.set_report_area(false, false,
////                    e.toString());
////        }
////    }
//
//    public static boolean nextPathExists() {
//        return !liveQueue.isEmpty();
//    }
//
//    public static String getNextPath() {
//        return liveQueue.peek();
//    }
//
//    public static void removeHeadPath() {
//        liveQueue.poll();
//    }
}
