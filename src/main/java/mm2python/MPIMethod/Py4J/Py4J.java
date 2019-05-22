/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mm2python.MPIMethod.Py4J;

import mm2python.DataStructures.Constants;
import mm2python.UI.reporter;
import org.micromanager.Studio;
import mm2python.MPIMethod.messengerInterface;
import py4j.GatewayServer;
import py4j.reflection.ReflectionUtil;
import py4j.reflection.RootClassLoadingStrategy;


/**
 *
 * @author bryant.chhun
 */
public class Py4J implements messengerInterface {
    private static Studio mm;
    private static GatewayServer gatewayServer;

    public Py4J(Studio mm_) {
        mm = mm_;
        RootClassLoadingStrategy rmmClassLoader = new RootClassLoadingStrategy();
        ReflectionUtil.setClassLoadingStrategy(rmmClassLoader);
    }

    @Override
    public void startConnection(int port) {
        gatewayServer = new GatewayServer(new Py4jEntryPoint(mm), port);
        gatewayServer.start();

        reporter.set_report_area(false, false, "Gateway Started at IP:port = "+gatewayServer.getAddress()+":"+gatewayServer.getPort());
    }
    
    @Override
    public void startConnection() {
        gatewayServer = new GatewayServer(new Py4jEntryPoint(mm));
        gatewayServer.start();
        int port = gatewayServer.getPort();
        Constants.ports.add(port);
        reporter.set_report_area(false, false, "Gateway Started at IP:port = "+gatewayServer.getAddress()+":"+gatewayServer.getPort());
    }
    
    @Override
    public void stopConnection(int port) {
        gatewayServer.shutdown();
        reporter.set_report_area(false, false, String.format("Gateway at port %04d shut down", port));
    }
    
    @Override
    public void stopConnection() {
        Constants.ports.stream().forEach((port) -> {
            gatewayServer.shutdown();
            reporter.set_report_area(false, false, String.format("Gateway at port %04d shut down", port));
        });

    }
}
