package Tests.EventHandlerTests;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.acquisition.AcquisitionManager;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.Coords;
import org.micromanager.data.Datastore;
import org.micromanager.display.DisplayWindow;
import org.mm2python.UI.reporter;

import javax.swing.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatastoreEventsThreadTests {

    private Studio mm;
    private Datastore ds;
    private Coords c;

    private void setUp() {
        mm = mock(Studio.class);

        // mocking methods needed by reporter
        JTextArea jta = mock(JTextArea.class);
        LogManager lm = mock(LogManager.class);
        when(mm.logs()).thenReturn(lm);
        doNothing().when(lm).logMessage(anyString());
        new reporter(jta, mm);

        // mocking methods needed by testrunnable
        Datastore ds = mock(Datastore.class);
        SequenceSettings ss = mock(SequenceSettings.class);
        AcquisitionManager aq = mock(AcquisitionManager.class);
        when(mm.acquisitions()).thenReturn(aq);
        when(aq.getAcquisitionSettings()).thenReturn(ss);

        // mock memmap reference queue

        // mock
    }

    @Test
    void testConstructor() {
        setUp();



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
