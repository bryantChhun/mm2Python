package Tests.DataStructuresTests;

import org.mm2python.DataStructures.Constants;
import org.mm2python.DataStructures.Queues.DynamicMemMapReferenceQueue;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.*;
import java.nio.MappedByteBuffer;

//todo: write data placement tests
//todo: rewrite offset tests
class DynamicMemMapReferenceQueueTests {

    private int NUM_CHANNELS = 2;
    private int NUM_Z = 10;
    private DynamicMemMapReferenceQueue dynamic;

    // ========= SETUP CODE =====================
    /**
     * to delete generated temp files and temp directory
     */
    private void clearTempFiles() {
        File index = new File(Constants.tempFilePath);
        String[] entries = index.list();

        for(String s: entries) {
            try {
                File currentFile = new File(index.getPath(), s);
                currentFile.delete();
            } catch (Exception ex) {
                System.out.println("skipped a file");
            }
        }
        File dir = new File(Constants.tempFilePath);
        dir.delete();
    }

    /**
     * to initialize the constants with reasonable image values and temp file directory
     */
    private void initializeConstants() {
        Constants.setFixedMemMap(false);
        Constants.bitDepth=16;
        Constants.width=2048;
        Constants.height=2048;
        switch(Constants.getOS()) {
            case "win":
                Constants.tempFilePath = "C:\\mm2pythonTemp";
                break;
            case "mac":
                String username = System.getProperty("user.name");
                Constants.tempFilePath =
                        String.format("/Users/%s/Desktop/mm2pythonTemp",
                                username);
                break;
            default:
                fail("no OS found");
        }
    }

    private void setUp() {
        dynamic = new DynamicMemMapReferenceQueue();
    }

    // ========= TESTS =====================

    /**
     * test creation of memory maps
     */
    @Test
    void testCreateMMap() {
        initializeConstants();
        setUp();

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        assertFalse(dynamic.isEmpty());
        assertEquals(0, dynamic.getCurrentPosition());
        assertEquals(16*2048*2048/8, dynamic.getCurrentByteLength());

        clearTempFiles();

    }

    /**
     * test resetQueues
     */
    @Test
    void testClear() {
        initializeConstants();
        setUp();

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        dynamic.resetQueue();
        assertTrue(dynamic.isEmpty());

        clearTempFiles();
    }

    /**
     * test simple retrieval of next file
     */
    @Test
    void testGetNextFileName() {
        initializeConstants();
        setUp();

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        String next = dynamic.getCurrentFileName();
        assertEquals(String.class, next.getClass());

        clearTempFiles();
    }

    /**
     * test position automatic increment after current block is filled
     */
    @Test
    void testPositionIncrement() {
        initializeConstants();
        setUp();

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        int bytelength = dynamic.getCurrentByteLength();

        int i = 0;
        while(!dynamic.isEmpty()) {
            int off = dynamic.getCurrentPosition();
            assertEquals(i, off);
            i += bytelength;
        }
        assertEquals(NUM_CHANNELS*NUM_Z*bytelength, i);

        clearTempFiles();
    }

    /**
     * test that new files are created when current file is filled
     */
    @Test
    void testDynamicCreation() {
        initializeConstants();
        setUp();

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        int position = -1;
        String firstFileName = dynamic.getCurrentFileName();

        for(int i =0; i < NUM_CHANNELS*NUM_Z + 1; i++) {
            MappedByteBuffer buf = dynamic.getCurrentBuffer();
            position = dynamic.getCurrentPosition();
        }
        assertEquals(0, position);
        assertNotEquals(firstFileName, dynamic.getCurrentFileName());

        clearTempFiles();
    }

    /**
     * test simple data placement
     */
    @Test
    void testDataPlacement() {
        initializeConstants();
        setUp();

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        byte[] array = new byte[512*512];
        MappedByteBuffer buf = dynamic.getCurrentBuffer();
        int pos = dynamic.getCurrentPosition();
        buf.put(array, 0 , pos);
        buf.force();

        clearTempFiles();

    }



    /**
     * test looping and concurrency : finite number of positions retrieved
     *  - concurrent threads will not retrieve the same position
     *
     */
    @Test
    void testGetNextConcurrent() {
        // follow: https://dzone.com/articles/how-i-test-my-java-classes-for-thread-safety
        initializeConstants();
        setUp();

        // make sure num is < num_channels*num_z
        int num = 10;

        try {
            dynamic.createFileNames(NUM_CHANNELS, NUM_Z);
        } catch (Exception ex) {
            fail(ex);
        }

        // create threads and submit getNextFileName
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(num);
        Collection<Future<Integer>> futures = new ArrayList<>(num);
        for (int t = 0; t < num; ++t) {
            futures.add(service.submit(
                    () -> {
                        latch.await();
                        return dynamic.getCurrentPosition();
                    }
            ));
        }
        latch.countDown();

        // retrieve number of unique hashes from futures
        Set<Integer> ids = new HashSet<>();
        for (Future<Integer> f : futures) {
            try {
                ids.add(f.get());
            } catch (Exception ex) {
                System.out.println("exception checking unique threads "+ex);
            }
        }

        // unlike in the FixedMemmap case,
        assertEquals(num, ids.size());

        clearTempFiles();
    }

}
