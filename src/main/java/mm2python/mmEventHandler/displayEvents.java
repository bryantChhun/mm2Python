/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.UI.reports;
import mm2python.Constants.constants;

import com.google.common.eventbus.Subscribe;
import org.micromanager.Studio;
import org.micromanager.display.*;

/**
 * used to track display windows
 *    number of windows is tracked using static field in "constants" class.
 * @author bryant.chhun
 */
public class displayEvents {
    private final Studio mm;
    private final reports textarea;
    private final DisplayWindow window;
    
    public displayEvents(Studio mm_, int current_window_count_, DisplayWindow window_, reports reports) {
        mm = mm_;
        window = window_;
        textarea = reports;
        constants.current_window_count = current_window_count_;
    }
    
    public displayEvents(Studio mm_, DisplayWindow window_, reports reports_) {
        mm = mm_;
        window = window_;
        textarea = reports_;
    }
    
    public void registerThisDisplay(){
        window.registerForEvents(this);
    }
 
    @Subscribe
    public void monitor_DisplayDestroyedEvent(DisplayDestroyedEvent event){
        textarea.set_report_area("display destroyed");
        //constants.current_window_count -= 1;
        //textarea.set_report_area("window count after destuction= "+constants.current_window_count);
        window.unregisterForEvents(this);
    }
    
//    @Subscribe
//    public void monitor_NewDisplaySettingsEvent(NewDisplaySettingsEvent event){
//        
//    }
    
//    @Subscribe
//    public void monitor_NewImagePlusEvent(NewImagePlusEvent event){
//        
//    }
    
//    @Subscribe
//    public void monitor_PixelsSetEvent(PixelsSetEvent event) {
//        try{ 
//            textarea.set_report_area("display window new drawing detected");
//        } catch (Exception ex){
//            System.out.println("pixel set exception = "+ex);
//        }
//    }

    
//    /**
//     * RequestToDrawEvent appears in the online documentation: https://micro-manager.org/wiki/Version_2.0_API_Events
//     *    but it does not appear in the code!
//     * @param event 
//     */
//    @Subscribe
//    public void monitor_RequestToDrawEvent(RequestToDrawEvent event){
//        
//    }
    
}
