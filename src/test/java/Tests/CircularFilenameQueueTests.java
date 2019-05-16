package Tests;

import mm2python.DataStructures.Constants;
import mm2python.DataStructures.Queues.CircularFilenameQueue;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

//todo: create test for concurrency
class CircularFilenameQueueTests {

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
    private void initializeMMaps() {
        Constants.setFixedMemMap(true);
        Constants.bitDepth=16;
        Constants.width=2048;
        Constants.height=2048;
        switch(Constants.OS) {
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

    /**
     * test creation of memory maps
     */
    @Test
    void testCreateMMap() {
        initializeMMaps();

        try {
            CircularFilenameQueue.createFilenames(10);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(CircularFilenameQueue.nextFilenameExists());
        clearTempFiles();

    }

    /**
     * test resetQueue
     */
    @Test
    void testClear() {
        initializeMMaps();

        try {
            CircularFilenameQueue.createFilenames(10);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(CircularFilenameQueue.nextFilenameExists());
        CircularFilenameQueue.resetQueue();
        assertFalse(CircularFilenameQueue.nextFilenameExists());
        clearTempFiles();
    }

    /**
     * test simple retrieval of next file
     */
    @Test
    void testGetNext() {
        initializeMMaps();

        try {
            CircularFilenameQueue.createFilenames(10);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(CircularFilenameQueue.nextFilenameExists());
        String next = CircularFilenameQueue.getNextFilename();
        assertEquals(String.class, next.getClass());
        clearTempFiles();
    }

    /**
     * test looping behavior of queue
     */
    @Test
    void testGetNextCircular() {
        initializeMMaps();
        int num = 10;

        try {
            CircularFilenameQueue.createFilenames(num);
        } catch (Exception ex) {
            fail(ex);
        }

        assertTrue(CircularFilenameQueue.nextFilenameExists());
        for(int i = 0; i<(2*num); i++) {
            String next = CircularFilenameQueue.getNextFilename();
            assertEquals(String.class, next.getClass());
        }
        clearTempFiles();
    }

    /**
     * test looping and concurrency
     * TODO: change all queues to LBQ
     *      can cycle twice and show that we have 2*number of unique names
     * TODO: implement this concurrent test for the concurrent HashMap
     */
    @Test
    void testGetNextConcurrent() {
        initializeMMaps();
        // follow: https://dzone.com/articles/how-i-test-my-java-classes-for-thread-safety

    }

}
