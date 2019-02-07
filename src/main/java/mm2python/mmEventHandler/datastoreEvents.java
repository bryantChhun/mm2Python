/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.UI.reporter;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;
import org.micromanager.data.Coords;
import org.micromanager.data.NewImageEvent;
import org.micromanager.data.DatastoreFrozenEvent;
import org.micromanager.data.DatastoreSavePathEvent;

import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import mm2python.mmEventHandler.Executor.main_executor;
import org.micromanager.acquisition.SequenceSettings;


/**
 * Class that subscribes to datastore events
 *   upon new image events, creates a thread that receives image data and immediately writes to disk
 * @author bryant.chhun
 */
public class datastoreEvents {
    private final Studio mm;
    private final Datastore data;
    private final String window_name;
    private String prefix;
    private final ExecutorService mmExecutor;
    
    public datastoreEvents(Studio mm_, Datastore data_, String window_name_) {
        mm = mm_;
        data = data_;
        window_name = window_name_;
        mmExecutor = main_executor.getExecutor();
        reporter.set_report_area(true, false, String.format("window %s registered", window_name));
    }
    
    public void registerThisDatastore(){
        
        if(!window_name.equals("Snap/Live View")) {
            
            SequenceSettings seq = mm.acquisitions().getAcquisitionSettings();
            prefix = seq.prefix;

            data.registerForEvents(this);
            
            // writes any data existing in window before draw.
            if(data.getNumImages() > 0){
                Iterable<Coords> itercoords = data.getUnorderedImageCoords();
                for (Coords c: itercoords) {
                    mmExecutor.execute(new datastoreEventsThread(mm, data, c, prefix, window_name) );
                }
            }
            
        } else {
            data.registerForEvents(this);
        }
        
    }
    
    @Subscribe
    public void monitor_DatastoreFrozenEvent(DatastoreFrozenEvent event) {
        reporter.set_report_area(true, false, "DatastoreFrozen event");
        reporter.set_report_area(true, false, "num images in this datastore = "+data.getNumImages());
        data.unregisterForEvents(this);
    }
    
    @Subscribe
    public void monitor_DatastoreSavePathEvent(DatastoreSavePathEvent event) {
        reporter.set_report_area(true, false, "Datastore save path event");
        reporter.set_report_area(true, false, "num images in this datastore = "+data.getNumImages());
        data.unregisterForEvents(this);
    }

    /**
     * NewImageEvent spins off new threads for the datastore
     *  Consider implementing PrioerityBlockingQueue
     * @param event
     */
    @Subscribe
    public void monitor_NewImageEvent(NewImageEvent event){
        try {
            mmExecutor.execute(new datastoreEventsThread(mm, event.getDatastore(), event.getCoords(), prefix, window_name));
        } catch (NullPointerException ex) {
            reporter.set_report_area(true, false, "null ptr exception in newImageeventMonitor");
        } catch (Exception ex) {
            reporter.set_report_area(true, false, "new image event exception = "+ex);
        }
    }
    
}
