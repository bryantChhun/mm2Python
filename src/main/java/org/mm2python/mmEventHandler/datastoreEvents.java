/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.mmEventHandler;

import net.bytebuddy.build.Plugin;
import org.micromanager.data.*;
import org.mm2python.DataStructures.Maps.RegisteredDatastores;
import org.mm2python.UI.reporter;

import org.micromanager.Studio;

import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import org.mm2python.mmEventHandler.Executor.MainExecutor;
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

    public datastoreEvents(Studio mm_, Datastore data_, String window_name_) {
        mm = mm_;
        data = data_;
        window_name = window_name_;
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
                            data.getImage(c),
                            data.getSummaryMetadata(),
                            "",
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
        RegisteredDatastores.remove(data);
    }
    
    @Subscribe
    public void datastoreIsFrozen(DatastoreFrozenEvent event) {
        reporter.set_report_area("DatastoreFrozen event");
        reporter.set_report_area("num images in this datastore = "+data.getNumImages());
        unRegisterThisDatastore();
    }
    
//    @Subscribe
//    public void datastoreSavePath(DatastoreSavePathEvent event) {
//        reporter.set_report_area("Datastore save path event");
//        reporter.set_report_area("num images in this datastore = "+data.getNumImages());
//        unRegisterThisDatastore();
//    }

    /**
     * NewImageEvent spins off new threads for the datastore
     *  Consider implementing PriorityBlockingQueue
     * @param event
     */
    @Subscribe
    public void executeDatastoreThread(NewImageEvent event){

        try {
            reporter.set_report_area("\nNewImageEvent event detected");
            reporter.set_report_area("\nevent : \t"+event.toString());
            reporter.set_report_area("event Image  : \t" +event.getImage().toString());
            reporter.set_report_area("event coords : \t"+event.getCoords().toString());
            reporter.set_report_area("window name : \t"+window_name);

            String channelgroup = mm.getCMMCore().getChannelGroup();
            String currentchannel = mm.getCMMCore().getCurrentConfig(channelgroup);

            // try retrieving channel name 100 times every 100 us for 1 ms
            if(currentchannel.isEmpty()){
                for(int count=0; count<100; count++){
                    currentchannel = mm.getCMMCore().getCurrentConfig(channelgroup);
                    if(!currentchannel.isEmpty()){
                        break;
                    }
                    Thread.sleep(0,100000);
                }
            }

            if(currentchannel.isEmpty()){
                reporter.set_report_area(true, true, true, "UNABLE TO RETRIEVE CHANNEL FROM CORE");
            }

            SummaryMetadata summary = event.getDatastore().getSummaryMetadata();

            mmExecutor.execute(new datastoreEventsThread(
                    event.getDatastore(),
                    event.getCoords(),
                    event.getImage(),
                    summary,
                    currentchannel,
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
