package com.example.d3ths.clockapp;

/**
 * Created by Chris on 1/1/2017.
 */
public class Alarm {
    public String name;
    public String ringtone;
    public int hour;
    public int minute;
    public int ampm;
    public String daysOfTheWeek;

    public Alarm(int hr, int min, int ampm, String daysOfTheWeek){
        hour = hr;
        minute = min;
        this.ampm = ampm;
        this.daysOfTheWeek = daysOfTheWeek;
        name = "My Alarm";
        ringtone = "";
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRingtone(String ringtone){
        this.ringtone = ringtone;
    }
}
