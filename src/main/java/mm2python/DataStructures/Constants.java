/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.DataStructures;

import java.util.List;
import java.util.ArrayList;

import mm2python.DataStructures.Exceptions.OSTypeException;

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

    public static boolean fixedMemMap;

    public static boolean py4JRadioButton;

    public static final String OS;

    static {
        OS = getOSandHandle();
    }

    public Constants() {
        if(ports == null) {
            ports = new ArrayList<>();
        }
    }

    private static String getOSandHandle() {
        try {
            return getOS();
        } catch(OSTypeException osx) {
            System.out.println("String osx");
        }
        return null;
    }

    public static String getOS() throws OSTypeException {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {
            return("win");
        } else if (OS.contains("mac")) {
            return("mac");
        } else {
            throw new OSTypeException("OS type is not implemented.  Must use Mac or Windows");
        }
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


}
