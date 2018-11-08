/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Constants;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.micromanager.data.Coords;

/**
 *
 * @author bryant.chhun
 */
public class constants {
    
    public static int current_window_count;
    public static String RAMDiskName;
    public static String PythonScriptPath;
    
    public static List<Integer> ports;

    public static ArrayDeque<String> data_queue;
    public static ArrayDeque<String> metadata_queue;
    
    public static BlockingQueue<String> BQ_data_queue;
    
    public static LinkedBlockingQueue<String> LBQ_data_queue;
    public static LinkedBlockingQueue<String> LBQ_metadata_queue;
    
    public static HashMap<List<Integer>,String> HM_data;
    
    public static ArrayList<String> data_list;
    
    
    public constants() {
        LBQ_data_queue = new LinkedBlockingQueue<>();
        LBQ_metadata_queue = new LinkedBlockingQueue<>();
        HM_data = new HashMap<>();
        data_list = new ArrayList<>();
        ports = new ArrayList<>();
    }
    
    public static void resetAll() {
        LBQ_data_queue = new LinkedBlockingQueue<>();
        LBQ_metadata_queue = new LinkedBlockingQueue<>();
    }
    
    public static void writeCoordToHashMap(Coords coord, String path){
        List<Integer> meta = new ArrayList<>(4);
        meta.add(coord.getTime());
        meta.add(coord.getStagePosition());
        meta.add(coord.getZ());
        meta.add(coord.getChannel());
        HM_data.put(meta, path);
    }
    
}
