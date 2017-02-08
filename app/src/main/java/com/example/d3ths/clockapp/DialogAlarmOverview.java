package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by Chris on 1/11/2017.
 */
public class DialogAlarmOverview extends Dialog{

    public Activity c;
    public Dialog d;
    public ImageView edit, cancel;
    Resources r;
    Alarm alarm;
    TextView alarmName;
    int textSize;
    RelativeLayout pointedLayout;
    int letterSpacer;

    public DialogAlarmOverview(Activity a, Alarm alarm) {
        super(a);
        this.c = a;
        this.alarm = alarm;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_alarm_overview);
        r = c.getResources();

        //Pixel to dp values for different displays
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.density = r.getDisplayMetrics().density;
        switch((int)(displayMetrics.density * 160)){
            //Nexus 5x
            case (420):
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, r.getDisplayMetrics());
                break;
            //HTC One M9
            case (480):
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, r.getDisplayMetrics());
                break;

            default:
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, r.getDisplayMetrics());
                break;
        }

        pointedLayout = (RelativeLayout)findViewById(R.id.dialog_alarm_overview);
        Typeface sourceSans = Typeface.createFromAsset(c.getAssets(), "SourceSansProR.otf");
        alarmName = (TextView)findViewById(R.id.alarmName);
        textSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 35, r.getDisplayMetrics());
        alarmName.setTextSize(textSize);
        alarmName.setText(alarm.name);
        alarmName.setTypeface(sourceSans);
        if(alarm.active)alarmName.setAlpha(1f);
        else alarmName.setAlpha(0.4f);
        edit = (ImageView) findViewById(R.id.alertEditButton);
        cancel = (ImageView) findViewById(R.id.alertCancelButton);


        String[] dotwLetters = {"M","T","W","TH","F", "S", "SU"};
        for(int i = 0; i < dotwLetters.length; i++){
            TextView l = new TextView(c);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.setMargins(0, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 53, r.getDisplayMetrics()), letterSpacer, 0);
            l.setLayoutParams(lp);
            l.setTypeface(sourceSans);
            l.setText(dotwLetters[i]);
            l.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            char onOff = alarm.daysOfTheWeek.charAt(i);
            if(onOff == '0'){
                l.setTextColor(c.getResources().getColor(R.color.greyish));
            }
            pointedLayout.addView(l);
            if(i == 2 || i == 5){
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
            }else if(i == 1){
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
            }else if(i == 4 || i == 3){
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
            }else
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
        }

        alarmName.post(new Runnable() {
            @Override
            public void run() {
                checkWidth();
            }
        });

    }

    public void checkWidth(){
        if(textSize < (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, r.getDisplayMetrics()))return;
        alarmName.measure(0,0);
        if(alarmName.getMeasuredWidth() > pointedLayout.getMeasuredWidth() / 1.1){
            textSize -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 1, r.getDisplayMetrics());
            alarmName.setTextSize(textSize);
            checkWidth();
        }
    }

}
