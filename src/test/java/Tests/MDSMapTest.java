package Tests;

import mm2python.DataStructures.*;
import mm2python.DataStructures.Builders.MDSBuilder;
import mm2python.DataStructures.Builders.MDSParamBuilder;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.Maps.MDSMap;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * test operation of concurrentHashMap "MDSMap"
 */
class MDSMapTest {

    private MetaDataStore mds;
    private MetaDataStore mds1;
    private MetaDataStore mds2;
    private MetaDataStore mds3;

    private MDSMap fds;

    private MDSParameters mdsp1;

    private void clear() {
        fds.clearData();
    }


    // ========== test get data ===================

    /**
     * test retrieve metadata by calling few hash values
     */
    @Test
    void testGetMeta()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(1).position(0).filepath("path2").buildMDS();
            mds2 = new MDSBuilder().z(2).position(0).filepath("path3").buildMDS();
            mds3 = new MDSBuilder().z(3).position(0).filepath("path4").buildMDS();

            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            fds.putMDS(mds2);
            fds.putMDS(mds3);
            assertEquals("path",
                    fds.getMDSByParams(new MDSParamBuilder().z(0).buildMDSParams()).get(0).getFilepath());
            assertEquals("path2",
                    fds.getMDSByParams(new MDSParamBuilder().z(1).buildMDSParams()).get(0).getFilepath());
            assertEquals("path3",
                    fds.getMDSByParams(new MDSParamBuilder().z(2).buildMDSParams()).get(0).getFilepath());
            assertEquals("path4",
                    fds.getMDSByParams(new MDSParamBuilder().z(3).buildMDSParams()).get(0).getFilepath());
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    /**
     * test retrieve matching MDS key
     */
    @Test
    void testGetDataFromSet()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            assertEquals(mds, fds.getMDS(new MDSBuilder().z(0).position(0).filepath("path").buildMDS()));
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    /**
     * test retrieve matching MDS key
     */
    @Test
    void testGetDataFromSetNull()
    {
        try{
            mds = new MDSBuilder().z(0).position(0).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            assertNull(fds.getMDS(new MDSBuilder().z(1).position(0).filepath("path").buildMDS()));
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    // ========== test put data ===================

    /**
     * test null MDS catch
     */
    @Test
    void testPutDataNull()
    {
        mds = null;
        assertThrows(NullPointerException.class, () -> fds.putMDS(mds));
    }


    /**
     * test proper hashing of MetaDataStore
     */
    @Test
    void testPutData()
    {
        try{
            // these three mds should have the same hash
            mds = new MDSBuilder().z(0).position(0).channel(0).filepath("path").buildMDS();
            mds2 = new MDSBuilder().z(0).position(0).channel(0).filepath("path").buildMDS();
            mds3 = new MDSBuilder().z(0).position(0).channel(0).xRange(1024).yRange(1024).bitDepth(2).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds2);
            fds.putMDS(mds3);
            assertEquals(1, fds.getSize());
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    /**
     * test unique hashing of MetaDataStore
     *  z-hashing
     */
    @Test
    void testPutDataZ()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).time(0).channel(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(1).position(0).time(0).channel(0).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            assertEquals(2, fds.getSize());
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    /**
     * test unique hashing of MetaDataStore
     *  position-hashing
     */
    @Test
    void testPutDataP()
    {
        try{
            mds = new MDSBuilder().z(0).position(0).time(0).channel(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(0).position(1).time(0).channel(0).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            assertEquals(2, fds.getSize());
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    /**
     * test unique hashing of MetaDataStore
     *  time-hashing
     */
    @Test
    void testPutDataT()
    {
        try{
            mds = new MDSBuilder().z(0).position(0).time(0).channel(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(0).position(0).time(1).channel(0).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            assertEquals(2, fds.getSize());
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }

    /**
     * test unique hashing of MetaDataStore
     *  channel-hashing
     */
    @Test
    void testPutDataC()
    {
        try{
            mds = new MDSBuilder().z(0).position(0).time(0).channel(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(0).position(0).time(0).channel(1).filepath("path").buildMDS();
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            assertEquals(2, fds.getSize());
        } catch(IllegalAccessException ex) {
            fail(ex);
        }
        clear();
    }


    // ========== test get data ===================

    /**
     * test c arraylist retrieval
     */
    @Test
    void testGetDataByC()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).channel(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(1).position(0).channel(0).filepath("path").buildMDS();
            mds2 = new MDSBuilder().z(0).position(0).channel(1).filepath("path").buildMDS();
            mds3 = new MDSBuilder().z(1).position(0).channel(1).filepath("path").buildMDS();
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            fds.putMDS(mds2);
            fds.putMDS(mds3);
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            mdsp1 = new MDSParamBuilder().channel(0).buildMDSParams();
        } catch(Exception ex) {
            fail(ex);
        }

        ArrayList<MetaDataStore> mdslist = fds.getMDSByParams(mdsp1);
        assertEquals(2, mdslist.size());
        clear();
    }

    /**
     * test position, channel arraylist retrieval
     */
    @Test
    void testGetDataByP()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).channel(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(1).position(0).channel(0).filepath("path").buildMDS();
            mds2 = new MDSBuilder().z(0).position(0).channel(1).filepath("path").buildMDS();
            mds3 = new MDSBuilder().z(1).position(1).channel(1).filepath("path").buildMDS();
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            fds.putMDS(mds2);
            fds.putMDS(mds3);
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            mdsp1 = new MDSParamBuilder().position(0).buildMDSParams();
        } catch(Exception ex) {
            fail(ex);
        }

        ArrayList<MetaDataStore> mdslist = fds.getMDSByParams(mdsp1);
        assertEquals(3, mdslist.size());
        clear();
    }

    /**
     * test position, channel arraylist retrieval
     */
    @Test
    void testGetDataByT()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).channel(0).time(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(1).position(0).channel(0).time(0).filepath("path").buildMDS();
            mds2 = new MDSBuilder().z(0).position(1).channel(1).time(0).filepath("path").buildMDS();
            mds3 = new MDSBuilder().z(1).position(1).channel(1).time(1).filepath("path").buildMDS();
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            fds.putMDS(mds2);
            fds.putMDS(mds3);
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            mdsp1 = new MDSParamBuilder().time(0).buildMDSParams();
        } catch(Exception ex) {
            fail(ex);
        }

        ArrayList<MetaDataStore> mdslist = fds.getMDSByParams(mdsp1);
        assertEquals(3, mdslist.size());
        clear();
    }

    @Test
    void testGetDataByParams()
    {
        try {
            mds = new MDSBuilder().z(0).position(0).channel(0).time(0).filepath("path").buildMDS();
            mds1 = new MDSBuilder().z(1).position(0).channel(0).time(0).filepath("path").buildMDS();
            mds2 = new MDSBuilder().z(0).position(1).channel(1).time(0).filepath("path").buildMDS();
            mds3 = new MDSBuilder().z(1).position(1).channel(1).time(1).filepath("path").buildMDS();
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            fds = new MDSMap();
            fds.putMDS(mds);
            fds.putMDS(mds1);
            fds.putMDS(mds2);
            fds.putMDS(mds3);
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            mdsp1 = new MDSParamBuilder().channel(1).position(1).buildMDSParams();
        } catch(Exception ex) {
            fail(ex);
        }

        ArrayList<MetaDataStore> mdslist = fds.getMDSByParams(mdsp1);
        assertEquals(2, mdslist.size());
        assertEquals(1, mdslist.get(0).getPosition().intValue());
        assertEquals(1, mdslist.get(0).getChannel().intValue());
        assertEquals(0, mdslist.get(0).getTime().intValue());
        assertEquals(0, mdslist.get(0).getZ().intValue());
        assertEquals(1, mdslist.get(1).getTime().intValue());
        assertEquals(1, mdslist.get(1).getZ().intValue());
        clear();
    }


}
