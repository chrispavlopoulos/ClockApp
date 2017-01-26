package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Chris on 1/24/2017.
 */
public class AlarmsCurrent extends Fragment{
    FragmentTransaction ft;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();
        view = (RelativeLayout)inflater.inflate(R.layout.alarms_current, container, false);

        int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());

        ArrayList<Alarm> alarms = ((Home)getActivity()).alarms;
        RelativeLayout layoutContainer = (RelativeLayout)view.findViewById(R.id.container);

        Typeface sourceSans = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansProR.otf");

        int evenOdd = 0;
        for(final Alarm alarm: alarms){
            View v = new View(getContext());
            LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,(int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics()));
            lp1.setMargins(0, marginTop, 0, 0);
            v.setLayoutParams(lp1);
            if(evenOdd % 2 == 0){
                v.setBackgroundColor(r.getColor(R.color.greyish));
            }else v.setBackgroundColor(r.getColor(R.color.whiteTaint));
            layoutContainer.addView(v);

            View border = new View(getContext());
            LayoutParams lp2 = new LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics()), (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, r.getDisplayMetrics()));
            lp2.setMargins((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()), marginTop + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()), 0, 0);
            border.setLayoutParams(lp2);
            if(evenOdd % 2 == 0) {
                border.setBackgroundColor(r.getColor(R.color.whiteTaint));
            }else border.setBackgroundColor(r.getColor(R.color.white));
            layoutContainer.addView(border);

            String time = "";
            time += alarm.hour + ":";
            int tempML = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());
            if(alarm.hour < 10)tempML += (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
            if(alarm.minute < 10)time += "0";
            time += alarm.minute;
            if(alarm.ampm == 0)time += "AM";
            else time += "PM";
            TextView displayedTime = new TextView(getActivity());
            LayoutParams lp3 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp3.setMargins(tempML, marginTop + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()), 0, 0);
            displayedTime.setLayoutParams(lp3);
            displayedTime.setText(time);
            displayedTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 45);
            displayedTime.setTypeface(sourceSans);
            layoutContainer.addView(displayedTime);

            int letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
            String[] dotwLetters = {"M","T","W","TH","F", "S", "SU"};
            for(int i = 0; i < dotwLetters.length; i++){
                TextView l = new TextView(getActivity());
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.setMargins(0, marginTop + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 53, r.getDisplayMetrics()), letterSpacer, 0);
                l.setLayoutParams(lp);
                l.setTypeface(sourceSans);
                l.setText(dotwLetters[i]);
                l.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                char onOff = alarm.daysOfTheWeek.charAt(i);
                if(onOff == '0'){
                    if(evenOdd % 2 == 0){
                        l.setTextColor(getResources().getColor(R.color.whiteTaint));
                    }else l.setTextColor(getResources().getColor(R.color.greyish));
                }
                layoutContainer.addView(l);
                if(i == 2 || i == 5){
                    letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
                }else if(i == 1){
                    letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
                }else
                    letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
            }

            String nameText = "";
            if(alarm.name.length() < 15){
                nameText += alarm.name;
            }else nameText += alarm.name.substring(0, 14) + "...";
            TextView name = new TextView(getContext());
            LayoutParams lp4 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp4.setMargins((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics()), marginTop + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()), 0, 0);
            name.setMaxLines(1);
            name.setLayoutParams(lp4);
            name.setText(nameText);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 31);
            name.setTypeface(sourceSans);
            layoutContainer.addView(name);


            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogEditText alert = new DialogEditText(getActivity(), "Alarm Name", alarm.name);
                    alert.show();
                    alert.setCanceledOnTouchOutside(false);

                    alert.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });
                }
            });


            marginTop += (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
            evenOdd++;
        }


        return view;
    }

    public void goBack(){
        ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        ft.replace(R.id.container, ((Home)getActivity()).alarmsPage, "alarmFragment");// Start the animated transition.
        ft.commit();
        callBack.leaveFromAlarmsCurrent();
    }

    private Callbacks callBack;

    public interface Callbacks{
        public void leaveFromAlarmsCurrent();
        //public void alarmGoToView(Fragment frag, String view);
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


    public static AlarmsCurrent getInstance() {
        AlarmsCurrent f = new AlarmsCurrent();

        return f;
    }
}
