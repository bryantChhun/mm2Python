package Tests;

import mm2python.DataStructures.Builders.MDSBuilder;
import mm2python.DataStructures.Builders.MDSParameters;
import mm2python.DataStructures.MetaDataStore;
import mm2python.DataStructures.Queues.MDSQueue;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class MDSQueueTests {

    private MetaDataStore mds;
    private MDSBuilder mdsb;
    private MDSParameters mdsp;
    private static final MDSQueue mdsq;

    static {
        mdsq = new MDSQueue();
    }

    private MDSQueue buildQueue(int entries) throws IllegalAccessException {
        for(int i=0; i< entries; i++) {
            mds = new MDSBuilder().time(i).buildMDS();
            mdsq.putMDS(mds);
        }
        return mdsq;
    }

    private void clearMDSQueue() {
        mdsq.resetQueue();
    }

    @Test
    void testGetFirst() throws IllegalAccessException {
        buildQueue(40);

        int i = 0;
        while(!mdsq.isMDSQueueEmpty()) {
            mds = mdsq.getFirstMDS();
            assertEquals(i, mds.getTime().intValue());
            i++;
        }
        System.out.println(String.format("traversed %03d timepoints", i));
        clearMDSQueue();
    }

    @Test
    void testGetLast() throws IllegalAccessException {
        int i = 40;
        buildQueue(i);

        while(!mdsq.isMDSQueueEmpty()) {
            mds = mdsq.getLastMDS();
            assertEquals(i-1, mds.getTime().intValue());
            i--;
            System.out.println(i);
        }
        clearMDSQueue();
    }
}
