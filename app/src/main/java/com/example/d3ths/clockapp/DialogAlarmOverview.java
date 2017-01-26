package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Chris on 1/11/2017.
 */
public class DialogAlarmOverview extends Dialog{

    public Activity c;
    public Dialog d;
    public ImageView ok, cancel;
    Alarm alarm;

    public DialogAlarmOverview(Activity a, Alarm alarm) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alarm_overview);
        Typeface sourceSans = Typeface.createFromAsset(c.getAssets(), "SourceSansProR.otf");
        TextView alarmName = (TextView)findViewById(R.id.alarmName);
        alarmName.setText(alarm.name);
        alarmName.setTypeface(sourceSans);
        ok = (ImageView) findViewById(R.id.alertOkButton);
        cancel = (ImageView) findViewById(R.id.alertCancelButton);
    }

}
