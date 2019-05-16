package mm2python.DataStructures.Queues;

import mm2python.UI.reporter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class PathQueue {
    private static LinkedBlockingQueue<String> liveQueue;
    public static LinkedBlockingQueue<String> filenameByChannel1;

    public PathQueue() {
        filenameByChannel1 = new LinkedBlockingQueue<>();
        liveQueue = new LinkedBlockingQueue<>();
    }

    public static void resetQueues() {
        filenameByChannel1 = new LinkedBlockingQueue<>();
        liveQueue = new LinkedBlockingQueue<>();
    }

    // methods to add/retrieve paths from liveQueue
    public static void putPath(String path) {
        try {
            liveQueue.offer(path, 20, TimeUnit.MILLISECONDS);
        } catch(Exception e) {
            reporter.set_report_area(false, false,
                    e.toString());
        }
    }

    public static boolean nextPathExists() {
        return !liveQueue.isEmpty();
    }

    public static String getNextPath() {
        return liveQueue.peek();
    }

    public static void removeHeadPath() {
        liveQueue.poll();
    }
}
