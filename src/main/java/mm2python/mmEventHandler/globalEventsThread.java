/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.Constants.constants;
import mm2python.UI.reports;
import org.micromanager.Studio;
import org.micromanager.display.DisplayWindow;

/**
 *
 * @author bryant.chhun
 */
public class globalEventsThread implements Runnable {
    private final Studio mm;
    private final reports reports;
    private final DisplayWindow dw;
    
    public globalEventsThread(Studio mm_, DisplayWindow dw_, reports reports_) {
        mm = mm_;
        dw = dw_;
        reports = reports_;
        System.out.println("global events thread filename = "+constants.RAMDiskName);
    }
    
    @Override
    public void run() {
        displayEvents display_events = new displayEvents(mm, dw, reports);
        display_events.registerThisDisplay();

        datastoreEvents datastore_events = new datastoreEvents(mm, dw.getDatastore(), dw.getName(), reports);
        datastore_events.registerThisDatastore();
    }
    
    private void println(String out){
        System.out.println(out);
    }
    
}
