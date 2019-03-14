package Tests;

import mm2python.DataStructures.constants;
import org.junit.jupiter.api.Test;

import org.micromanager.Studio;

import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTests {

    private Studio mm;

    /**
     * tests to write:
     *
     * inputs and outputs for each method
     *
     * integrated method: writing then retrieving from constants LBQ
     *
     *
     */

    @Test
    void constants_constructor() {

    }

    @Test
    void constants_resetAll() {

    }

    // ============ MetaData Object maps ========================
    @Test
    void put_meta_to_filename_MAP() {

    }

    @Test
    void put_channelname_to_meta_MAP() {

    }

    @Test
    void remove_channelname_to_meta_MAP() {

    }

    // ============ Channelname to Filename maps ================
    @Test
    void put_channelname_to_filename_MAP() {
        System.out.println("put one filename in each of four channels");
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
        constants.resetAll();
        new constants(mm);
        String filename = "testfilename";

        constants.putChanToFilenameMap(channel_names[0], filename);
        constants.putChanToFilenameMap(channel_names[1], filename);
        constants.putChanToFilenameMap(channel_names[2], filename);
        constants.putChanToFilenameMap(channel_names[3], filename);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename);
        assertEquals(constants.getNextFileForChannel(channel_names[2]), filename);
        assertEquals(constants.getNextFileForChannel(channel_names[3]), filename);

    }

    @Test
    void remove_channelname_to_filename_MAP() {
        System.out.println("remove all filenames from each of four channels, confirm empty");
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
        constants.resetAll();
        new constants(mm);
        String filename = "testfilename";

        constants.putChanToFilenameMap(channel_names[0], filename);
        constants.putChanToFilenameMap(channel_names[1], filename);
        constants.putChanToFilenameMap(channel_names[2], filename);
        constants.putChanToFilenameMap(channel_names[3], filename);

        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[1]);
        constants.removeChanToFilenameMap(channel_names[2]);
        constants.removeChanToFilenameMap(channel_names[3]);

        assertNull(constants.getNextFileForChannel(channel_names[0]));
        assertNull(constants.getNextFileForChannel(channel_names[1]));
        assertNull(constants.getNextFileForChannel(channel_names[2]));
        assertNull(constants.getNextFileForChannel(channel_names[3]));
    }

    /**
     * Writes two channels with three file names
     *  inspects if "removeChanToFilenameMap" properly removes filenames
     */
    @Test
    void remove_channelname_to_filename_MAP_multiple() {
        System.out.println("remove all filenames from each of four channels, confirm empty");
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
        constants.resetAll();
        new constants(mm);
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";
        String filename3 = "testfilename_3";

        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[1], filename1);
        constants.putChanToFilenameMap(channel_names[0], filename2);
        constants.putChanToFilenameMap(channel_names[1], filename2);
        constants.putChanToFilenameMap(channel_names[0], filename3);
        constants.putChanToFilenameMap(channel_names[1], filename3);

        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[1]);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename2);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename2);

        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[1]);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename3);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename3);
    }

    @Test
    void remove_channelname_to_filename_MAP_multiple_2() {
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
        constants.resetAll();
        new constants(mm);
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";

        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[1], filename2);
        constants.putChanToFilenameMap(channel_names[1], filename2);
        constants.putChanToFilenameMap(channel_names[1], filename2);

        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[1]);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename2);

        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[1]);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename2);
    }

    @Test
    void get_nextFileForChannel() {
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
        constants.resetAll();
        new constants(mm);
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";
        String filename3 = "testfilename_3";

        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[1], filename2);
        constants.putChanToFilenameMap(channel_names[2], filename3);
        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[1], filename2);
        constants.putChanToFilenameMap(channel_names[2], filename3);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename2);
        assertEquals(constants.getNextFileForChannel(channel_names[2]), filename3);

        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[1]);
        constants.removeChanToFilenameMap(channel_names[2]);

        assertEquals(constants.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(constants.getNextFileForChannel(channel_names[1]), filename2);
        assertEquals(constants.getNextFileForChannel(channel_names[2]), filename3);

    }

    @Test
    void nextImageExists() {
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
        constants.resetAll();
        new constants(mm);
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";
        String filename3 = "testfilename_3";
        String filename4 = "testfilename_4";

        constants.putChanToFilenameMap(channel_names[0], filename1);
        constants.putChanToFilenameMap(channel_names[0], filename2);
        constants.putChanToFilenameMap(channel_names[0], filename3);
        constants.putChanToFilenameMap(channel_names[0], filename4);

        // check that this returns true
        assertTrue(constants.nextImageExists(channel_names[0]));

        // remove all elements and check that returns false
        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[0]);
        constants.removeChanToFilenameMap(channel_names[0]);

        assertFalse(constants.nextImageExists(channel_names[0]));

    }

    // ============ Integration tests ================

}
