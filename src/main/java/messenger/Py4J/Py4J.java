/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package messenger.Py4J;

import org.micromanager.Studio;
import messenger.messengerInterface;
import UI.reports;
import py4j.GatewayServer;
import javax.swing.JTextArea;
import Constants.constants;


/**
 *
 * @author bryant.chhun
 */
public class Py4J implements messengerInterface {
    private static Studio mm;
    private static GatewayServer gatewayServer;
    private final reports reports;
    
    
    public Py4J(Studio mm_, JTextArea UI_textArea) {
        mm = mm_;
        reports = new reports(UI_textArea);
    }
    //TODO: write port management methods
    
    @Override
    public void startConnection(int port) {
        gatewayServer = new GatewayServer(new Py4jEntryPoint(mm), port);
        gatewayServer.start();
        reports.set_report_area("Gateway Started at IP:port = "+gatewayServer.getAddress()+":"+gatewayServer.getPort());
        mm.logs().logMessage("Gateway Started at port = "+port);
    }
    
    @Override
    public void startConnection() {
        gatewayServer = new GatewayServer(new Py4jEntryPoint(mm));
        gatewayServer.start();
        int port = gatewayServer.getPort();
        constants.ports.add(port);
        reports.set_report_area("Gateway Started at IP:port = "+gatewayServer.getAddress()+":"+gatewayServer.getPort());
        mm.logs().logMessage("Gateway Started at port: "+port);
    }
    
    @Override
    public void stopConnection(int port) {
        //TODO: clean up the shutdown and restart of connection
        //TODO: write tests that probe whether this is properly shut down
        gatewayServer.shutdown();
        reports.set_report_area(String.format("Gateway at port %04d shut down", port));
        mm.logs().logMessage(String.format("Gateway at port %04d shut down", port));
    }
    
    @Override
    public void stopConnection() {
        constants.ports.stream().forEach((port) -> {
            gatewayServer.shutdown();
            reports.set_report_area(String.format("Gateway at port %04d shut down", port));
            mm.logs().logMessage(String.format("Gateway at port %04d shut down", port));
        });

    }
}
