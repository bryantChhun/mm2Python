/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler.ramDisk;

import mm2python.UI.reporter;
import org.micromanager.Studio;

import javax.swing.JPanel;
import javax.swing.JOptionPane;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author bryant.chhun
 */
public class ramDiskConstructor {

    private static final JPanel panel = new JPanel();

    // modify later to actually create a ramdisk
    public ramDiskConstructor(Studio mm_) {
        Studio mm;
        String command;
        String command2;
        mm = mm_;

        String OS = System.getProperty("os.name").toLowerCase();

        /**
         * check for operating system
         * establish commands needed to create RAMDISK based on that OS
         */
        if (OS.indexOf("win") >= 0) {
            reporter.set_report_area(true, false, "Windows system detected");
            reporter.set_report_area("Windows system detected, be sure imdisk is installed");
            JOptionPane.showMessageDialog(panel, "Windows system detected, \n" +
                    "Please be sure imdisk is installed\n" +
                    "https://sourceforge.net/projects/imdisk-toolkit/");

            command = "imdisk -a -s 4096M -m Q: -p \"/fs:ntfs /q /y\"";
            command2 = "mkdir JavaPlugin_temp_folder";

        } else if (OS.indexOf("mac") >= 0) {
            reporter.set_report_area(true, false, "Mac system detected, creating 4gb RAM disk at 'RAM_Disk'");
            reporter.set_report_area("Mac system detected, creating 4gb RAM disk at 'RAM_Disk'");

            command = String.format("diskutil erasevolume HFS+ '%s' `hdiutil attach -nomount ram://8388608`", "RAM_disk");
            command2 = "mkdir /Volumes/RAM_disk/JavaPlugin_temp_folder";

        } else if (OS.indexOf("nix") >= 0) {
            reporter.set_report_area(true, false, "Unix or Linux system detected");
            reporter.set_report_area("RAMdisk for unix system not supported");
            JOptionPane.showMessageDialog(panel, "Current OS does not support Ram disk, or imdisk is not installed");

            command = "";
            command2 = "";

        } else {
            reporter.set_report_area(true, false, "Current OS is not supported");
            reporter.set_report_area("current OS is not supported");
            JOptionPane.showMessageDialog(panel, "Current OS does not support Ram disk, or imdisk is not installed");

            command = "";
            command2 = "";

        }

        try {
            reporter.set_report_area(true, false, "creating RAM disk using command: ");
            reporter.set_report_area(true, false, command);
            if (OS.indexOf("mac") >= 0) {
                // because making ramdisk requires parsing used by shell.  We must use String[] object
                reporter.set_report_area("(mac): Creating Ramdisk as drive Q");
                Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command});
                TimeUnit.SECONDS.sleep(3);
                Process p2 = Runtime.getRuntime().exec(new String[] { "bash", "-c", command2});
            } else if (OS.indexOf("win") >= 0) {
                reporter.set_report_area("(win): Creating Ramdisk as drive Q");
                try {
                    Process p = Runtime.getRuntime().exec(new String[] {"cmd.exe", "/c", command});
                } catch (Exception ex) {
                    reporter.set_report_area(ex.toString());
                }
            }

        } catch (Exception ex) {
            mm.logs().showError(ex);
            reporter.set_report_area("exception during ramdisk construction");
        }
        
    }
    
}
