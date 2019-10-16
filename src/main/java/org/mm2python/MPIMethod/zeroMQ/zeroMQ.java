package org.mm2python.MPIMethod.zeroMQ;

import org.mm2python.mmEventHandler.Executor.MainExecutor;
import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZContext;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.ExecutorService;

public class zeroMQ {

    private static String port;
    private static ZContext context;
    private static ZMQ.Socket socket;

    /**
     * create zeroMQ context and sockets in a PUSH-PULL pattern
     */
    public zeroMQ() {

        ExecutorService executor = MainExecutor.getExecutor();
        port = "5500";
        context = new ZContext();
        socket = context.createSocket(SocketType.PUSH);
        socket.bind(String.format("tcp://*:%s", port));

    }

    /**
     * PUSH image data on this socket as a byte array
     * @param rawpixels : Object.  Retrieved from micro-manager's Image data class
     */
    public static void send(Object rawpixels) {
        byte[] bytepixels = convertToByte(rawpixels);
        socket.send(bytepixels);
    }

    /**
     * get the port this ZMQ socket is bound to
     * @return : String
     */
    public static String getPort() {
        return port;
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
