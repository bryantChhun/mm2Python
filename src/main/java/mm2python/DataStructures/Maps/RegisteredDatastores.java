package mm2python.DataStructures.Maps;

import mm2python.mmEventHandler.datastoreEvents;
import org.micromanager.data.Datastore;

import java.util.concurrent.ConcurrentHashMap;

/**
 * to keep track of datastores that are registered to EventBus
 */
public class RegisteredDatastores {

    private static ConcurrentHashMap<Datastore, datastoreEvents> RegisteredStores;

    static {
        RegisteredStores = new ConcurrentHashMap<>(100, 0.75f, 30);
    }

    public static void put(Datastore ds, datastoreEvents de) {
        RegisteredStores.put(ds, de);
    }

    public static ConcurrentHashMap<Datastore, datastoreEvents> getMap() {
        return RegisteredStores;
    }

    public static datastoreEvents get(Datastore ds) {
        return RegisteredStores.get(ds);
    }

    public static void remove(Datastore ds) {
        RegisteredStores.remove(ds);
    }

    public static void reset() {
        RegisteredStores.clear();
        RegisteredStores = null;
        RegisteredStores = new ConcurrentHashMap<>(100, 0.75f, 30);
    }

    public static int getSize() {
        return RegisteredStores.size();
    }
}
