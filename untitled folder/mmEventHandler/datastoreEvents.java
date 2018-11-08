/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

import UI.reports;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;
import org.micromanager.data.Coords;
import org.micromanager.data.NewImageEvent;
import org.micromanager.data.DatastoreFrozenEvent;
import org.micromanager.data.DatastoreSavePathEvent;
import org.micromanager.data.NewSummaryMetadataEvent;
import org.micromanager.data.Image;

import com.google.common.eventbus.Subscribe;
import java.util.ArrayList;
import java.util.List;
import org.micromanager.acquisition.AcquisitionManager;
import org.micromanager.acquisition.SequenceSettings;


/**
 * Class that subscribes to datastore events
 *   upon new image events, creates a thread that receives image data and immediately writes to disk
 * @author bryant.chhun
 */
public class datastoreEvents {
    private final Studio mm;
    private final reports report;
    private final Datastore data;
    private final String window_name;
    private Image temp_img;
    private Coords temp_coord;
    private String prefix;
    
    public datastoreEvents(Studio mm_, Datastore data_, String window_name_, reports report_) {
        mm = mm_;
        data = data_;
        window_name = window_name_;
        report = report_;
        System.out.println(String.format("window %s registered", window_name));
    }
    
//    public datastoreEvents(Studio mm_, Datastore data_, reports report_) {
//        mm = mm_;
//        data = data_;
//        report = report_;
//        window_name = "new window";
//    }
    
    /*
     * by default, we do not register the snap/live view window
     */
    public void registerThisDatastore(){
        
        if(!window_name.equals("Snap/Live View")) {
            
            SequenceSettings seq = mm.acquisitions().getAcquisitionSettings();
            prefix = seq.prefix;
            System.out.println("FILE NAME PREFIX = "+prefix);
            
            System.out.println("registering this datastore");
            data.registerForEvents(this);
            
            if(data.getNumImages() > 0){
                Iterable<Coords> itercoords = data.getUnorderedImageCoords();
                for (Coords c: itercoords) {
                    Thread det = new Thread(new datastoreEventsThread(mm, data, c, prefix, report));
                    det.start();
                }
            }
            
        }
        
    }
    
    @Subscribe
    public void monitor_DatastoreFrozenEvent(DatastoreFrozenEvent event) {
        System.out.println("DatastoreFrozen event");
        System.out.println("num images in this datastore = "+data.getNumImages());
        data.unregisterForEvents(this);
    }
    
    @Subscribe
    public void monitor_DatastoreSavePathEvent(DatastoreSavePathEvent event) {
        System.out.println("Datastore save path event");
        System.out.println("num images in this datastore = "+data.getNumImages());
        data.unregisterForEvents(this);
    }
    
    @Subscribe
    public void monitor_NewImageEvent(NewImageEvent event){
        //get coordinates
        try {
            //System.out.println("Datastore new image event");
            //report.set_report_area("Datastore new image event");
                        
            new Thread(new datastoreEventsThread(mm, event.getDatastore(), event.getCoords(), prefix, report)).start();

        } catch (NullPointerException ex) {
            System.out.println("null ptr exception in newImageeventMonitor");
        } catch (Exception ex) {
            System.out.println("new image event exception = "+ex);
        }
    }
    
//    private void getAcquisitionSettings() {
//        SequenceSettings seq = mm.acquisitions().getAcquisitionSettings();
//        int numchans = seq.channels.size();
//        int numslics = seq.slices.size();
//        if(seq.usePositionList) {int numpos = 1;}
//        int numtimepoints = seq.numFrames;
//        
//    }
    
}
