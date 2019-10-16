/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.DataStructures;

import java.util.List;
import java.util.ArrayList;

import org.mm2python.DataStructures.Exceptions.OSTypeException;
import org.mm2python.UI.reporter;

/**
 *
 * @author bryant.chhun
 */
public class Constants {
    /**
     * Constants contains data maps for reference by the messenger service
     *  It also contains methods for OS-level Constants, like temp file paths.
     */

    public static String tempFilePath;

    public static long bitDepth, height, width;

    public static List<Integer> ports;

    private static boolean fixedMemMap;

    private static boolean py4JRadioButton;

    private static boolean zmqButton;

    private static String OS;

    public static long max;
    public static long min;
    public static int init;
    public static volatile int data_mismatches;

    static {
        try {
            OS = setOS();
        } catch (OSTypeException osx) {
            reporter.set_report_area("error getting OS type: "+osx);
        }

        bitDepth = 16;
    }

    public Constants() {
        if(ports == null) {
            ports = new ArrayList<>();
        }
    }

    private static String setOS() throws OSTypeException {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return("win");
        } else if (OS.contains("mac")) {
            return("mac");
        } else {
            throw new OSTypeException("OS type is not implemented.  Must use Mac or Windows");
        }
    }

    public static String getOS() {
        return OS;
    }

    /**
     * if true, user will use finite number of memmap files
     * @param b : boolean
     */
    public static void setFixedMemMap(boolean b) {
        fixedMemMap = b;
    }

    public static boolean getFixedMemMap() {
        return fixedMemMap;
    }

    /**
     * whether to use the Py4J message passing system
     * @param b : boolean
     */
    public static void setPy4JRadioButton(boolean b) {
        py4JRadioButton = b;
    }

    public static boolean getPy4JRadioButton() {
        return py4JRadioButton;
    }

    /**
     * whether to use the
     * @param b : boolean
     */
    public static void setZMQButton(boolean b) {
        zmqButton = b;
    }

    public static boolean getZMQButton() {
        return zmqButton;
    }


}
