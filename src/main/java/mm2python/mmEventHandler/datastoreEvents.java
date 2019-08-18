/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.DataStructures.Maps.RegisteredDatastores;
import mm2python.UI.reporter;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;
import org.micromanager.data.Coords;
import org.micromanager.data.NewImageEvent;
import org.micromanager.data.DatastoreFrozenEvent;
import org.micromanager.data.DatastoreSavePathEvent;

import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import mm2python.mmEventHandler.Executor.MainExecutor;
import org.micromanager.acquisition.SequenceSettings;

import java.io.StringWriter;
import java.io.PrintWriter;

/**
 * Class that subscribes to datastore events
 *   upon new image events, creates a datastoreEventsThread
 * @author bryant.chhun
 */
public class datastoreEvents {
    private final Studio mm;
    private final Datastore data;
    private final String window_name;
    private String prefix_;
    private static final ExecutorService mmExecutor;

    static {
        mmExecutor = MainExecutor.getExecutor();
    }

    datastoreEvents(Studio mm_, Datastore data_, String window_name_) {
        mm = mm_;
        data = data_;
        window_name = window_name_;
//        registerThisDatastore();
        reporter.set_report_area(String.format("window %s registered", window_name));
    }

    /**
     * registers datastore for events
     * puts this datastore in concurrent map for later unregistration
     */
    public void registerThisDatastore(){

        SequenceSettings seq = mm.acquisitions().getAcquisitionSettings();
        if(seq.prefix != null) {
            prefix_ = seq.prefix;
        } else {
            prefix_ = "prefix_";
        }

        // if not snap/live view, write data that exists before window is drawn
//        if(window_name.equals("Snap/Live View")) {
//            data.registerForEvents(this);
//            reporter.set_report_area(true, true, false, "datastoreEvent: registered this SNAPLIVE datastore, prefix: "+data.toString()+" "+prefix_);
//
//        } else {

        data.registerForEvents(this);
        RegisteredDatastores.put(data, this);

        reporter.set_report_area("datastoreEvent: registered this MDA datastore, prefix: "+data.toString()+" "+prefix_);

        // writes any data existing in window before draw.
        if(data.getNumImages() > 0){
            Iterable<Coords> itercoords = data.getUnorderedImageCoords();
            for (Coords c: itercoords) {
                reporter.set_report_area("datastoreEvent: existing images in datastore before window rendered: "+c.toString());
                try {
                    mmExecutor.execute(new datastoreEventsThread(data,
                            c,
                            mm.getCMMCore().getCurrentConfig("Channel"),
                            mm.acquisitions().getAcquisitionSettings().prefix,
                            window_name)
                    );
                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }
        }
        
    }

    public void unRegisterThisDatastore() {
        data.unregisterForEvents(this);
    }
    
    @Subscribe
    public void monitor_DatastoreFrozenEvent(DatastoreFrozenEvent event) {
        reporter.set_report_area("DatastoreFrozen event");
        reporter.set_report_area("num images in this datastore = "+data.getNumImages());
        data.unregisterForEvents(this);
    }
    
    @Subscribe
    public void monitor_DatastoreSavePathEvent(DatastoreSavePathEvent event) {
        reporter.set_report_area("Datastore save path event");
        reporter.set_report_area("num images in this datastore = "+data.getNumImages());
        data.unregisterForEvents(this);
    }

    /**
     * NewImageEvent spins off new threads for the datastore
     *  Consider implementing PriorityBlockingQueue
     * @param event
     */
    @Subscribe
    public void monitor_NewImageEvent(NewImageEvent event){
        long start = System.nanoTime();

        try {
            reporter.set_report_area("\nNewImageEvent event detected");
            reporter.set_report_area("\nevent : \t"+event.toString());
            reporter.set_report_area("event Image  : \t" +event.getImage().toString());
            reporter.set_report_area("event datastore : \t"+event.getDatastore().toString());
            reporter.set_report_area("event coords : \t"+event.getCoords().toString());
            reporter.set_report_area("window name : \t"+window_name);
//            reporter.set_report_area(true, true, true,"event Image in Datastore at coords : \t"+event.getDatastore().getImage(event.getCoords()).toString());

//            mmExecutor.execute(new datastoreEventsThread(mm, event.getDatastore(), event.getCoords(), window_name));
            mmExecutor.execute(new datastoreEventsThread(
                    event.getDatastore(),
                    event.getCoords(),
                    mm.getCMMCore().getCurrentConfig("Channel"),
                    mm.acquisitions().getAcquisitionSettings().prefix,
                    window_name)
            );

        } catch (NullPointerException ex) {
            reporter.set_report_area("null ptr exception in newImageeventMonitor");

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);

            reporter.set_report_area("\n"+sw.toString());
        } catch (Exception ex) {
            reporter.set_report_area("new image event exception = "+ex);
        }
    }
    
}
