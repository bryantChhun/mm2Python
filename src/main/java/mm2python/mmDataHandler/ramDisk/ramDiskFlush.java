/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler.ramDisk;

import mm2python.DataStructures.constants;
import mm2python.UI.reporter;
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

        String OS = System.getProperty("os.name").toLowerCase();

        if (OS.contains("mac")) {
            String command = String.format("rm %s/*.dat", constants.RAMDiskName);
            try {
                Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
            } catch (Exception ex) {
                reporter.set_report_area(false, true, ex.toString());
                reporter.set_report_area(true, false, "error while trying to clear all temp files in RAM_disk");
            }
        } else if (OS.contains("win")) {
            String command = String.format("diskutil erasevolume HFS+ '%s' `hdiutil attach -nomount ram://8388608`", "RAM_disk");
            reporter.set_report_area(command);
        }

    }
    
    
}
