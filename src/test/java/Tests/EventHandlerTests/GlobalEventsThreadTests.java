package Tests.EventHandlerTests;

import org.junit.jupiter.api.Test;
import org.micromanager.LogManager;
import org.micromanager.Studio;
import org.micromanager.acquisition.AcquisitionManager;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.Datastore;
import org.micromanager.display.DisplayWindow;
import org.mm2python.UI.reporter;
import org.mm2python.mmEventHandler.globalEventsThread;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class GlobalEventsThreadTests {

    private Studio mm;
    private DisplayWindow dw;

    private void setUp() {
        mm = mock(Studio.class);
        dw = mock(DisplayWindow.class);

        // mocking methods needed by reporter
        JTextArea jta = mock(JTextArea.class);
        LogManager lm = mock(LogManager.class);
        when(mm.logs()).thenReturn(lm);
        doNothing().when(lm).logMessage(anyString());

        when(dw.getName()).thenReturn("TEST: window name");
        new reporter(jta, mm);

        // mocking methods needed by testrunnable
        Datastore ds = mock(Datastore.class);
        when(dw.getDatastore()).thenReturn(ds);
        SequenceSettings ss = mock(SequenceSettings.class);
        AcquisitionManager aq = mock(AcquisitionManager.class);
        when(mm.acquisitions()).thenReturn(aq);
        when(aq.getAcquisitionSettings()).thenReturn(ss);
    }

    @Test
    void testConstructor() {
        setUp();
        globalEventsThread geth = new globalEventsThread(mm, dw);

        assertEquals(globalEventsThread.class, geth.getClass());
    }

    @Test
    void testrunnable() {
        setUp();
        globalEventsThread geth = new globalEventsThread(mm, dw);

        geth.run();

    }


}
