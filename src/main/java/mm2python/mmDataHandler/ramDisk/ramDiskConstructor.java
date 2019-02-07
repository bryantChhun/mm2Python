/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler.ramDisk;

import mm2python.UI.reporter;
import org.micromanager.Studio;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author bryant.chhun
 */
public class ramDiskConstructor {
    private Studio mm;
    private String command;
    private String command2;

    // modify later to actually create a ramdisk
    public ramDiskConstructor(Studio mm_) {
        mm = mm_;

        String OS = System.getProperty("os.name").toLowerCase();
        
        if (OS.indexOf("win") >= 0) {
            reporter.set_report_area(true, false, "Windows system detected");
            reporter.set_report_area("Windows system detected, be sure imdisk is installed");
            // TODO: add a check here for whether imdisk is installed
//            WHERE imdisk
//            IF %ERRORLEVEL% NEQ 0 ECHO imdisk wasn't found
//            sc query imdisk
//             commmand = runas /user:591Y4M2 "cmd.exe"
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
            command = "";
        } else {
            reporter.set_report_area(true, false, "Current OS is not supported");
            reporter.set_report_area("current OS is not supported");
            command = "";
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
