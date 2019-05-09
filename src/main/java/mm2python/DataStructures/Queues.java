package mm2python.DataStructures;

import mm2python.UI.reporter;

import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class Queues {
    private static LinkedBlockingQueue<String> liveQueue;
    static LinkedBlockingQueue<String> filenameByChannel1;

//    static ArrayList<String> Dynamic

    public Queues() {
        filenameByChannel1 = new LinkedBlockingQueue<>();
        liveQueue = new LinkedBlockingQueue<>();
    }

    public static void resetQueues() {
        filenameByChannel1 = new LinkedBlockingQueue<>();
        liveQueue = new LinkedBlockingQueue<>();
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
