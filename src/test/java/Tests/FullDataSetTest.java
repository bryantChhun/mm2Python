package Tests;

import mm2python.DataStructures.FullDataSet;
import mm2python.DataStructures.MetaDataStore;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

public class FullDataSetTest {

    private MetaDataStore mds;
    private MetaDataStore mds1;
    private MetaDataStore mds2;
    private MetaDataStore mds3;

    private FullDataSet fds;

    /**
     * test null MDS catch
     */
    @Test
    void testPutData1()
    {
        mds = null;
        assertThrows(NullPointerException.class, () -> fds.putDataToSet(mds));
    }

    /**
     * test proper hashing of MetaDataStore
     */
    @Test
    void testPutData2()
    {
        // these three mds should have the same hash
        mds = new MetaDataStore(0, 0, "path");
        mds2 = new MetaDataStore(0, 0, "path");
        mds3 = new MetaDataStore(0,0, 0, 0, 1024, 1024, 2, "path");
        fds = new FullDataSet();
        fds.putDataToSet(mds);
        fds.putDataToSet(mds2);
        fds.putDataToSet(mds3);
        assertEquals(1, fds.getAllData().size());
        ArrayList<MetaDataStore> mdslist = fds.getDataFromSetByC(0);
        assertEquals(1, mdslist.size());
    }

    /**
     * test c arraylist retrieval
     */
    @Test
    void testGetDataByC()
    {
        try {
            mds = new MetaDataStore(0, 0, "path");
            mds1 = new MetaDataStore(1, 0, "path");
            mds2 = new MetaDataStore(2, 0, "path");
            mds3 = new MetaDataStore(3, 0, "path");
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            fds = new FullDataSet();
            fds.putDataToSet(mds);
            fds.putDataToSet(mds1);
            fds.putDataToSet(mds2);
            fds.putDataToSet(mds3);
        } catch(Exception ex) {
            fail(ex);
        }

        ArrayList<MetaDataStore> mdslist = fds.getDataFromSetByC(0);
        assertEquals(4, mdslist.size());

    }

    /**
     * test position, channel arraylist retrieval
     */
    @Test
    void testGetDataByP()
    {
        try {
            mds = new MetaDataStore(0, 0, "path");
            mds1 = new MetaDataStore(1, 0, "path");
            mds2 = new MetaDataStore(2, 0, "path");
            mds3 = new MetaDataStore(3, 0, "path");
        } catch(Exception ex) {
            fail(ex);
        }

        try {
            fds = new FullDataSet();
            fds.putDataToSet(mds);
            fds.putDataToSet(mds1);
            fds.putDataToSet(mds2);
            fds.putDataToSet(mds3);
        } catch(Exception ex) {
            fail(ex);
        }

        ArrayList<MetaDataStore> mdslist = fds.getDataFromSetByC(0);
        assertEquals(4, mdslist.size());

    }
}
