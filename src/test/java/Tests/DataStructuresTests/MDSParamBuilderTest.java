package Tests.DataStructuresTests;

import mm2python.DataStructures.Builders.MDSParamBuilder;
import mm2python.DataStructures.Builders.MDSParamObject;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.MetaDataStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * test operation of MetaDataStore Parameter builder
 */
class MDSParamBuilderTest {

    private MDSParameters mdsp;

    private MetaDataStore mds;

    @Test
    void testMDSParamBuilder() {
        try {
            mdsp = new MDSParamBuilder().position(0).z(1).time(2).channel(3).
                    xRange(1024).yRange(2048).bitDepth(16).
                    channel_name("DAPI").prefix("prefix_").windowname("window").
                    filepath("/path/to/this/memorymapfile.dat").buildMDSParams();
        } catch (Exception ex) {
            fail(ex);
        }

        ArrayList<MDSParamObject> params = mdsp.getParams();

        for (MDSParamObject s : params) {
            switch (s.getLabel()) {
                case "TIME":
                    assertEquals(2, s.getInt().intValue());
                    break;
                case "POSITION":
                    assertEquals(0, s.getInt().intValue());
                    break;
                case "Z":
                    assertEquals(1, s.getInt().intValue());
                    break;
                case "CHANNEL":
                    assertEquals(3, s.getInt().intValue());
                    break;

                case "XRANGE":
                    assertEquals(1024, s.getInt().intValue());
                    break;
                case "YRANGE":
                    assertEquals(2048, s.getInt().intValue());
                    break;
                case "BITDEPTH":
                    assertEquals(16, s.getInt().intValue());
                    break;

                case "PREFIX":
                    assertEquals("prefix_", s.getStr());
                    break;
                case "WINDOWNAME":
                    assertEquals("window", s.getStr());
                    break;
                case "CHANNELNAME":
                    assertEquals("DAPI", s.getStr());
                    break;

                case "FILEPATH":
                    assertEquals("/path/to/this/memorymapfile.dat", s.getStr());
                    break;
            }
        }
    }

    /**
     * must call buildMDSParams from MDSParamBuilder
     *  not buildMDS
     */
    @Test
    void testBuildParams() {
        assertThrows(IllegalAccessException.class, () ->
                mds = new MDSParamBuilder().position(0).z(0).time(0).channel(0).
                        xRange(1024).yRange(1024).bitDepth(16).
                        channel_name("DAPI").prefix("prefix_").windowname("window").
                        filepath("/path/to/this/memorymapfile.dat").buildMDS());
    }
}
