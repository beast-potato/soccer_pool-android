package com.plastic.bevslch.europool2016.Models;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class Players {
    String name;
    int points;
    int position;

    public Players(String name, int pts, int position)
    {
        this.name = name;
        this.points = pts;
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public int getPoints() {
        return points;
    }

    public int getPosition() {
        return position;
    }
}
