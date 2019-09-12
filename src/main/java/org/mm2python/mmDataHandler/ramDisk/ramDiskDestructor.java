/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.mmDataHandler.ramDisk;

import javax.swing.JPanel;
import javax.swing.JOptionPane;

/**
 *
 * @author bryant.chhun
 */
public class ramDiskDestructor {

    private static final JPanel panel = new JPanel();

    public ramDiskDestructor() {
        JOptionPane.showMessageDialog(panel, "Automatic Destruction of RAM DISK not implemented\n" +
                "Please manually unmount your ramdisk after micromanager is shut down");
    }

}
