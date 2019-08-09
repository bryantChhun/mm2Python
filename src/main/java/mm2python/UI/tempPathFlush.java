/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.UI;

import mm2python.DataStructures.Constants;
import mm2python.DataStructures.Queues.DynamicMemMapReferenceQueue;
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

        if(!Constants.getFixedMemMap()) {
            DynamicMemMapReferenceQueue.resetAll();
        }

        JOptionPane.showMessageDialog(panel,
                String.format("Removing files from %s\n" +
                "%s files removed\n"+
                "%s files skipped", Constants.tempFilePath, count, skipped));

    }
    
}
