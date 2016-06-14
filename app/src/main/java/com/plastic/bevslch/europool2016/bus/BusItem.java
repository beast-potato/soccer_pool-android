package com.plastic.bevslch.europool2016.bus;

import android.util.Pair;

/**
 * Created by Oleksiy on 6/13/2016.
 */

public class BusItem extends Pair<Class, EventBus.BusEventListener> {
    public BusItem(Class first, EventBus.BusEventListener second) {
        super(first, second);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof BusItem) {
            BusItem other = (BusItem) o;
            if (other.first.equals(first) && other.second != null && other.second.equals(second)) {
                return true;
            }
        }
        return false;
    }
}
