package mm2python.DataStructures.Maps;

import mm2python.mmEventHandler.datastoreEvents;
import org.micromanager.data.Datastore;

import java.util.concurrent.ConcurrentHashMap;

public class RegisteredDatastores {

    public static final ConcurrentHashMap<Datastore, datastoreEvents> RegisteredStores
                = new ConcurrentHashMap<>(100, 0.75f, 30);

    public static void put(Datastore ds, datastoreEvents de) {
        RegisteredStores.put(ds, de);
    }

    public static datastoreEvents get(Datastore ds) {
        return RegisteredStores.get(ds);
    }

    public static int getSize() {
        return RegisteredStores.size();
    }
}
