package Tests;

import mm2python.DataStructures.Constants;
import mm2python.DataStructures.Maps.Maps;
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
     * integrated method: writing then retrieving from Constants LBQ
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
//        Constants.resetAll();
        new Constants();
        String filename = "testfilename";

        Maps.putChanToFilenameMap(channel_names[0], filename);
        Maps.putChanToFilenameMap(channel_names[1], filename);
        Maps.putChanToFilenameMap(channel_names[2], filename);
        Maps.putChanToFilenameMap(channel_names[3], filename);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename);
        assertEquals(Maps.getNextFileForChannel(channel_names[2]), filename);
        assertEquals(Maps.getNextFileForChannel(channel_names[3]), filename);

    }

    @Test
    void remove_channelname_to_filename_MAP() {
        System.out.println("remove all filenames from each of four channels, confirm empty");
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
//        Constants.resetAll();
        new Constants();
        String filename = "testfilename";

        Maps.putChanToFilenameMap(channel_names[0], filename);
        Maps.putChanToFilenameMap(channel_names[1], filename);
        Maps.putChanToFilenameMap(channel_names[2], filename);
        Maps.putChanToFilenameMap(channel_names[3], filename);

        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[1]);
        Maps.removeChanToFilenameMap(channel_names[2]);
        Maps.removeChanToFilenameMap(channel_names[3]);

        assertNull(Maps.getNextFileForChannel(channel_names[0]));
        assertNull(Maps.getNextFileForChannel(channel_names[1]));
        assertNull(Maps.getNextFileForChannel(channel_names[2]));
        assertNull(Maps.getNextFileForChannel(channel_names[3]));
    }

    /**
     * Writes two channels with three file names
     *  inspects if "removeChanToFilenameMap" properly removes filenames
     */
    @Test
    void remove_channelname_to_filename_MAP_multiple() {
        System.out.println("remove all filenames from each of four channels, confirm empty");
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
//        Constants.resetAll();
        new Constants();
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";
        String filename3 = "testfilename_3";

        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[1], filename1);
        Maps.putChanToFilenameMap(channel_names[0], filename2);
        Maps.putChanToFilenameMap(channel_names[1], filename2);
        Maps.putChanToFilenameMap(channel_names[0], filename3);
        Maps.putChanToFilenameMap(channel_names[1], filename3);

        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[1]);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename2);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename2);

        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[1]);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename3);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename3);
    }

    @Test
    void remove_channelname_to_filename_MAP_multiple_2() {
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
//        Constants.resetAll();
        new Constants();
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";

        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[1], filename2);
        Maps.putChanToFilenameMap(channel_names[1], filename2);
        Maps.putChanToFilenameMap(channel_names[1], filename2);

        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[1]);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename2);

        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[1]);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename2);
    }

    @Test
    void get_nextFileForChannel() {
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
//        Constants.resetAll();
        new Constants();
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";
        String filename3 = "testfilename_3";

        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[1], filename2);
        Maps.putChanToFilenameMap(channel_names[2], filename3);
        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[1], filename2);
        Maps.putChanToFilenameMap(channel_names[2], filename3);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename2);
        assertEquals(Maps.getNextFileForChannel(channel_names[2]), filename3);

        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[1]);
        Maps.removeChanToFilenameMap(channel_names[2]);

        assertEquals(Maps.getNextFileForChannel(channel_names[0]), filename1);
        assertEquals(Maps.getNextFileForChannel(channel_names[1]), filename2);
        assertEquals(Maps.getNextFileForChannel(channel_names[2]), filename3);

    }

    @Test
    void nextImageExists() {
        String[] channel_names = new String[]{"Cy5","DAPI","FITC","Rhodamine"};
//        Constants.resetAll();
        new Constants();
        String filename1 = "testfilename_1";
        String filename2 = "testfilename_2";
        String filename3 = "testfilename_3";
        String filename4 = "testfilename_4";

        Maps.putChanToFilenameMap(channel_names[0], filename1);
        Maps.putChanToFilenameMap(channel_names[0], filename2);
        Maps.putChanToFilenameMap(channel_names[0], filename3);
        Maps.putChanToFilenameMap(channel_names[0], filename4);

        // check that this returns true
        assertTrue(Maps.nextImageExists(channel_names[0]));

        // remove all elements and check that returns false
        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[0]);
        Maps.removeChanToFilenameMap(channel_names[0]);

        assertFalse(Maps.nextImageExists(channel_names[0]));

    }

    // ============ Integration tests ================

}
