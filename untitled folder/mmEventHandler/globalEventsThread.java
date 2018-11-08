/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmEventHandler;

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
    
    public globalEventsThread(Studio mm_, DisplayManager dm_, reports reports_) {
        mm = mm_;
        dm = dm_;
        dw = dm.getCurrentWindow();
        reports = reports_;
    }
    
    public globalEventsThread(Studio mm_, DisplayWindow dw_, reports reports_) {
        mm = mm_;
        dw = dw_;
        dm = mm.getDisplayManager();
        reports = reports_;
    }
    
    @Override
    public void run() {
        display_events = new displayEvents(mm, dw, reports);
        display_events.registerThisDisplay();

        datastore_events = new datastoreEvents(mm, dw.getDatastore(), dw.getName(), reports);
        datastore_events.registerThisDatastore();
    }
    
//    @Override
//    public void run() {
//        try {
//            while(constants.current_window_count <= dm.getAllImageWindows().size()) {
//                println("current window count = "+constants.current_window_count);
//                int num_windows = dm.getAllImageWindows().size();
//                println("num of image windows = "+num_windows);
//                //DisplayWindow dw = dm.getAllImageWindows().get(num_windows-1);
//                //println("new displaywindow exists = "+dw);
//                //println(""+dw.getDatastore().getNumImages());
//                if(num_windows > constants.current_window_count){
//                    println("num windows increased = "+num_windows);
//
//                    DisplayWindow dw = dm.getAllImageWindows().get(num_windows-1);
//                    println("new displaywindow exists = "+dw);
//
//                    display_events = new displayEvents(mm, constants.current_window_count, dw, reports);
//                    display_events.registerThisDisplay();
//
//                    constants.current_window_count += 1;
//                    println("current window count incremented = "+constants.current_window_count);
//                    //old_window_count = constants.current_window_count;
//
//                    datastore_events = new datastoreEvents(mm, dw.getDatastore(), dw.getName(), reports);
//                    datastore_events.registerThisDatastore();
//                    break;
////                } else if (old_window_count == dm.getAllImageWindows().size()){
////                    //old_window_count = current_window_count;
////                    System.out.println("window incremented and accounted for, breaking loop");
////                    break;
//                } else  {
//                    System.out.println("num_windows not incremented yet");
//                }
//            }
//        } catch (Exception ex) {
//            System.out.println("exception in monitor to show = "+ex);
//        }
//    }
//    
    private void println(String out){
        System.out.println(out);
    }
    
}
