package Tests.EventHandlerTests;

import org.micromanager.events.EventManager;
import org.micromanager.events.NewDisplayEvent;
import org.mm2python.mmEventHandler.globalEvents;
import org.junit.jupiter.api.Test;
import org.micromanager.Studio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class GlobalEventsTests {

    private Studio mm;

    private void setUp() {
        mm = mock(Studio.class);
        EventManager em = mock(EventManager.class);

        when(mm.events()).thenReturn(em);
        doNothing().when(em).registerForEvents(globalEvents.class);
        doNothing().when(em).unregisterForEvents(globalEvents.class);
    }

    @Test
    void testConstruction() {
        setUp();

        globalEvents ge = new globalEvents(mm);

        assertEquals(globalEvents.class, ge.getClass());
    }

    @Test
    void testRegistration() {
        setUp();

        globalEvents ge = new globalEvents(mm);
        globalEvents ge1 = new globalEvents(mm);
        globalEvents ge2 = new globalEvents(mm);
        globalEvents ge3 = new globalEvents(mm);

        assertEquals(globalEvents.class, ge.getClass());
        assertEquals(globalEvents.class, ge1.getClass());
        assertEquals(globalEvents.class, ge2.getClass());
        assertEquals(globalEvents.class, ge3.getClass());
    }

    @Test
    void testUnregister() {
        setUp();

        globalEvents ge = new globalEvents(mm);
        globalEvents ge1 = new globalEvents(mm);
        globalEvents ge2 = new globalEvents(mm);
        globalEvents ge3 = new globalEvents(mm);

        assertEquals(globalEvents.class, ge.getClass());
        assertEquals(globalEvents.class, ge1.getClass());
        assertEquals(globalEvents.class, ge2.getClass());
        assertEquals(globalEvents.class, ge3.getClass());

        ge.unRegisterGlobalEvents();
        ge1.unRegisterGlobalEvents();
        ge2.unRegisterGlobalEvents();
        ge3.unRegisterGlobalEvents();
    }

    @Test
    void testDisplayAboutToShow() {

        NewDisplayEvent evt = mock(NewDisplayEvent.class);

        setUp();

        globalEvents ge = new globalEvents(mm);

        ge.monitor_aboutToShow(evt);
    }
}
