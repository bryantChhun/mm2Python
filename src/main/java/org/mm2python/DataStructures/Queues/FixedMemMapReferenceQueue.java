package org.mm2python.DataStructures.Queues;

import org.mm2python.DataStructures.Constants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * DataStructure to manage a circular queue of filenames and memory mapped files
 * Contains both file creation and file assignment/retrieval methods
 *
 * filenames represent memory mapped files based on camera parameters
 *
 */
public class FixedMemMapReferenceQueue {

    private static Queue<String> mmap_filename_queue = new ConcurrentLinkedDeque<>();

    private static Queue<MappedByteBuffer> mmap_buffer_queue = new ConcurrentLinkedDeque<>();

    public static void resetQueues() {
        if(mmap_filename_queue != null){
            mmap_filename_queue.clear();
        }
        if(mmap_buffer_queue != null) {
            mmap_buffer_queue.clear();
        }
    }

    /**
     * populate the "buffer" of blank MMaps which will be replaced by ImageData
     *
     * @param num : number of blank MMaps
     */
    public static void createFileNames(int num) throws FileNotFoundException {
        int bytelength = (int) ((Constants.bitDepth * Constants.width * Constants.height) / 8);

        File directory = new File(Constants.tempFilePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // create num amount of blank mmaps
        if (Constants.getFixedMemMap()) {
            for (int i = 0; i < num; i++) {
                String fixedMapName = Constants.tempFilePath + "/mmap_fixed_" + i+".dat";

                // write filename
                mmap_filename_queue.add(fixedMapName);

                MappedByteBuffer buf = initializeMemMapBuffers(fixedMapName, bytelength);
                mmap_buffer_queue.add(buf);

            }
        }
    }

    // =================== GETTERS for filename, buffer, filechannel ===============

    /**
     * check if is empty.  Should never be empty
     * @return : boolean
     */
    public boolean nextFileNameExists() {
        return !mmap_filename_queue.isEmpty();
    }

    /**
     * poll from head, place back at tail
     * for FILENAME
     * @return : String, head value
     */
    public String getNextFileName() {
        String next = mmap_filename_queue.poll();
        mmap_filename_queue.offer(next);
        return next;
    }

    /**
     * poll from head, place back at tail
     * for MAPPED BYTE BUFFER
     * @return : MappedByteBuffer
     */
    public MappedByteBuffer getNextBuffer() {
        MappedByteBuffer buf = mmap_buffer_queue.poll();
        mmap_buffer_queue.offer(buf);
        return buf;
    }

    // =============== WRITE TO FILE METHODS ==================

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

    /**
     * create blank MMap based on supplied byte length.
     * @param filename : String, path and filename to temp/MMapFile
     * @param length : int, length in bytes to allocate
     * @return : MappedByteBuffer
     */
    private static MappedByteBuffer initializeMemMapBuffers(String filename, int length) throws FileNotFoundException {
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
