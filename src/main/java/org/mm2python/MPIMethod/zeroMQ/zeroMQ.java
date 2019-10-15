package org.mm2python.MPIMethod.zeroMQ;

import org.mm2python.UI.reporter;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.ZMQException;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class zeroMQ {

    private static ZMQ.Socket socket;
    private static String port;
    public static ZContext context;
    public static SocketType REP;
    public static SocketType REQ;

    public zeroMQ() {

        port = "5500";
        context = new ZContext();
        REP = SocketType.REP;
        REQ = SocketType.REQ;

        try {
            // Socket to talk to clients
            socket = context.createSocket(SocketType.REP);
            socket.bind(String.format("tcp://*:%s", port));
            waitformessage();
        } catch (Exception ex) {
            reporter.set_report_area(ex.toString());
        }

    }

    /**
     * not necessary if using py4j to fetch
     */
    private void waitformessage() {

        try {

            while (!Thread.currentThread().isInterrupted()) {

                // Block until a message is received
                // this comes from py4jEntryPOint
                byte[] reply = socket.recv(0);
                System.out.println("message received from py4j");

                // call datastructure to send byte array
                socket.send(reply, 0);

            }
        } catch (Exception e) {
            System.out.println("Exception waiting for message "+e.toString());
        }
    }

//    private static void send(Object rawpixels) {
//
////        while (!Thread.currentThread().isInterrupted()) {
//            System.out.println("sending data by zmq");
//            byte[] data = convertToByte(rawpixels);
//            socket.send(data, 0);
////        }
//    }

    public static String getPort() {
        return port;
    }

    public static byte[] convertToByte(Object pixels) throws UnsupportedOperationException {
        try
        {
            byte[] bytes;
            if (pixels instanceof byte[]) {
                bytes = (byte[]) pixels;
            }
            else if (pixels instanceof short[]) {
                ShortBuffer shortPixels = ShortBuffer.wrap((short[]) pixels);
                ByteBuffer dest = ByteBuffer.allocate(2 * ((short[]) pixels).length).order(ByteOrder.nativeOrder());
                ShortBuffer shortDest = dest.asShortBuffer();
                shortDest.put(shortPixels);
                bytes = dest.array();
            }
            else {
                throw new UnsupportedOperationException("Unsupported pixel type");
            }
            return bytes;

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

}
