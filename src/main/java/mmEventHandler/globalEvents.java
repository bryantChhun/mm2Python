/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

import org.micromanager.Studio;
import org.micromanager.display.DisplayWindow;

import javax.swing.JTextArea;
import UI.reports;
import Constants.constants;

import com.google.common.eventbus.Subscribe;
import java.util.concurrent.ExecutorService;
import mmEventHandler.Executor.main_executor;
import org.micromanager.events.NewDisplayEvent;
import org.micromanager.events.DisplayAboutToShowEvent;


/**
 *
 * @author bryant.chhun
 */
public class globalEvents {
    private final Studio mm;
    private final reports reports;
//    private final DisplayManager dm;

    private final ExecutorService mmExecutor;
    
    
    //private Thread det;
    
    public globalEvents(Studio mm_, JTextArea UI_textArea) {
        mm = mm_;
        //dm = mm.getDisplayManager();
        reports = new reports(UI_textArea);
        mmExecutor = main_executor.getExecutor();
        System.out.println("global events filename = "+constants.RAMDiskName);
    }
    
    public void registerGlobalEvents() { 
        mm.events().registerForEvents(this);
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

        DisplayWindow dw = event.getDisplay();

        //new Thread(new globalEventsThread(mm, dw, reports)).start();
        
        mmExecutor.execute(new globalEventsThread(mm, dw, reports));
    }
    
    private void println(String out){
        System.out.println(out);
    }
    
}
