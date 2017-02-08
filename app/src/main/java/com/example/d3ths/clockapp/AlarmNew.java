package com.example.d3ths.clockapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chris on 8/29/2016.
 */
public class AlarmNew extends Fragment {
    FragmentTransaction ft;
    public int hour = 1;
    public int minute = 0;
    public int ampm = 0;
    public String time;
    RelativeLayout alarmsLayout;
    int dp15;
    int dp142;
    int dp184;
    RelativeLayout view;
    TextView circle = null;
    TextView circle2 = null;
    TextView hrText = null;
    Handler handler = new Handler();
    TextView aHT;

    boolean reloaded;
    Alarm tempAlarm;


    public AlarmNew() {
        hour = 1;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();
        dp15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());
        dp142 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 142, r.getDisplayMetrics());
        dp184 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 184, r.getDisplayMetrics());

        view = (RelativeLayout)inflater.inflate(R.layout.alarm_new, container, false);

        ft = getFragmentManager().beginTransaction();
        SelectionHour selectionHour = new SelectionHour();
        ft.add(R.id.alarmTimeSelection, selectionHour, "SelectionHour");
        SelectionMinute selectionMinute = new SelectionMinute();
        ft.add(R.id.alarmTimeSelection2, selectionMinute, "SelectionMinute");
        SelectionAMPM selectionAMPM = new SelectionAMPM();
        ft.add(R.id.alarmTimeSelection3, selectionAMPM, "SelectionAMPM");
        ft.commit();

        TextView gradientTop = (TextView)view.findViewById(R.id.gradientTop);
        TextView gradienBottom = (TextView)view.findViewById(R.id.gradientBottom);

        gradientTop.bringToFront();
        gradienBottom.bringToFront();

        ((TextView)view.findViewById(R.id.setTimeText)).setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "LiberationSerifR.ttf"));

        alarmsLayout = (RelativeLayout)view.findViewById(R.id.alarmsLayout);

        /*
        //Put a circle around the selected scrollview
        //This idea was scrapped due to it looking a lot better with the gray circles staying there
        circle = new TextView(getActivity());
        circle2 = new TextView(getActivity());

        LayoutParams circleLp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        circleLp.setMargins(dp15, dp184, 0, 0);
        LayoutParams circleLp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        circleLp2.setMargins(dp142, dp184, 0, 0);

        circle.setBackground(getResources().getDrawable(R.drawable.circle));
        circle2.setBackground(getResources().getDrawable(R.drawable.circle));

        circle.setLayoutParams(circleLp);
        circle2.setLayoutParams(circleLp2);

        circle.setId(R.id.hourCircle);
        circle2.setId(R.id.minuteCircle);

        //Log.i("temp", "" + circle);
        //Log.i("temp2", "" + circle2);
        */



        circle = (TextView)view.findViewById(R.id.circle);
        circle2 = (TextView)view.findViewById(R.id.circle2);

        View hr = (View)view.findViewById(R.id.hr);
        hr.bringToFront();

        aHT = (TextView)view.findViewById(R.id.aHT);
        aHT.setText("1:00AM");

        //I hate these arrows
        ImageButton hrArrowUp = (ImageButton) view.findViewById(R.id.hrArrowUp);
        ImageButton hrArrowDown = (ImageButton) view.findViewById(R.id.hrArrowDown);
        ImageButton minArrowUp = (ImageButton) view.findViewById(R.id.minArrowUp);
        ImageButton minArrowDown = (ImageButton) view.findViewById(R.id.minArrowDown);

        hrArrowUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        callBack.hrScrollUp();
                    case MotionEvent.ACTION_UP:
                        //callBack.hrCheckAndMove();

                }
                return false;
            }
        });
        hrArrowDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        callBack.hrScrollDown();
                    case MotionEvent.ACTION_UP:
                        //callBack.hrCheckAndMove();

                }
                return false;
            }
        });
        minArrowUp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        callBack.minScrollUp();
                    case MotionEvent.ACTION_UP:
                        //callBack.minCheckAndMove();

                }
                return false;
            }
        });
        minArrowDown.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event){
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        callBack.minScrollDown();
                    case MotionEvent.ACTION_UP:
                        //callBack.minCheckAndMove();

                }
                return false;
            }
        });

        final ArrayList<CheckBox> checkBoxes = new ArrayList<CheckBox>();
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkMon));
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkTues));
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkWed));
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkThur));
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkFri));
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkSat));
        checkBoxes.add((CheckBox)view.findViewById(R.id.checkSun));

        final Fragment thisObj = this;

        ((ImageView)view.findViewById(R.id.nextArrow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String daysOfTheWeek = "";
                for(CheckBox cb: checkBoxes){
                    if(cb.isChecked()){
                        daysOfTheWeek += "1";
                    }else{
                        daysOfTheWeek += "0";
                    }
                }
                Alarm alarm = new Alarm(hour, minute, ampm, daysOfTheWeek, getActivity());
                if(reloaded)alarm.setName(tempAlarm.name);
                ((Home)getActivity()).alarms.add(alarm);

                ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

                AlarmEdit alarmEdit = new AlarmEdit();
                alarmEdit.setAlarm(alarm);
                alarmEdit.setLastFrag(thisObj, "newAlarmPage", "newAlarmFrag");

                ft.replace(R.id.container, alarmEdit, "alarmEditFrag");
                ft.commit();
                callBack.newAlarmGoToView(alarmEdit, "alarmEditPage");

            }
        });

        if(reloaded) {
            view.post(new Runnable() {
                @Override
                public void run() {
                    ((Home)getActivity()).alarms.remove(((Home)getActivity()).alarms.size() - 1);
                    callBack.changeValues(tempAlarm);
                    String daysOfTheWeek = tempAlarm.daysOfTheWeek;
                    for(int i = 0; i < checkBoxes.size(); i++){
                        char onOff = daysOfTheWeek.charAt(i);
                        if(onOff == '1'){
                            checkBoxes.get(i).setChecked(true);
                        }
                    }
                    setHour(hour);
                }
            });
        }





        return view;

    }


    //Setting selected time
    public void setHour(int hour) {
        this.hour = hour;
        //Put a zero in front of the minute if < 10
        String tempMin = "" + minute;
        if(minute < 10) tempMin = "0" + minute;

        String tempAMPM = "PM";
        if(ampm == 0) tempAMPM = "AM";

        time = hour +":" +tempMin +tempAMPM;
        aHT.setText(time);
    }
    public void setMinute(int minute) {
        this.minute = minute;
        String tempMin = "" + minute;
        if(minute < 10) tempMin = "0" + minute;

        String tempAMPM = "PM";
        if(ampm == 0) tempAMPM = "AM";

        time = hour +":" +tempMin +tempAMPM;
        aHT.setText(time);
    }
    public void setAMPM(int ampm){
        this.ampm = ampm;
        String tempMin = "" + minute;
        if(minute < 10) tempMin = "0" + minute;

        String tempAMPM = "PM";
        if(ampm == 0) tempAMPM = "AM";

        time = hour +":" +tempMin +tempAMPM;
        aHT.setText(time);
    }

    public void goBack(){
        ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        ft.replace(R.id.container, ((Home)getActivity()).alarmsPage);
        ft.commit();
        callBack.returnFromNewAlarm();
    }

    public void setReloaded(Alarm alarm){
        reloaded = true;
        this.tempAlarm = alarm;
    }

    private Callbacks callBack;

    public interface Callbacks{
        public void hrScrollUp();
        public void hrScrollDown();
        public void minScrollUp();
        public void minScrollDown();
        public void hrCheckAndMove();
        public void minCheckAndMove();
        public void returnFromNewAlarm();
        public void newAlarmGoToView(Fragment frag, String view);
        public void changeValues(Alarm alarm);
    }

    @Override
    public void onDetach() {
        callBack = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity){
        callBack = (Callbacks) activity;
        super.onAttach(activity);
    }


    public static AlarmNew getInstance() {
        AlarmNew f = new AlarmNew();

        return f;
    }
}
