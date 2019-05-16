package Tests;

import mm2python.DataStructures.Builders.MDSBuilder;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.MetaDataStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * test operation of MetaDataStore builder
 */
class MDSBuilderTest {

    private MetaDataStore mds;
    private MetaDataStore mds1;
    private MetaDataStore mds2;
    private MetaDataStore mds3;

    private MDSParameters mdsp;

    /**
     * tests to write:
     * build full mds
     * check hash on small mds
     *
     */

    @Test
    void testBuild() {
        try {
            mds = new MDSBuilder().position(0).z(1).time(2).channel(3).
                    xRange(1024).yRange(1024).bitDepth(16).
                    channel_name("DAPI").prefix("prefix_").windowname("window").
                    filepath("/path/to/this/memorymapfile.dat").buildMDS();
        } catch (Exception ex) {
            fail(ex);
        }
        assertEquals(0, mds.getPosition().intValue());
        assertEquals(1, mds.getZ().intValue());
        assertEquals(2, mds.getTime().intValue());
        assertEquals(3, mds.getChannel().intValue());

        assertEquals(1024, mds.getxRange().intValue());
        assertEquals(1024, mds.getyRange().intValue());
        assertEquals(16, mds.getBitDepth().intValue());

        assertEquals("DAPI", mds.getChannelName());
        assertEquals("prefix_", mds.getPrefix());
        assertEquals("window", mds.getWindowName());

        assertEquals("/path/to/this/memorymapfile.dat", mds.getFilepath());
    }

    @Test
    void testBuildHash() {
        try{
            mds = new MDSBuilder().position(0).z(1).time(2).channel(3).
                    xRange(1024).yRange(1024).bitDepth(16).
                    channel_name("DAPI").prefix("prefix_").windowname("window").
                    filepath("/path/to/this/memorymapfile.dat").buildMDS();
            mds1 = new MDSBuilder().position(0).z(1).time(2).channel(3).
                    buildMDS();
            mds2 = new MDSBuilder().position(0).z(2).time(2).channel(3).
                    xRange(1024).yRange(1024).bitDepth(16).
                    buildMDS();
            mds3 = new MDSBuilder().position(0).z(2).time(2).channel(3).
                    buildMDS();
        } catch (Exception ex) {
            fail(ex);
        }
        assertEquals(mds, mds1);
        assertNotSame(mds, mds2);
        assertNotSame(mds, mds3);

        assertEquals(mds2, mds3);
        assertNotSame(mds2, mds1);
        assertNotSame(mds2, mds);

    }

    /**
     * must call buildMDS from MDSBuilder
     *  not buildMDSParams
     */
    @Test
    void testBuildParams() {
        assertThrows(IllegalAccessException.class, () ->
                mdsp = new MDSBuilder().position(0).z(0).time(0).channel(0).
                xRange(1024).yRange(1024).bitDepth(16).
                channel_name("DAPI").prefix("prefix_").windowname("window").
                filepath("/path/to/this/memorymapfile.dat").buildMDSParams());
    }


}
