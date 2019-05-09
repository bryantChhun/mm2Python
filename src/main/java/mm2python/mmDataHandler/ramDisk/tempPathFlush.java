/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler.ramDisk;

import mm2python.DataStructures.Constants;
import org.micromanager.Studio;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.File;

/**
 * clears all images from the RAMdisk
 * @author bryant.chhun
 */
public class tempPathFlush {
    private static final JPanel panel = new JPanel();
    private final Studio mm;

    public tempPathFlush(Studio mm_) {
        mm = mm_;
    }

    /**
     * memory mapped files can not be deleted while the micro-manager process is open
     * Nor can memory mapped files be unmapped until after the process closes
     *
     * This method should be called to clear old memmap file names from earlier runs
     */
    public void clearTempPathContents() {
        File index = new File(Constants.tempFilePath);
        String[] entries = index.list();

        int count = 0;
        int skipped = 0;
        for(String s: entries) {
            try {
                File currentFile = new File(index.getPath(),s);
                currentFile.delete();
                count += 1;
            } catch (Exception ex){
                skipped += 1;
                mm.logs().logError(ex);
            }
        }

        JOptionPane.showMessageDialog(panel,
                String.format("Removing files from %s\n" +
                "%s files removed\n"+
                "%s files skipped", Constants.tempFilePath, count, skipped));

        //        String OS = System.getProperty("os.name").toLowerCase();

//        if (OS.contains("mac")) {
//            String command = String.format("rm %s/*.dat", Constants.tempFilePath);
//            try {
//                Process p = Runtime.getRuntime().exec(new String[]{"bash", "-c", command});
//            } catch (Exception ex) {
//                reporter.set_report_area(false, true, ex.toString());
//                reporter.set_report_area(true, false, "error while trying to clear all temp files in RAM_disk");
//            }
//        } else if (OS.contains("win")) {
//            String command = String.format("diskutil erasevolume HFS+ '%s' `hdiutil attach -nomount ram://8388608`", "RAM_disk");
//            reporter.set_report_area(command);
//        }
    }
    
}
