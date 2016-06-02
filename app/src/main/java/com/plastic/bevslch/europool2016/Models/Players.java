package com.plastic.bevslch.europool2016.Models;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class Players {
    String name;
    String points;

    public Players(String name, String pts)
    {
        this.name = name;
        this.points = pts;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return points;
    }
}
