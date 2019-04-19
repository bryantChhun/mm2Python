/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.DataStructures.constants;
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
    private final Studio mm;
    private final Image temp_img;
    private final Coords temp_coord;
    private final String prefix;
    private String filename;
    private final String window_name;
    private final String[] channel_names;


    /**
     * Executes sequence of tasks up run by executor:
     *  1) assigns a filename based on metadata
     *  2) writes the file as memory mapped image
     *  3) registers file metadata in static repository
     *  4) notifies any py4j listeners
     * @param mm_
     * @param data_
     * @param c_
     * @param prefix_
     * @param window_name_
     */
    public datastoreEventsThread(Studio mm_, Datastore data_, Coords c_, String prefix_, String window_name_) {
        mm = mm_;
        temp_img = data_.getImage(c_);
        temp_coord = c_;
        prefix = prefix_;
        window_name = window_name_;
        channel_names = data_.getSummaryMetadata().getChannelNames();
    }
    
    @Override
    public void run() {
        
        //Check for live vs MDA image
        if(window_name.equals("Snap/Live View")) {
            filename = constants.RAMDiskName+"Snap-Live-Stream.dat";
            reporter.set_report_area(false, false, "datastoreEventsThread: SNAPLIVE = "+filename);
        } else {
            filename = String.format(constants.RAMDiskName+"%s_t%03d_p%03d_z%02d_c%02d.dat",
                prefix, temp_coord.getTime(), temp_coord.getStagePosition(), temp_coord.getZ(), temp_coord.getChannel());
            reporter.set_report_area(false, false, "datastoreEventsThread MDA = "+filename);
        }
        
        //Write memory mapped image
        try {
            reporter.set_report_area(false, false, "datastoreEventsThread: writing memmap");
            memMapImage out = new memMapImage(mm, temp_img, temp_coord, filename, prefix, window_name, channel_names);
            out.writeToMemMap();
        } catch (NullPointerException ex) {
            reporter.set_report_area(true, false, "null ptr exception in datastoreEvents Thread");
        } catch (NoImageException ex) {
            reporter.set_report_area(true, false, ex.toString());
        }
        
        // notify Listeners
        try {
            reporter.set_report_area(true, false, "notifying py4j listeners");
            if (constants.py4JRadioButton) {
                Py4JListener notifyListener = new Py4JListener();
                notifyListener.notifyAllListeners();
            }
        } catch (Py4JListenerException ex) {
            reporter.set_report_area(true, false, "Exception in datastore events thread while notifying Py4J listeners: "+ex.toString());
        } catch (Exception ex) {
            reporter.set_report_area(true, false, "General Exception while notifying Py4J listeners: "+ex.toString());
        }
        
    }
    
    
    
}
