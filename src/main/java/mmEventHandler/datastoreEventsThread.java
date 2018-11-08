/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

import Constants.constants;
import mmDataHandler.Exceptions.NoImageException;
import UI.reports;
import messenger.Py4J.Exceptions.Py4JListenerException;
import messenger.Py4J.Py4JListener;
import mmDataHandler.memMapImage;
import org.micromanager.Studio;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;

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
    private final boolean snaplive;
    private String filename;
    private final Py4JListener notifylistener;
    
    public datastoreEventsThread(Studio mm_, Datastore data_, Coords c_, String prefix_, Boolean snaplive_, reports report) {
        mm = mm_;
        temp_img = data_.getImage(c_);
        temp_coord = c_;
        prefix = prefix_;
        snaplive = snaplive_;
        notifylistener = new Py4JListener();
    }
    
    @Override
    public void run() {
        
        //Check for live vs MDA image
        if(snaplive) {
            filename = constants.RAMDiskName+"Snap-Live-Stream.dat";
        } else {
            filename = String.format(constants.RAMDiskName+"/%s_t%03d_p%03d_z%02d_c%02d.dat", 
                prefix, temp_coord.getTime(), temp_coord.getStagePosition(), temp_coord.getZ(), temp_coord.getChannel());
        }
        
        // write memory mapped image 
        try {
            System.out.println("FILENAME = "+filename);
            //filename = "/Users/bryant.chhun/Desktop/untitled_folder/Snap-Live-Stream.dat";
            memMapImage out = new memMapImage(temp_img, temp_coord, filename);
            out.writeToMemMap();
        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in datastoreEvents Thread");
        } catch (NoImageException ex) {
            System.out.println(ex);
        }
        
        // notify Py4J listeners
        try {
            System.out.println("notifying py4j listeners");
            notifylistener.notifyAllListeners();
        } catch (Py4JListenerException ex) {
            System.out.println("Exception in datastore events thread while notifying Py4J listeners: "+ex.toString());
        } catch (Exception ex) {
            System.out.println("General Exception while notifying Py4J listeners: "+ex.toString());
        }
        
    }
    
    
    
}
