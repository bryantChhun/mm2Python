package Tests.EventHandlerTests;

import org.junit.jupiter.api.Test;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.acquisition.AcquisitionManager;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.data.Image;
import org.micromanager.data.SummaryMetadata;
import org.mm2python.DataStructures.Maps.MDSMap;
import org.mm2python.DataStructures.Queues.DynamicMemMapReferenceQueue;
import org.mm2python.DataStructures.Queues.FixedMemMapReferenceQueue;
import org.mm2python.DataStructures.Queues.MDSQueue;
import org.mm2python.UI.reporter;
import org.mm2python.mmEventHandler.datastoreEventsThread;

import javax.swing.*;
import javax.swing.text.Document;

import java.nio.MappedByteBuffer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


class DatastoreEventsThreadTests {

    private Studio mm;
    private Datastore ds;
    private Coords coord;
    private Image im;
    private SummaryMetadata smd;
    private DynamicMemMapReferenceQueue dynamic;
    private FixedMemMapReferenceQueue fixed;
    private MDSMap mdsm;
    private MDSQueue mdsq;

    private void setUp() {
        mm = mock(Studio.class);

        // mocking methods needed by reporter
        //  JText Area
        JTextArea jta = mock(JTextArea.class);
        doNothing().when(jta).append(anyString());
        Document doc = mock(Document.class);
        when(jta.getDocument()).thenReturn(doc);
        when(doc.getLength()).thenReturn(0);
        doNothing().when(jta).setCaretPosition(anyInt());
        new reporter(jta, mm);

        //  mm log manager
        LogManager lm = mock(LogManager.class);
        when(mm.logs()).thenReturn(lm);
        doNothing().when(lm).logMessage(anyString());

        // mocking methods needed by testrunnable
        ds = mock(Datastore.class);
        SequenceSettings ss = mock(SequenceSettings.class);
        AcquisitionManager aq = mock(AcquisitionManager.class);
        when(mm.acquisitions()).thenReturn(aq);
        when(aq.getAcquisitionSettings()).thenReturn(ss);

        // mock for constructor
        coord = mock(Coords.class);
        im = mock(Image.class);
        smd = mock(SummaryMetadata.class);
        when(smd.getChannelNames()).thenReturn(new String[] {"mock ch0", "mock ch1"});
        when(coord.getChannel()).thenReturn(1);
        when(im.getWidth()).thenReturn(2048);
        when(im.getHeight()).thenReturn(2048);
        when(im.getBytesPerPixel()).thenReturn(2);

        // not possible to mock
        // mock fixed and dynamic memmap reference
        datastoreEventsThread det = spy(new datastoreEventsThread(ds,
                coord,
                im,
                smd,
                "chan",
                "prefix",
                "window"));
        fixed = mock(FixedMemMapReferenceQueue.class);

        when(fixed.getNextFileName()).thenReturn("mocked_fixed_filename");
        MappedByteBuffer mbb = mock(MappedByteBuffer.class);
        when(fixed.getNextBuffer()).thenReturn(mbb);

        dynamic = mock(DynamicMemMapReferenceQueue.class);
        when(dynamic.getCurrentFileName()).thenReturn("mocked_dyanmic_filename");
        when(dynamic.getCurrentBuffer()).thenReturn(mbb);
        when(dynamic.getCurrentPosition()).thenReturn(0);

        // mock MDSMap, MDSQueue
        mdsm = mock(MDSMap.class);
        mdsq = mock(MDSQueue.class);

    }

    @Test
    void testConstructor() {
//        setUp();
//
//        datastoreEventsThread deth = new datastoreEventsThread(ds,
//                coord,
//                im,
//                smd,
//                "current channel",
//                "prefix",
//                "window name");
//
//        assertEquals(deth, datastoreEventsThread.class);


    }

    /**
     * need to test sub methods using multiple constructors with different passed parameters
     * "testing private methods through the public methods"
     */

    @Test
    void testRunnableWriteToMemMap() {

    }

    @Test
    void testRunnableMakeMDS() {

    }

    @Test
    void testRunnableWriteToHashMap() {

    }

    @Test
    void testRunnableWriteToQueues() {

    }

    @Test
    void testRunnableNotifyListeners() {

    }

    @Test
    void testGetFileName() {

    }

}
