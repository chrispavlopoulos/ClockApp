package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.Calendar;

/**
 * Created by Chris on 1/1/2017.
 */
public class Alarm {
    private Activity a;
    private AlarmManager alarmManager;
    private PendingIntent oneTimeAlarm;
    private PendingIntent[] setAlarms = new PendingIntent[7];
    public String name;
    public String ringtone;
    public int hour;
    public int minute;
    public int ampm;
    public String daysOfTheWeek;
    public boolean active;

    public Alarm(int hr, int min, int ampm, String daysOfTheWeek, Activity a){
        hour = hr;
        minute = min;
        this.ampm = ampm;
        this.daysOfTheWeek = daysOfTheWeek;
        name = "My Alarm";
        ringtone = "default.mp3";
        this.a = a;
    }

    public void init(){
        if(alarmManager != null) deactivateAll();
        setActive(true);
    }

    public void setActive(boolean tf){
        active = tf;
        if(!active){
            deactivateAll();
            return;
        }
        boolean allZeros = true;
        for(int i = 0; i < daysOfTheWeek.length(); i++){
            if(daysOfTheWeek.charAt(i) == '0'){
                if(setAlarms[i] != null){
                    alarmManager.cancel(setAlarms[i]);
                }
                continue;
            }
            if(oneTimeAlarm!=null)oneTimeAlarm.cancel();
            allZeros = false;
            alarmManager = (AlarmManager)a.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(a.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(a.getApplicationContext(), i, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            active = true;
            Calendar calendar = Calendar.getInstance();
            int tempHour = hour;
            if(ampm == 1)tempHour += 12;
            calendar.set(Calendar.HOUR_OF_DAY, tempHour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()) calendar.add(Calendar.DATE, 1);
            myIntent.putExtra("name", name);
            switch(i){
                case 0:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.MONDAY, pendingIntent);
                    break;
                case 1:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.TUESDAY, pendingIntent);
                    break;
                case 2:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.WEDNESDAY, pendingIntent);
                    break;
                case 3:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.THURSDAY, pendingIntent);
                    break;
                case 4:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.FRIDAY, pendingIntent);
                    break;
                case 5:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.SATURDAY, pendingIntent);
                    break;
                case 6:
                    alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), Calendar.SUNDAY, pendingIntent);
                    break;
            }
            setAlarms[i] = pendingIntent;
        }
        if(allZeros){
            alarmManager = (AlarmManager)a.getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(a.getApplicationContext(), AlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(a.getApplicationContext(), 0, myIntent, 0);
            Calendar calendar = Calendar.getInstance();
            int tempHour = hour;
            if(ampm == 1)tempHour += 12;
            calendar.set(Calendar.HOUR_OF_DAY, tempHour);
            calendar.set(Calendar.MINUTE, minute);
            calendar.set(Calendar.SECOND, 0);
            if(System.currentTimeMillis() > calendar.getTimeInMillis()) calendar.add(Calendar.DATE, 1);
            myIntent.putExtra("name", name);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            oneTimeAlarm = pendingIntent;
        }
    }

    public void deactivateAll(){
        for(PendingIntent p: setAlarms){
            if(p != null) alarmManager.cancel(p);
        }
        if(oneTimeAlarm != null) oneTimeAlarm.cancel();
    }

    public void setName(String name){
        this.name = name;
    }

    public void setRingtone(String ringtone){
        this.ringtone = ringtone;
    }
}
