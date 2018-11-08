/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

import org.micromanager.Studio;
import org.micromanager.display.DisplayWindow;
import org.micromanager.display.DisplayManager;
import org.micromanager.data.Datastore;
import org.micromanager.SnapLiveManager;

import javax.swing.JTextArea;
import UI.reports;
import Constants.constants;

import com.google.common.eventbus.Subscribe;
import org.micromanager.events.NewDisplayEvent;
import org.micromanager.events.DisplayAboutToShowEvent;


/**
 *
 * @author bryant.chhun
 */
public class globalEvents {
    private final Studio mm;
    private final reports reports;
    private displayEvents display_events;
    private datastoreEvents datastore_events;
    private final DisplayManager dm;
    private DisplayWindow dw;
    private Datastore ds;
    private static int old_window_count;
    
    //private Thread det;
    
    public globalEvents(Studio mm_, JTextArea UI_textArea) {
        mm = mm_;
        dm = mm.getDisplayManager();
        reports = new reports(UI_textArea);
    }
    
    public void registerGlobalEvents() { 
        mm.events().registerForEvents(this);
        new constants();
    }
    
    public void unRegisterGlobalEvents() {
        System.out.println("shutting down event monitoring and clearing dequeue references");
        reports.set_report_area("shutting down event monitoring and clearing dequeue references");
        constants.resetAll();
        mm.events().unregisterForEvents(this);
    }
 
    @Subscribe
    public void monitor_aboutToShow(DisplayAboutToShowEvent event) {
        reports.set_report_area("DisplayAboutToShowEvent event detected");
        
        dw = event.getDisplay();
        
//        Thread GET = new Thread(new globalEventsThread(mm, dm, reports));
//        GET.start();
//
//        Thread GET = new Thread(new globalEventsThread(mm, dw, reports));
//        GET.start();
        
        new Thread(new globalEventsThread(mm, dw, reports)).start();
        
    }
    
    private void println(String out){
        System.out.println(out);
    }
    
}
