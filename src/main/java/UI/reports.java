/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import javax.swing.JTextArea;

/**
 * send messages to UI display
 * @author bryant.chhun
 */
public class reports {
    private static JTextArea reportArea;
    
    public reports(JTextArea UI_logger_textarea) {
        reportArea = UI_logger_textarea;
    }
    
    public void set_report_area(String report) {	
        String newline = "\n";
        //reportArea.append("");
        //reportArea.setText(null);
        reportArea.append(newline+report);
    }
    
}
