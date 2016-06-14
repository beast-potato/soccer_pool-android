package com.plastic.bevslch.europool2016.bus;

import android.util.Pair;

import java.util.ArrayList;

/**
 * Created by Oleksiy on 6/13/2016.
 */
public class EventBus {
    private static EventBus ourInstance = new EventBus();

    private ArrayList<Pair<Class, BusEventListener>> listeners = new ArrayList<>();

    private EventBus() {
    }

    public static EventBus getInstance() {
        return ourInstance;
    }

    public void addListener(Class cls, BusEventListener listener) {
        if (!listeners.contains(listener))
            listeners.add(new Pair<>(cls, listener));
    }

    public void sendData(Object data, Object sender) {
        ArrayList<Pair<Class, BusEventListener>> listenersToDelete = new ArrayList<>();
        for (Pair<Class, BusEventListener> item : listeners) {
            if (item.first.equals(data.getClass()) && item.second != null) {
                item.second.onBusEvent(data, sender);
            } else if (item.second == null) {
                listenersToDelete.add(item);
            }
        }
        listeners.removeAll(listenersToDelete);
    }

    public interface BusEventListener<T> {
        void onBusEvent(T eventData, Object sender);
    }
}
