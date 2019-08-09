package mm2python.DataStructures.Queues;

import mm2python.DataStructures.Constants;
import mm2python.UI.reporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This datastructure creates a memory mapped buffer on disk of varying size and manages positions
 * It is not responsible for assigning data to the memory mapped buffer
 *
 * writable position in the buffer is managed by a concurrent Queue.
 */
public class DynamicMemMapReferenceQueue {

    private static int bytelength;
    private static String dynamicMapname;
    private static MappedByteBuffer current_buf;

    private static int filelength;
    private static int index = 0;
    private static int num_channels;
    private static int num_z;

    private static Queue<Integer> positions = new ConcurrentLinkedQueue<>();

    public DynamicMemMapReferenceQueue(int num_channels_, int num_z_) {

        num_channels = num_channels_;
        num_z = num_z_;

        // bytes per image
        bytelength = (int) ((Constants.bitDepth * Constants.width * Constants.height) / 8);

        // images per memmap file
        filelength = num_channels*num_z*bytelength;

        File directory = new File(Constants.tempFilePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // create a single, large blank memmap
        createDynamicMemMapStore();
    }

    /**
     * creates a single large memory mapped file (integer multiple of num image's size)
     *  - creates a new file name by incrementing the index
     *  - adds the new large mmap file to a queue
     *  - populates a concurrent positions queue that is popped every time current buffer is queried
     */
    private static void createDynamicMemMapStore() {
        if (!Constants.getFixedMemMap()) {
            dynamicMapname = Constants.tempFilePath + "/mmap_dynamic_" + index;
            index += 1;

            try {
                current_buf = createLargeMemMapBuffer(dynamicMapname, filelength);
            } catch(Exception ex) {
                reporter.set_report_area("Exception creating dynamic map :"+ex);
            }

            // positions for each image in memmap file (in queue)
            positions.clear();
            for(int i=0; i<num_channels*num_z; i++){
                positions.add(i*bytelength);
            }
        }
    }

    // if called before getCurrentBuffer and positionQueue is empty, will fail.
    public static int getCurrentPosition() {
        return positions.remove();
    }

    public static boolean isEmpty() {
        return positions.isEmpty();
    }

    public static MappedByteBuffer getCurrentBuffer() {
        if(isEmpty()) {
            createDynamicMemMapStore();
            return current_buf;
        } else {
            return current_buf;
        }
    }

    public static int getCurrentByteLength() {
        return bytelength;
    }

    public static String getCurrentFileName() {
        return dynamicMapname;
    }

    public static void clear() {
//        current_buf.position(0);
        positions.clear();
    }

    public static void resetAll() {
        clear();
        index = 0;
    }

    /**
     * create blank MMap based on supplied byte length.
     *  returned buffer position is at the END of the data
     * @param filename : String, path and filename to temp/MMapFile
     * @param length : int, length in bytes to allocate
     */
    private static MappedByteBuffer createLargeMemMapBuffer(String filename, int length) throws FileNotFoundException {
        byte[] bytearray = new byte[length];

        MappedByteBuffer buffer = null;

        File file = new File(filename);
        file.delete();

        // write data as memmap to memmap file
        try (FileChannel fileChannel = new RandomAccessFile(file, "rw").getChannel())
        {
            buffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, length);
            buffer.put(bytearray);
            buffer.force();
            buffer.position(0);
        } catch (FileNotFoundException ex) {
            throw new FileNotFoundException(ex.toString());
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        return buffer;
    }

}
