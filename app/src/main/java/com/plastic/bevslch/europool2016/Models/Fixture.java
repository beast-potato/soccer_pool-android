package com.plastic.bevslch.europool2016.Models;

/**
 * Created by sjsaldanha on 2016-06-02.
 */

public class Fixture {
    String homeTeam;
    String awayTeam;
    public Fixture(String home, String away)
    {
        this.homeTeam = home;
        this.awayTeam = away;
    }

    public String getAwayTeam() {
        return awayTeam;
    }

    public String getHomeTeam() {
        return homeTeam;
    }
}
