package org.mm2python.UI;

import mmcorej.CMMCore;
import org.micromanager.Studio;

public class mm2pythonGUIRunnable implements Runnable{

    private CMMCore mmc_;
    private Studio mm_;

    private pythonBridgeUI_dialog myFrame_;

    public mm2pythonGUIRunnable(Studio studio, CMMCore core) {
        mm_ = studio;
        mmc_ = core;
    }

    @Override
    public void run() {
        if (myFrame_ == null) {
            try {
                myFrame_ = new pythonBridgeUI_dialog(mm_, mmc_);
                mm_.events().registerForEvents(myFrame_);
            } catch (Exception e) {
                mm_.logs().showError(e);
            }
        }
        myFrame_.pack();
        myFrame_.setVisible(true);
    }
}
