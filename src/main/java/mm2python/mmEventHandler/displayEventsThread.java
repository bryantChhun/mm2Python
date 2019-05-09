/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.UI.reporter;
import org.micromanager.Studio;
import org.micromanager.display.DisplayManager;

/**
 *
 * @author bryant.chhun
 */
public class displayEventsThread implements Runnable {
    private Studio mm;
    private DisplayManager dm;
    private displayEvents display_events;
    private static int old_window_count;
    
    public displayEventsThread(Studio mm_, DisplayManager dm_) {
        mm = mm_;
        dm = dm_;
    }
    
    @Override
    public void run() {
        /**
         * To keep track of the number of active windows in micromanager.
         *  **** This was deemed entirely unnecessary as micromanager already handles window counts ****
         */
//        while(Constants.current_window_count <= dm.getAllImageWindows().size()) {
//
//            reporter.set_report_area(true, false, "current window count = "+Constants.current_window_count);
//            int num_windows = dm.getAllImageWindows().size();
//            reporter.set_report_area(true, false, "num of image windows = "+num_windows);
//            //System.out.println("old window counter = "+old_window_count);
//
//            if(num_windows > Constants.current_window_count){
//                reporter.set_report_area(true, false, "num windows increased = "+num_windows);
//                DisplayWindow dw = dm.getAllImageWindows().get(num_windows-1);
//                reporter.set_report_area(true, false,"new displaywindow exists = "+dw);
//
//                display_events = new displayEvents(mm, Constants.current_window_count, dw);
//                display_events.registerThisDisplay();
//
//                Constants.current_window_count += 1;
//                reporter.set_report_area("current window count = "+Constants.current_window_count);
//                old_window_count = Constants.current_window_count;
//                break;
//
//            } else if (old_window_count == dm.getAllImageWindows().size()){
//                //old_window_count = current_window_count;
//                break;
//            } else  {
//                reporter.set_report_area(true, false, "num_windows not incremented yet");
//            }
//        }
        reporter.set_report_area(false, false, "displayevents not supported");
    }
    
}
