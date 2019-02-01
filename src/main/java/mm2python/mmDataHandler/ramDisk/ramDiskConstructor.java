/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.mmDataHandler.ramDisk;

import org.micromanager.Studio;

import javax.swing.*;
import java.util.concurrent.TimeUnit;
import mm2python.UI.reports;

/**
 *
 * @author bryant.chhun
 */
public class ramDiskConstructor {
    Studio mm;
    String command;
    String command2;
    private final reports report_area;
    
    // modify later to actually create a ramdisk
    public ramDiskConstructor(Studio mm_, JTextArea logger_area_) {
        mm = mm_;
        report_area = new reports(logger_area_);
        
        String OS = System.getProperty("os.name").toLowerCase();
        
        if (OS.indexOf("win") >= 0) {
            System.out.println("Windows system detected");
            report_area.set_report_area("Windows system detected");
            // TODO: add a check here for whether imdisk is installed
//            WHERE imdisk
//            IF %ERRORLEVEL% NEQ 0 ECHO imdisk wasn't found
//            sc query imdisk
            // commmand = runas /user:591Y4M2 "cmd.exe"
            command = "imdisk -a -s 4096M -m Q: -p \"/fs:ntfs /q /y\"";
//            command2 = "mkdir JavaPlugin_temp_folder";
        } else if (OS.indexOf("mac") >= 0) {
            System.out.println("Mac system detected, creating 4gb RAM disk at 'RAM_Disk'");
            report_area.set_report_area("Mac system detected, creating 4gb RAM disk at 'RAM_Disk'");
            command = String.format("diskutil erasevolume HFS+ '%s' `hdiutil attach -nomount ram://8388608`", "RAM_disk");
            command2 = "mkdir /Volumes/RAM_disk/JavaPlugin_temp_folder";
        } else if (OS.indexOf("nix") >= 0) {
            System.out.println("Unix or Linux system detected");
            command = "";
        } else {
            System.out.println("Current OS is not supported");
            command = "";
        }

        try {
            System.out.println("creating RAM disk using command: ");
            System.out.println(command);
            if (OS.indexOf("mac") >= 0) {
                // because making ramdisk requires parsing used by shell.  We must use String[] object
                Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command});
                TimeUnit.SECONDS.sleep(3);
                Process p2 = Runtime.getRuntime().exec(new String[] { "bash", "-c", command2});
            } else if (OS.indexOf("win") >= 0) {
                report_area.set_report_area("executing windows process to create ramdisk");
                try {
                    Process p = Runtime.getRuntime().exec(new String[]{"cmd.exe", "/c", command});
                } catch (Exception ex) {
                    report_area.set_report_area(ex.toString());
                }
            }



        } catch (Exception ex) {
            mm.logs().showError(ex);
            System.out.println("exception during ramdisk construction");
        }
        
    }
    
}
