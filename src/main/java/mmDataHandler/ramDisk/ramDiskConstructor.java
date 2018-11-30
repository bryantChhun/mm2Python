/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmDataHandler.ramDisk;

import org.micromanager.Studio;
import Constants.constants;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author bryant.chhun
 */
public class ramDiskConstructor {
    Studio mm;
    String command;
    String command2;
    
    // modify later to actually create a ramdisk
    public ramDiskConstructor(Studio mm_) {
        mm = mm_;
        
        String OS = System.getProperty("os.name").toLowerCase();
        
        if (OS.indexOf("win") >= 0) {
            System.out.println("Windows system detected");
            command = "imdisk -a -s 4096M -m R: -p \"/fs:ntfs /q /y\"";
        } else if (OS.indexOf("mac") >= 0) {
            System.out.println("Mac system detected, creating 4gb RAM disk at 'RAM_Disk'");
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
            // because making ramdisk requires parsing used by shell.  We must use String[] object
            Process p = Runtime.getRuntime().exec(new String[] { "bash", "-c", command});
            TimeUnit.SECONDS.sleep(3);
            Process p2 = Runtime.getRuntime().exec(new String[] { "bash", "-c", command2});
        } catch (Exception ex) {
            mm.logs().showError(ex);
            System.out.println("exception during ramdisk construction");
        }
        
    }
    
}
