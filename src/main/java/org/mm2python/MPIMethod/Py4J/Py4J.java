/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mm2python.MPIMethod.Py4J;

import org.mm2python.DataStructures.Constants;
import org.mm2python.UI.reporter;
import org.micromanager.Studio;
import org.mm2python.MPIMethod.messengerInterface;
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

    /**
     * Constructor sets classloading strategy.
     * micro-manager plugin system has its own classloading strategy, which interferes with py4j
     * @param mm_ : Studio
     */
    public Py4J(Studio mm_) {
        mm = mm_;
        RootClassLoadingStrategy rmmClassLoader = new RootClassLoadingStrategy();
        ReflectionUtil.setClassLoadingStrategy(rmmClassLoader);
    }

    /**
     * Open ports using py4j for python process
     */
    @Override
    public void startConnection() {
        gatewayServer = new GatewayServer(new Py4JEntryPoint(mm));
        gatewayServer.start();
        int port = gatewayServer.getPort();
        Constants.ports.add(port);
        reporter.set_report_area(true, true, true,"Gateway Started at IP:port = "+gatewayServer.getAddress()+":"+gatewayServer.getPort());
    }

    /**
     * close default port
     */
    @Override
    public void stopConnection() {
        gatewayServer.shutdown();
        reporter.set_report_area(true, true, true, "Gateway at default port shut down");
    }

//    /**
//     * close ports for
//     * @param port
//     */
//    @Override
//    public void stopConnection(int port) {
//        gatewayServer.shutdown();
//        reporter.set_report_area(true, true, true, String.format("Gateway at port %04d shut down", port));
//    }

}
