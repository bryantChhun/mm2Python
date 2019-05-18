package mm2python.DataStructures.Queues;

import mm2python.DataStructures.Constants;
import mm2python.mmDataHandler.Exceptions.NoImageException;
import org.micromanager.data.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.ShortBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * DataStructure to manage a circular queue of filenames
 *  filenames represent memory mapped files based on camera parameters
 *
 */
public class CircularFilenameQueue {

    private static final Queue<String> MMapQueue = new ConcurrentLinkedDeque<>();

    public static void resetQueue() {
        MMapQueue.clear();
    }

    /**
     * populate the "buffer" of blank MMaps which will be replaced by ImageData
     *
     * @param num : number of blank MMaps
     */
    public static void createFilenames(int num) throws FileNotFoundException {
        int bitlength = (int) ((Constants.bitDepth * Constants.width * Constants.height) / 8);

        File directory = new File(Constants.tempFilePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // create num amount of blank mmaps
        if (Constants.getFixedMemMap()) {
            for (int i = 0; i < num; i++) {
                String fixedMapName = Constants.tempFilePath + "/mmap_fixed_" + i;
                writeBlankToMemMap(fixedMapName, bitlength);
                putMemMap(fixedMapName);
            }
        }
    }

    /**
     * check if is empty.  Should never be empty
     * @return : boolean
     */
    public static boolean nextFilenameExists() {
        return !MMapQueue.isEmpty();
    }

    /**
     * pull from head, place back at tail
     * @return : String, head value
     */
    public static String getNextFilename() {
        String next = MMapQueue.poll();
        MMapQueue.offer(next);
//        MMapQueue.add(next);
        return next;
    }

    public static int getHashOfNext() throws NullPointerException{
        return MMapQueue.peek().hashCode();
    }

    // ===== Private methods ======
    private static void putMemMap(String mmap) {
        MMapQueue.add(mmap);
    }

    /**
     * create blank MMap based on supplied byte length
     * @param filename : String, path and filename to temp/MMapFile
     * @param length : int, length in bytes to allocate
     */
    private static void writeBlankToMemMap(String filename, int length) throws FileNotFoundException {
        byte[] bytearray = new byte[length];

        File file = new File(filename);
        file.delete();

        // write data as memmap to memmap file
        try (   FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel())
        {
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
            buffer.put(bytearray);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(ex.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    // ======== below methods are taken from image-to-mmap conversion ============
    // this circular queue doesn't necessarily require these methods.

    /**
     * convert supplied image to memory mapped file with name=filename
     * @param filename : String, name of mmap file
     * @throws NoImageException : null image
     */
    private static void writeToMemMap(String filename, Image temp_img) throws NoImageException {
        byte[] byteimg;

        File file = new File(filename);
        file.delete();

        // write data as memmap to memmap file
        try (   FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel())
        {
            byteimg = convertToByte(temp_img);
            if(byteimg == null) {
                throw new NoImageException("image not converted to byte[]");
            }
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, byteimg.length);
            buffer.put(byteimg);
        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }

    /**
     * convert supplied image to byte array
     * @param tempImg_ : Image
     * @return byte[], byte array
     * @throws UnsupportedOperationException : Image is neither type byte[] nor type short[]
     */
    private static byte[] convertToByte(Image tempImg_) throws UnsupportedOperationException {
        try
        {
            byte[] bytes;
            Object pixels = tempImg_.getRawPixels();
            if (pixels instanceof byte[]) {
                bytes = (byte[]) pixels;
            }
            else if (pixels instanceof short[]) {
                ShortBuffer shortPixels = ShortBuffer.wrap((short[]) pixels);
                ByteBuffer dest = ByteBuffer.allocate(2 * ((short[]) pixels).length).order(ByteOrder.nativeOrder());
                ShortBuffer shortDest = dest.asShortBuffer();
                shortDest.put(shortPixels);
                bytes = dest.array();
            } else {
                throw new UnsupportedOperationException("Unsupported pixel type");
            }
            return bytes;

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
        return null;
    }
}
