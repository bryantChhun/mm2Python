package Tests.DataStructuresTests;

import org.mm2python.DataStructures.Constants;
import org.mm2python.DataStructures.Queues.FixedMemMapReferenceQueue;
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

class FixedMemMapReferenceQueueTests {

    private FixedMemMapReferenceQueue fixed;
    //TODO: mock the file names

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
        Constants.setFixedMemMap(true);
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
        fixed = new FixedMemMapReferenceQueue();
    }

    /**
     * test creation of memory maps
     */
    @Test
    void testCreateMMap() {
        initializeConstants();
        setUp();

        try {
            FixedMemMapReferenceQueue.createFileNames(10);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(fixed.nextFileNameExists());
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
            FixedMemMapReferenceQueue.createFileNames(10);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(fixed.nextFileNameExists());
        FixedMemMapReferenceQueue.resetQueues();
        assertFalse(fixed.nextFileNameExists());
        clearTempFiles();
    }

    /**
     * test simple retrieval of next file
     */
    @Test
    void testGetNext() {
        initializeConstants();
        setUp();

        try {
            FixedMemMapReferenceQueue.createFileNames(10);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(fixed.nextFileNameExists());
        String next = fixed.getNextFileName();
        assertEquals(String.class, next.getClass());
        clearTempFiles();
    }

    /**
     * test looping behavior of queue
     */
    @Test
    void testGetNextCircular() {
        initializeConstants();
        setUp();
        int num = 10;

        try {
            FixedMemMapReferenceQueue.createFileNames(num);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(fixed.nextFileNameExists());
        for(int i = 0; i<(2*num); i++) {
            String next = fixed.getNextFileName();
            assertEquals(String.class, next.getClass());
        }
        clearTempFiles();
    }

    /**
     * test looping and concurrency: finite number of filenames
     *  - retrieves 2*num filenames from queue
     *  - latch forces all "getNextFileNames" to occur simultaneously
     *  - should observe exactly 10 unique names even though 20 are retrieved
     */
    @Test
    void testGetNextConcurrent_finite() {
        // follow: https://dzone.com/articles/how-i-test-my-java-classes-for-thread-safety
        initializeConstants();
        setUp();

        int num = 10;

        try {
            FixedMemMapReferenceQueue.createFileNames(num);
        } catch (Exception ex) {
            fail(ex);
        }

        // create threads and submit getNextFileName
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(num);
        Collection<Future<String>> futures = new ArrayList<>(num);
        int retrievals = 2*num;
        for (int t = 0; t < retrievals; ++t) {
            futures.add(service.submit(
                    () -> {
                        latch.await();
                        return fixed.getNextFileName();
                    }
            ));
        }
        latch.countDown();

        // retrieve number of unique hashes from futures
        Set<String> ids = new HashSet<>();
        for (Future<String> f : futures) {
            try {
                ids.add(f.get());
            } catch (Exception ex) {
                System.out.println("exception checking unique threads "+ex);
            }
        }

        // even though we do 20 retrievals on 10 files, we still have only 10 unique IDs
        // importantly, we do not have fewer than 10 unique IDs
        assertEquals(ids.size(), num);
        assertEquals(2*ids.size(), retrievals);
        clearTempFiles();
    }

    /**
     * test looping and concurrency: 10 unique names retrieved
     *  - concurrent threads will not retrieve the same name
     *
     */
    @Test
    void testGetNextConcurrent_order() {
        // follow: https://dzone.com/articles/how-i-test-my-java-classes-for-thread-safety
        initializeConstants();
        setUp();
        int num = 20;

        try {
            FixedMemMapReferenceQueue.createFileNames(num);
        } catch (Exception ex) {
            fail(ex);
        }

        // create threads and submit getNextFileName
        CountDownLatch latch = new CountDownLatch(1);
        ExecutorService service = Executors.newFixedThreadPool(num);
        Collection<Future<String>> futures = new ArrayList<>(num);
        int retrievals = num/2;
        for (int t = 0; t < retrievals; ++t) {
            futures.add(service.submit(
                    () -> {
                        latch.await();
                        return fixed.getNextFileName();
                    }
            ));
        }
        latch.countDown();

        // retrieve number of unique hashes from futures
        Set<String> ids = new HashSet<>();
        for (Future<String> f : futures) {
            try {
                ids.add(f.get());
            } catch (Exception ex) {
                System.out.println("exception checking unique threads "+ex);
            }
        }

        assertEquals(ids.size(), num/2);

        clearTempFiles();
    }

}
