/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmEventHandler;

import mm2python.UI.reporter;
import mm2python.mmEventHandler.Exceptions.NotImplementedException;

import com.google.common.eventbus.Subscribe;
import org.micromanager.Studio;
import org.micromanager.display.*;

/**
 * used to track display windows
 *    number of windows is tracked using static field in "Constants" class.
 * @author bryant.chhun
 */
public class displayEvents {
    private final Studio mm;
    private final DisplayWindow window;


    /**
     * For registering micro-manager display events
     *   See here for more details about mm API events:
     *   https://micro-manager.org/wiki/Version_2.0_API_Events
     *
     * @param mm_ parent micro-manager Studio object
     * @param window_ DisplayWindow whose construction triggered this class's construction
     */
    public displayEvents(Studio mm_, DisplayWindow window_) {
        mm = mm_;
        window = window_;
    }

    public displayEvents(Studio mm_, int current_window_count_, DisplayWindow window_) {
        mm = mm_;
        window = window_;
    }

    /**
     * Register this class for notifications from micro-manager.
     */
    public void registerThisDisplay(){
        window.registerForEvents(this);
    }

    @Subscribe
    public void monitor_DisplayDestroyedEvent(DisplayDestroyedEvent event){
        reporter.set_report_area("display destroyed");
//        Constants.current_window_count -= 1;
        //textarea.set_report_area("window count after destuction= "+Constants.current_window_count);
        window.unregisterForEvents(this);
    }
    
//    @Subscribe
    public void monitor_NewDisplaySettingsEvent(NewDisplaySettingsEvent event) throws NotImplementedException{
        throw new NotImplementedException("NewDisplaySettingsEvent not implemented");
    }
    
//    @Subscribe
    public void monitor_NewImagePlusEvent(NewImagePlusEvent event) throws NotImplementedException{
        throw new NotImplementedException("NewImagePlusEvent not implemented");
    }
    
//    @Subscribe
    public void monitor_PixelsSetEvent(PixelsSetEvent event) throws NotImplementedException{
        throw new NotImplementedException("PixelsSetEvent not implemented");
    }

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
