/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.DataStructures.*;
import mm2python.DataStructures.Builders.MDSBuilder;
import mm2python.DataStructures.Maps.MDSMap;
import mm2python.DataStructures.Queues.FixedMemMapReferenceQueue;
import mm2python.DataStructures.Queues.DynamicMemMapReferenceQueue;
import mm2python.DataStructures.Queues.MDSQueue;
import mm2python.mmDataHandler.Exceptions.NoImageException;
import mm2python.MPIMethod.Py4J.Exceptions.Py4JListenerException;
import mm2python.MPIMethod.Py4J.Py4JListener;
import mm2python.mmDataHandler.memMapFromBuffer;
import mm2python.UI.reporter;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;

import java.nio.MappedByteBuffer;

import mm2python.DataStructures.MovingAverage;
// todo: add more metadata values: file index, buffer_position, length

/**
 * immediately writes data to outputs based on the type of backend.
 *  writes to py4j arraydeque object new metadata AND data
 * @author bryant.chhun
 */
public class datastoreEventsThread implements Runnable {
    // variables from micro-manager
    private final Image temp_img;
    private final Coords coord;
    private final String prefix;
    private final String window_name;
    private String channel_name;

    // variables for MetaDataStorage
    private final MetaDataStore mds;
    private final MDSMap fds;
    private final MDSQueue mq;

    // variables taken from circular queue
    private MappedByteBuffer buffer;
    private String filename;
    private int buffer_position = 0;

    /**
     * Executes sequence of tasks run by executor:
     *  1) assigns a filename based on metadata
     *  2) writes the file as memory mapped image
     *  3) registers file metadata in static repository (queue and map)
     *  4) notifies any py4j listeners
     * @param data_ : Datastore associated with triggered event
     * @param c_ : Coords associated with triggered event
     * @param channel_name_ : configuration channel name
     * @param prefix_ : MDA experiment prefix
     * @param window_name_ : display window that triggered event
     */
    datastoreEventsThread(Datastore data_,
                          Coords c_,
                          String channel_name_,
                          String prefix_,
                          String window_name_) {
        // assigning parameters
        temp_img = data_.getImage(c_);
        coord = c_;
        window_name = window_name_;
        prefix = prefix_;

        // if using MDA, SummaryMetadata contains channel names
        // if using script, assume "Channel" group is the channel name
        try {
            final String[] channel_names = data_.getSummaryMetadata().getChannelNames();
            channel_name = channel_names[coord.getChannel()];
        } catch (NullPointerException nex) {
            channel_name = channel_name_;
            reporter.set_report_area("\nscript acquisition detected, channel name = "+channel_name);
        } catch (ArrayIndexOutOfBoundsException arrayex) {
            channel_name=channel_name_;
        }

        if(channel_name == null){
            reporter.set_report_area("\nNO CHANNEL NAME FOUND\n");
        }

        // assign filename based on type of queue or data source
        filename = getFileName();

        //
        if(Constants.getFixedMemMap()){
            buffer = FixedMemMapReferenceQueue.getNextBuffer();
            buffer_position = 0;
        } else {
            buffer = DynamicMemMapReferenceQueue.getCurrentBuffer();
            buffer_position = DynamicMemMapReferenceQueue.getCurrentPosition();
        }

        //create MetaDataStore for this object
        mds = makeMDS();
        fds = new MDSMap();

        mq = new MDSQueue();
//        reporter.set_report_area("constructed datastoreEventsThread");
    }
    
    @Override
    public void run() {

        long start = System.nanoTime();

        // Write memory mapped image
        writeToMemMap();

        long stop = System.nanoTime();

        // Write to concurrent hashmap
        writeToHashMap();

        // write filename to queue
        // write MetaDataStore to queue
        writeToQueues();

        // notify Listeners
//        notifyListeners();


        if(stop-start < Constants.min && Constants.init > 5) {
            Constants.min = (stop-start);
        }
        if(stop-start >Constants.max && Constants.init > 5) {
            Constants.max = (stop-start);
        }
        Constants.init += 1;

        reporter.set_report_area("Time elapsed for writes (ns): "+Long.toString(stop-start));
//        reporter.set_report_area("Max time elapsed for run (ns):"+Long.toString(Constants.max));
//        reporter.set_report_area("Min time elapsed for run (ns):"+Long.toString(Constants.min));
        reporter.set_report_area("max average over 10 frame window (ns):"+Long.toString(MovingAverage.next(stop-start)));

    }

    private String getFileName() {
        //Check for live vs MDA image, assign a filename
//        if(window_name.equals("Snap/Live View")) {
//            filename = Constants.tempFilePath +"/Snap-Live-Stream.dat";
//            reporter.set_report_area("datastoreEventsThread: SNAPLIVE = "+filename);
//        } else {

        if(Constants.getFixedMemMap()) {
                filename = FixedMemMapReferenceQueue.getNextFileName();
            } else {
                filename = DynamicMemMapReferenceQueue.getCurrentFileName();
            }
            reporter.set_report_area("datastoreEventsThread MDA = "+filename);
//        }
        return filename;
    }

    private MetaDataStore makeMDS() {
        try {
            return new MDSBuilder()
                    .position(coord.getStagePosition())
                    .time(coord.getTime())
                    .z(coord.getZ())
                    .channel(coord.getChannel())
                    .xRange(temp_img.getWidth())
                    .yRange(temp_img.getHeight())
                    .bitDepth(temp_img.getBytesPerPixel() * 8)
                    .prefix(prefix)
                    .windowname(window_name)
                    .channel_name(channel_name)
                    .filepath(filename)
                    .buffer_position(buffer_position)
                    .buildMDS();
        } catch(IllegalAccessException ilex){
            reporter.set_report_area(String.format("Fail to build MDS for c%d, z%d, p%d, t%d, filepath=%s",
                    coord.getChannel(), coord.getZ(), coord.getStagePosition(), coord.getTime(), filename));
        }
        return null;
    }

    private void writeToMemMap() {
        try {
            reporter.set_report_area("datastoreEventsThread: writing memmap to =("+filename+", "+channel_name+")" );

//            // old style
//            memMapImage out = new memMapImage(temp_img, filename);
//            out.writeToMemMap();

            if(Constants.getFixedMemMap()){
                memMapFromBuffer out = new memMapFromBuffer(temp_img, buffer);

//                out.verifyMemMapAt(buffer_position);

                out.writeToMemMapAt(buffer_position);

//                out.verifyMemMapAt(buffer_position);

            } else {
                memMapFromBuffer out = new memMapFromBuffer(temp_img, buffer);
                out.writeToMemMapAt(buffer_position);
            }

        } catch (NullPointerException ex) {
            reporter.set_report_area("null ptr exception in datastoreEvents Thread");
        } catch (NoImageException ex) {
            reporter.set_report_area(ex.toString());
        } catch (Exception ex) {
            reporter.set_report_area("EXCEPTION IN WRITE TO MEMMAP: "+ex.toString());
        }
    }

    private void writeToHashMap() {
        try {
            fds.putMDS(mds);
            reporter.set_report_area("writing MetaDataStore to Map");
        } catch (Exception ex) {
            reporter.set_report_area(ex.toString());
        }
    }

    private void writeToQueues() {
        try {
//            pq.putPath(filename);
            mq.putMDS(mds);
            reporter.set_report_area("writing to path and MDS queues");
        } catch (NullPointerException ex) {
            reporter.set_report_area("null ptr exception writing to LinkedBlockingQueue");
        } catch (Exception ex) {
            reporter.set_report_area(ex.toString());
        }
    }

    private void notifyListeners() {
        try {
            reporter.set_report_area("notifying py4j listeners");
            if (Constants.getPy4JRadioButton()) {
                Py4JListener.notifyAllListeners();
            }
        } catch (Py4JListenerException ex) {
            reporter.set_report_area("Exception in datastoreEventsThread notifying Py4J listeners: "+ex.toString());
        } catch (Exception ex) {
            reporter.set_report_area("General Exception while notifying Py4J listeners: "+ex.toString());
        }
    }


    
}
