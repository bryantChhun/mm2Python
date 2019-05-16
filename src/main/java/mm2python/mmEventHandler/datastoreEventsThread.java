/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.DataStructures.*;
import mm2python.DataStructures.Builders.MDSBuilder;
import mm2python.DataStructures.Maps.MDSMap;
import mm2python.DataStructures.Queues.CircularFilenameQueue;
import mm2python.DataStructures.Queues.MDSQueue;
import mm2python.DataStructures.Queues.PathQueue;
import mm2python.mmDataHandler.Exceptions.NoImageException;
import mm2python.messenger.Py4J.Exceptions.Py4JListenerException;
import mm2python.messenger.Py4J.Py4JListener;
import mm2python.mmDataHandler.memMapImage;
import mm2python.UI.reporter;
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
    private final Image temp_img;
    private final Coords coord;
    private final String prefix;
    private String filename;
    private final String window_name;
    private final String[] channel_names;
    private final String channel_name;

    private final MetaDataStore mds;
    private final MDSMap fds;

    /**
     * Executes sequence of tasks up run by executor:
     *  1) assigns a filename based on metadata
     *  2) writes the file as memory mapped image
     *  3) registers file metadata in static repository
     *  4) notifies any py4j listeners
     * @param mm_ : Studio object inherited from UI
     * @param data_ : Most recent Datastore
     * @param c_ : coordinates
     * @param window_name_ : window from which this datastore originates
     */
    datastoreEventsThread(Studio mm_, Datastore data_, Coords c_, String window_name_) {
        // assigning parameters
        temp_img = data_.getImage(c_);
        coord = c_;

        window_name = window_name_;
        prefix = mm_.acquisitions().getAcquisitionSettings().prefix;
        channel_names = data_.getSummaryMetadata().getChannelNames();
        channel_name = channel_names[coord.getChannel()];

        // if using Circular MMapQueue, pick a filename
        if(Constants.getFixedMemMap()){
            filename = CircularFilenameQueue.getNextFilename();
        } else {
            filename = assignFileName();
        }

        //create MetaDataStore for this object
        mds = makeMDS();
        fds = new MDSMap();
    }
    
    @Override
    public void run() {

        // Write memory mapped image
        writeToMemMap();

        // Write to concurrent hashmap
        writeToHashMap();

        // write filename to queue
        // write MetaDataStore to queue
        writeToQueues();
        
        // notify Listeners
        notifyListeners();
        
    }

    private String assignFileName() {
        //Check for live vs MDA image, assign a filename
        if(window_name.equals("Snap/Live View")) {
            filename = Constants.tempFilePath +"/Snap-Live-Stream.dat";
            reporter.set_report_area(false, false, "datastoreEventsThread: SNAPLIVE = "+filename);
        } else {

            filename = String.format(Constants.tempFilePath +"/%s_t%03d_p%03d_z%02d_c%02d.dat",
                    prefix, coord.getTime(), coord.getStagePosition(), coord.getZ(), coord.getChannel());
            reporter.set_report_area(false, false, "datastoreEventsThread MDA = "+filename);
        }
        return filename;
    }

    private MetaDataStore makeMDS() {
        try {
            return new MDSBuilder().position(coord.getStagePosition()).time(coord.getTime()).z(coord.getZ()).channel(coord.getChannel()).
                    xRange(temp_img.getWidth()).yRange(temp_img.getHeight()).bitDepth(temp_img.getBytesPerPixel() * 8).
                    prefix(prefix).windowname(window_name).channel_name(channel_names[coord.getChannel()]).
                    filepath(filename).buildMDS();
        } catch(IllegalAccessException ilex){
            reporter.set_report_area(false, false, String.format("Fail to build MDS for c%d, z%d, p%d, t%d, filepath=%s",
                    coord.getChannel(), coord.getZ(), coord.getStagePosition(), coord.getTime(), filename));
        }
        return null;
    }

    private void writeToMemMap() {
        try {
            reporter.set_report_area(false, false, "datastoreEventsThread: writing memmap");
            memMapImage out = new memMapImage(temp_img, filename);
            out.writeToMemMap();
        } catch (NullPointerException ex) {
            reporter.set_report_area(true, false, "null ptr exception in datastoreEvents Thread");
        } catch (NoImageException ex) {
            reporter.set_report_area(true, false, ex.toString());
        }
    }

    private void writeToHashMap() {
        try {
            fds.putMDS(mds);
            reporter.set_report_area(false, false, "writing chan to filename map = ("+filename+", "+channel_name+")" );
        } catch (Exception ex) {
            reporter.set_report_area(false, false, ex.toString());
        }
    }

    private void writeToQueues() {
        try {
            PathQueue.putPath(filename);
            MDSQueue.putMDS(mds);
        } catch (NullPointerException ex) {
            reporter.set_report_area(false, false, "null ptr exception writing to LinkedBlockingQueue");
        } catch (Exception ex) {
            reporter.set_report_area(false, false, ex.toString());
        }
    }

    private void notifyListeners() {
        try {
            reporter.set_report_area(true, false, "notifying py4j listeners");
            if (Constants.py4JRadioButton) {
                Py4JListener.notifyAllListeners();
            }
        } catch (Py4JListenerException ex) {
            reporter.set_report_area(true, false, "Exception in datastore events thread while notifying Py4J listeners: "+ex.toString());
        } catch (Exception ex) {
            reporter.set_report_area(true, false, "General Exception while notifying Py4J listeners: "+ex.toString());
        }
    }


    
}
