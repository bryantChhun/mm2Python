package org.mm2python.MPIMethod.zeroMQ;

import org.mm2python.UI.reporter;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;
import org.zeromq.ZMQException;
//import zmq.Ctx;
//import zmq.SocketBase;
//import zmq.ZMQ;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;


//https://github.com/zeromq/jeromq/blob/master/src/test/java/zmq/socket/pubsub/PubSubHwmTest.java
public class zeroMQ {

    private static String port;
    private static ZContext context;
    public static ZMQ.Socket socket;

    /**
     * create zeroMQ context and sockets in a PUSH-PULL pattern
     */
    public zeroMQ() {

        try {
            context = new ZContext();
            socket = context.createSocket(SocketType.PUSH);
            port = "5500";
            socket.bind(String.format("tcp://*:%s", port));
            socket.setHWM(1);
            reporter.set_report_area(String.format("creating zeroMQ bridge at tcp://*:%s", port));
        } catch (ZMQException zmqe) {
            reporter.set_report_area(String.format("zmq address at tcp://*:%s already exists.\n" +
                    "Shut down then restart bridges", port));
        }
    }

    public static void shutdown() {
        if(context!=null) {
            reporter.set_report_area("shutting down zeroMQ bridge");
            context.destroy();
        }
    }

    /**
     * PUSH image data on this socket as a byte array
     * @param rawpixels : Object.  Retrieved from micro-manager's Image data class
     */
    public static void send(Object rawpixels) {
        long start = System.nanoTime();
        byte[] bytepixels = convertToByte(rawpixels);
        socket.send(bytepixels, ZMQ.DONTWAIT);
//        ByteBuffer pixelbuffer = convertToByteBuffer(rawpixels);
//        socket.sendByteBuffer(pixelbuffer, ZMQ.DONTWAIT);
        long stop = System.nanoTime();
        reporter.set_report_area(String.format("Time to send to sockets (us): %d", (stop-start)/1000));
    }

    /**
     * get the port this ZMQ socket is bound to
     * @return : String
     */
    public static String getPort() {
        return port;
    }

    private static ByteBuffer convertToByteBuffer(Object pixels) {
        try
        {
            byte[] bytes;
            ByteBuffer outbuf;
            if (pixels instanceof byte[]) {
                bytes = (byte[]) pixels;
                outbuf = ByteBuffer.wrap(bytes);
            }
            else if (pixels instanceof short[]) {
                ShortBuffer shortPixels = ShortBuffer.wrap((short[]) pixels);
                ByteBuffer dest = ByteBuffer.allocate(2 * ((short[]) pixels).length).order(ByteOrder.nativeOrder());
                ShortBuffer shortDest = dest.asShortBuffer();
                shortDest.put(shortPixels);
                bytes = dest.array();
                outbuf = ByteBuffer.wrap(bytes);
            }
            else {
                throw new UnsupportedOperationException("Image data is not of type byte[] or short[]");
            }
            return outbuf;
        }
        catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

    /**
     * micro-manager Object can be a byte[] or short[].
     * This method will convert to required byte[] for the zmq socket
     * @param pixels : Object
     * @return : byte[]
     * @throws UnsupportedOperationException : if object is not byte[] or short[]
     */
    private static byte[] convertToByte(Object pixels) throws UnsupportedOperationException {
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
                throw new UnsupportedOperationException("Image data is not of type byte[] or short[]");
            }
            return bytes;

        } catch (Exception ex) {
            System.out.println(ex);
        }
        return null;
    }

}
