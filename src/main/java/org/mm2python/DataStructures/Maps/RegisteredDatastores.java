package org.mm2python.DataStructures.Maps;

import org.micromanager.data.DataProvider;
import org.mm2python.mmEventHandler.datastoreEvents;

import java.util.concurrent.ConcurrentHashMap;

/**
 * to keep track of datastores that are registered to EventBus
 * These must be manually unregistered upon disconnection, or they will always receive events.
 */
public class RegisteredDatastores {

    private static ConcurrentHashMap<DataProvider, datastoreEvents> RegisteredStores;

    static {
        RegisteredStores = new ConcurrentHashMap<>(100, 0.75f, 30);
    }

    public static void put(DataProvider ds, datastoreEvents de) {
        RegisteredStores.put(ds, de);
    }

    public static ConcurrentHashMap<DataProvider, datastoreEvents> getMap() {
        return RegisteredStores;
    }

    public static datastoreEvents get(DataProvider ds) {
        return RegisteredStores.get(ds);
    }

    public static void remove(DataProvider ds) {
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
