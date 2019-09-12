package Tests.DataStructuresTests;

import org.mm2python.mmEventHandler.datastoreEvents;
import org.mm2python.DataStructures.Maps.RegisteredDatastores;

import org.micromanager.Studio;
import org.micromanager.data.Datastore;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentHashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class RegisteredDatastoresTests {

    private Studio mm;

    private Datastore ds;

    private void setUp() {
        new RegisteredDatastores();
        mm = mock(Studio.class);
        ds = mock(Datastore.class);
    }

    private void breakDown() {
        RegisteredDatastores.reset();
    }

    @Test
    void testPut() {
        setUp();

        datastoreEvents de = new datastoreEvents(mm, ds, "blank window");

        RegisteredDatastores.put(ds, de);
        assertEquals(1, RegisteredDatastores.getSize());

        breakDown();
    }

    @Test
    void testGetMap() {
        setUp();

        datastoreEvents de = new datastoreEvents(mm, ds, "blank window");
        RegisteredDatastores.put(ds, de);

        assertEquals(RegisteredDatastores.getMap().getClass(), ConcurrentHashMap.class);

        breakDown();
    }

    @Test
    void testGet() {
        setUp();

        Studio mm = mock(Studio.class);
        Datastore ds1 = mock(Datastore.class);
        Datastore ds2 = mock(Datastore.class);
        Datastore ds3 = mock(Datastore.class);

        datastoreEvents de1 = new datastoreEvents(mm, ds1, "blank window");
        datastoreEvents de2 = new datastoreEvents(mm, ds2, "blank window");
        datastoreEvents de3 = new datastoreEvents(mm, ds3, "blank window");

        RegisteredDatastores.put(ds1, de1);
        RegisteredDatastores.put(ds2, de2);
        RegisteredDatastores.put(ds3, de3);

        assertEquals(de1, RegisteredDatastores.get(ds1));
        assertEquals(de2, RegisteredDatastores.get(ds2));
        assertEquals(de3, RegisteredDatastores.get(ds3));

        breakDown();
    }

    @Test
    void testRemove() {
        setUp();

        Studio mm = mock(Studio.class);
        Datastore ds1 = mock(Datastore.class);
        Datastore ds2 = mock(Datastore.class);

        datastoreEvents de1 = new datastoreEvents(mm, ds1, "blank window");
        datastoreEvents de2 = new datastoreEvents(mm, ds2, "blank window");

        RegisteredDatastores.put(ds1, de1);
        RegisteredDatastores.put(ds2, de2);

        assertEquals(2, RegisteredDatastores.getSize());

        RegisteredDatastores.remove(ds1);

        assertEquals(1, RegisteredDatastores.getSize());

        breakDown();
    }

    @Test
    void testReset() {
        setUp();

        datastoreEvents de = new datastoreEvents(mm, ds, "blank window");

        RegisteredDatastores.put(ds, de);
        assertEquals(1, RegisteredDatastores.getSize());
        RegisteredDatastores.reset();
        assertEquals(0, RegisteredDatastores.getSize());

        breakDown();
    }

    @Test
    void testGetSize() {
        setUp();

        Datastore ds1 = mock(Datastore.class);
        Datastore ds2 = mock(Datastore.class);
        datastoreEvents de = new datastoreEvents(mm, ds, "blank window");

        RegisteredDatastores.put(ds, de);
        RegisteredDatastores.put(ds1, de);
        RegisteredDatastores.put(ds2, de);

        assertEquals(3, RegisteredDatastores.getSize());

        breakDown();

    }
}
