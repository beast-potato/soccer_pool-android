package com.plastic.bevslch.europool2016.Models;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class Players {
    String name;
    int points;
    int position;
    String picUrl;

    public Players(String name, int pts, int position, String picUrl)
    {
        this.name = name;
        this.points = pts;
        this.position = position;
        this.picUrl = picUrl;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
