/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayDeque;
import Constants.constants;
import Exceptions.NoImageException;
import UI.reports;
import mmDataHandler.memMapImage;
import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.data.NewImageEvent;

/**
 * immediately writes data to outputs based on the type of backend.
 *  writes to py4j arraydeque object new metadata AND data
 * @author bryant.chhun
 */
public class datastoreEventsThread implements Runnable {
    private final Studio mm;
    private final Image temp_img;
    private final Coords temp_coord;
    private final String prefix;
    
    public datastoreEventsThread(Studio mm_, Datastore data_, Coords c_, String prefix_, reports report) {
        mm = mm_;
        temp_img = data_.getImage(c_);
        temp_coord = c_;
        prefix = prefix_;
    }
    
    @Override
    public void run() {
        
//        String filename = String.format("/Volumes/RAM_Disk/JavaPlugin_temp_folder/%s_t%03d_p%03d_z%02d_c%02d.dat", 
//                prefix, temp_coord.getTime(), temp_coord.getStagePosition(), temp_coord.getZ(), temp_coord.getChannel());
        
        String filename = "/Volumes/Ram Disk/JavaPlugin_temp_folder/test_ONE_FILE.dat";
        
        try {
            new memMapImage(temp_img, temp_coord, prefix, filename);
        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in datastoreEvents Thread");
        } catch (NoImageException ex) {
            System.out.println(ex);
        }
        
    }
    
}
