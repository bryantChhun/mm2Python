/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

import Constants.constants;
import UI.reports;
import org.micromanager.Studio;
import org.micromanager.display.DisplayManager;
import org.micromanager.display.DisplayWindow;

/**
 *
 * @author bryant.chhun
 */
public class globalEventsThread implements Runnable {
    private final Studio mm;
    private final reports reports;
    private displayEvents display_events;
    private datastoreEvents datastore_events;
    private final DisplayManager dm;
    private final DisplayWindow dw;
    private static int old_window_count;
    
    public globalEventsThread(Studio mm_, DisplayWindow dw_, reports reports_) {
        mm = mm_;
        dw = dw_;
        dm = mm.getDisplayManager();
        reports = reports_;
        System.out.println("global events thread filename = "+constants.RAMDiskName);
    }
    
    @Override
    public void run() {
        display_events = new displayEvents(mm, dw, reports);
        display_events.registerThisDisplay();

        datastore_events = new datastoreEvents(mm, dw.getDatastore(), dw.getName(), reports);
        datastore_events.registerThisDatastore();
    }
    
    private void println(String out){
        System.out.println(out);
    }
    
}
