/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.UI;

import org.micromanager.Studio;

import javax.swing.JTextArea;

/**
 * send messages to mm2python.UI display
 * @author bryant.chhun
 */

/**
 * Singleton class for reporting to the same JTextArea, System.out, or mm.logs
 */
public class reporter {

    private static JTextArea reportArea;
    private static Studio mm;
    
    public reporter(JTextArea reportArea_, Studio mm_) {
        if (reportArea == null) {
            reportArea = reportArea_;
        }
        if (mm == null) {
            mm = mm_;
        }
    }

    /**
     * Basic reporter to ONLY JTextArea
     * @param report String message
     */
    public static void set_report_area(String report) {
        String newline = "\n";
        reportArea.append(newline+report);
    }

    /**
     * Overloaded reporter
     *  Boolean flags signal which reporter to send to
     *  If both systemout and mmlogs are false, report is sent to all
     *  use above method to send report to JTextArea ONLY
     * @param systemout boolean flag, true = send to system.out
     * @param mmlogs boolean flag, true = send to mmlogs
     * @param report String message
     */
    public static void set_report_area(boolean systemout, boolean mmlogs, String report) {
        // this logic is weird, fix it
        // just do two if's and one else.  Remove the overload above.
        if (systemout && mmlogs){
            System.out.println(report);
            mm.logs().logMessage(report);
        } else if (systemout) {
            System.out.println(report);
        } else if (mmlogs) {
            mm.logs().logMessage(report);
        } else {
            System.out.println(report);
            mm.logs().logMessage(report);
            reportArea.append("\n"+report);
        }
    }

}
