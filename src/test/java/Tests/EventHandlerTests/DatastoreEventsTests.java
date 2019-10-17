package Tests.EventHandlerTests;

import org.junit.jupiter.api.Test;
import org.micromanager.Studio;
import org.micromanager.acquisition.AcquisitionManager;
import org.micromanager.acquisition.SequenceSettings;
import org.micromanager.data.DataProviderHasNewImageEvent;
import org.micromanager.data.Datastore;
import org.micromanager.data.DatastoreFrozenEvent;
import org.mm2python.DataStructures.Maps.RegisteredDatastores;
import org.mm2python.mmEventHandler.datastoreEvents;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DatastoreEventsTests {

    private Studio mm;
    private Datastore ds;

    private void setUp() {
        mm = mock(Studio.class);
        ds = mock(Datastore.class);
        SequenceSettings ss = mock(SequenceSettings.class);
        AcquisitionManager aq = mock(AcquisitionManager.class);
        when(mm.acquisitions()).thenReturn(aq);
        when(aq.getAcquisitionSettings()).thenReturn(ss);
    }

    private void breakDown() {
        RegisteredDatastores.reset();
    }

    @Test
    void testConstructor() {
        setUp();

        datastoreEvents de = new datastoreEvents(mm, ds, "TEST: window name");

        assertEquals(datastoreEvents.class, de.getClass());

        breakDown();
    }

    @Test
    void testRegisterDatastore() {
        setUp();

        datastoreEvents de = new datastoreEvents(mm, ds, "TEST: window name");
        de.registerThisDatastore();

        assertEquals(datastoreEvents.class, de.getClass());
        assertEquals(1, RegisteredDatastores.getSize());
        assertEquals(de, RegisteredDatastores.get(ds));

        breakDown();
    }

    @Test
    void testUnregisterDatastore() {
        setUp();

        datastoreEvents de = new datastoreEvents(mm, ds, "TEST: window name");
        de.registerThisDatastore();

        assertEquals(datastoreEvents.class, de.getClass());
        assertEquals(1, RegisteredDatastores.getSize());
        assertEquals(de, RegisteredDatastores.get(ds));

        de.unRegisterThisDatastore();

        assertEquals(datastoreEvents.class, de.getClass());
        assertEquals(0, RegisteredDatastores.getSize());
        assertNull(RegisteredDatastores.get(ds));

        breakDown();
    }

    @Test
    void testDatastoreFrozenEvent() {
        setUp();

        DatastoreFrozenEvent dfe = mock(DatastoreFrozenEvent.class);
        datastoreEvents de = new datastoreEvents(mm, ds, "TEST: window name");
        de.registerThisDatastore();

        de.datastoreIsFrozen(dfe);

        breakDown();
    }


    @Test
    void testNewImageEvent() {
        setUp();

        DataProviderHasNewImageEvent nie = mock(DataProviderHasNewImageEvent.class);
        datastoreEvents de = new datastoreEvents(mm, ds, "TEST: window name");
        de.registerThisDatastore();

        de.executeDatastoreThread(nie);

        breakDown();

    }
}
