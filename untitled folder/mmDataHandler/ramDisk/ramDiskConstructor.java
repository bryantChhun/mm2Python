/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mmDataHandler.ramDisk;

import org.micromanager.Studio;
import Constants.constants;

/**
 *
 * @author bryant.chhun
 */
public class ramDiskConstructor {
    Studio mm;
    String command;
    
    // modify later to actually create a ramdisk
    public ramDiskConstructor(Studio mm_) {
        mm = mm_;
        
        String OS = System.getProperty("os.name").toLowerCase();
        
        if (OS.indexOf("win") >= 0) {
            System.out.println("Windows system detected");
            command = "imdisk -a -s 4096M -m R: -p \"/fs:ntfs /q /y\"";
        } else if (OS.indexOf("mac") >= 0) {
            System.out.println("Mac system detected, creating 4gb RAM disk at 'RAM_Disk'");
            command = String.format("diskutil erasevolume HFS+ '%s' `hdiutil attach -nomount ram://8388608`", constants.RAMDiskName);
        } else if (OS.indexOf("nix") >= 0) {
            System.out.println("Unix or Linux system detected");
            command = "";
        } else {
            System.out.println("Current OS is not supported");
            command = "";
        }

        try {
            Process p = Runtime.getRuntime().exec(command);
        } catch (Exception ex) {
            mm.logs().showError(ex);
            System.out.println("exception during ramdisk construction");
        }
        
    }
    
}
