/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.Constants.constants;
import mm2python.UI.reports;
import org.micromanager.Studio;
import org.micromanager.display.DisplayManager;
import org.micromanager.display.DisplayWindow;

/**
 *
 * @author bryant.chhun
 */
public class displayEventsThread implements Runnable {
    private Studio mm;
    private reports reports;
    private DisplayManager dm;
    private displayEvents display_events;
    private static int old_window_count;
    
    public displayEventsThread(Studio mm_, DisplayManager dm_, reports report) {
        mm = mm_;
        dm = dm_;
    }
    
    @Override
    public void run() {
        while(constants.current_window_count <= dm.getAllImageWindows().size()) {
            
            System.out.println("current window count = "+constants.current_window_count);
            int num_windows = dm.getAllImageWindows().size();
            System.out.println("num of image windows = "+num_windows);
            //System.out.println("old window counter = "+old_window_count);

            if(num_windows > constants.current_window_count){
                System.out.println("num windows increased = "+num_windows);
                DisplayWindow dw = dm.getAllImageWindows().get(num_windows-1);
                System.out.println("new displaywindow exists = "+dw);

                display_events = new displayEvents(mm, constants.current_window_count, dw, reports);
                display_events.registerThisDisplay();

                constants.current_window_count += 1;
                reports.set_report_area("current window count = "+constants.current_window_count);
                old_window_count = constants.current_window_count;
                break;
                
            } else if (old_window_count == dm.getAllImageWindows().size()){
                //old_window_count = current_window_count;
                break;
            } else  {
                System.out.println("num_windows not incremented yet");
            }
        }
    }
    
}
