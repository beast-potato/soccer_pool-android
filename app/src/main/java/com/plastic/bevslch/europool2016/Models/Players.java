package com.plastic.bevslch.europool2016.Models;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class Players {
    String name;
    String points;
    Integer position;
    int color;

    public Players(String name, String pts, Integer position, int color)
    {
        this.name = name;
        this.points = pts;
        this.position = position;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public String getPoints() {
        return points;
    }

    public Integer getPosition() {
        return position;
    }

    public int getColor() {
        return color;
    }
}
