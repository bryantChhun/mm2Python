/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 *
 * @author bryant.chhun
 */
public class constants {
    
    public static int current_window_count;
    public static String RAMDiskName;

    public static ArrayDeque<String> data_queue;
    public static ArrayDeque<String> metadata_queue;
    
    public static BlockingQueue<String> BQ_data_queue;
    
    public static LinkedBlockingQueue<String> LBQ_data_queue;
    public static LinkedBlockingQueue<String> LBQ_metadata_queue;
    
    
    
    public constants() {
        LBQ_data_queue = new LinkedBlockingQueue<>();
        LBQ_metadata_queue = new LinkedBlockingQueue<>();
        RAMDiskName = "RAM_Disk";
    }
    
    public static void resetAll() {
        LBQ_data_queue = new LinkedBlockingQueue<>();
        LBQ_metadata_queue = new LinkedBlockingQueue<>();
        RAMDiskName = "RAM_Disk";
    }
    
}
