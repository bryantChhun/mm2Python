/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmDataHandler.ramDisk;

import Constants.constants;
import org.micromanager.Studio;

/**
 * clears all images from the RAMdisk
 * @author bryant.chhun
 */
public class ramDiskFlush {
    private Studio mm;

    public ramDiskFlush(Studio mm_) {
        mm = mm_;
    }

    public void clearRamDiskContents() {
        String command = String.format("rm %s/*.dat", constants.RAMDiskName);
        try {
            Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
        } catch (Exception ex) {
            mm.logs().showError(ex);
            System.out.println("error while trying to clear all temp files in RAM_disk");
        }
    }
    
    
}
