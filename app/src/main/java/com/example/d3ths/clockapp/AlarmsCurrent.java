package com.example.d3ths.clockapp;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    int textSizeTime;
    int nameMarginL;
    int boxHeight;
    int borderW;
    int borderH;
    int marginTopShiftDotw;
    int letterSpacer;
    int letterSpacerChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.density = r.getDisplayMetrics().density;
        switch((int)(displayMetrics.density * 160)){
            //Nexus 5x
            case (420):
                textSizeTime = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 45, r.getDisplayMetrics());
                boxHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
                borderW = 170;
                borderH = 60;
                boxHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
                marginTopShiftDotw = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 53, r.getDisplayMetrics());
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
                letterSpacerChange = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
                nameMarginL = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
                break;
            //HTC One M9
            case (480):
                textSizeTime = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 40, r.getDisplayMetrics());
                boxHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
                borderW = 150;
                borderH = 50;
                marginTopShiftDotw = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, r.getDisplayMetrics());
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());
                letterSpacerChange = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());
                nameMarginL = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());
                break;

            default:
                textSizeTime = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 40, r.getDisplayMetrics());
                boxHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());
                borderW = 150;
                borderH = 50;
                marginTopShiftDotw = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 43, r.getDisplayMetrics());
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());
                letterSpacerChange = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());
                nameMarginL = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());
                break;
        }

        view = (RelativeLayout)inflater.inflate(R.layout.alarms_current, container, false);
        int marginTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 70, r.getDisplayMetrics());

        ArrayList<Alarm> alarms = ((Home)getActivity()).alarms;
        RelativeLayout layoutContainer = (RelativeLayout)view.findViewById(R.id.container);

        Typeface sourceSans = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansProR.otf");

        int evenOdd = 0;
        for(final Alarm alarm: alarms){
            View v = new View(getContext());
            LayoutParams lp1 = new LayoutParams(LayoutParams.MATCH_PARENT,boxHeight);
            lp1.setMargins(0, marginTop, 0, 0);
            v.setLayoutParams(lp1);
            if(evenOdd % 2 == 0){
                v.setBackgroundColor(r.getColor(R.color.greyish));
            }else v.setBackgroundColor(r.getColor(R.color.whiteTaint));
            layoutContainer.addView(v);

            View border = new View(getContext());
            LayoutParams lp2 = new LayoutParams((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderW, r.getDisplayMetrics()), (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, borderH, r.getDisplayMetrics()));
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
            displayedTime.setTextSize(textSizeTime);
            displayedTime.setTypeface(sourceSans);
            layoutContainer.addView(displayedTime);

            String[] dotwLetters = {"M","T","W","TH","F", "S", "SU"};
            for(int i = 0; i < dotwLetters.length; i++){
                TextView l = new TextView(getActivity());
                LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                lp.setMargins(0, marginTop + marginTopShiftDotw, letterSpacer, 0);
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
                    letterSpacer -= (letterSpacerChange + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics()));
                }else if(i == 1){
                    letterSpacer -= (letterSpacerChange + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()));
                }else if(i == 4 || i == 3){
                    letterSpacer -= (letterSpacerChange + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, r.getDisplayMetrics()));
                }else
                    letterSpacer -= letterSpacerChange;
            }

            String nameText = "";
            if(alarm.name.length() < 12){
                nameText += alarm.name;
            }else nameText += alarm.name.substring(0, 11) + "-";
            TextView name = new TextView(getContext());
            LayoutParams lp4 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp4.setMargins(nameMarginL, marginTop + (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics()), 0, 0);
            name.setMaxLines(1);
            name.setLayoutParams(lp4);
            name.setText(nameText);
            name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 31);
            name.setTypeface(sourceSans);
            if(alarm.active)name.setAlpha(1f);
            else name.setAlpha(0.4f);
            //if(alarm.active)name.setTextColor(r.getColor(R.color.grey));
            //else if(evenOdd % 2 == 0)name.setTextColor(r.getColor(R.color.whiteTaint));
            //else name.setTextColor(r.getColor(R.color.greyish));
            layoutContainer.addView(name);

            final Fragment thisObj = this;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final DialogAlarmOverview alert = new DialogAlarmOverview(getActivity(), alarm);
                    alert.show();
                    alert.onOff.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alarm.setActive(alert.onOff.isChecked());
                        }
                    });

                    alert.edit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                            ft = getFragmentManager().beginTransaction();
                            ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

                            AlarmEdit alarmEdit = new AlarmEdit();
                            alarmEdit.setAlarm(alarm);
                            alarmEdit.setLastFrag(thisObj, "alarmsCurrentPage", "alarmsCurrentFrag");

                            ft.replace(R.id.container, alarmEdit, "alarmEditFrag");
                            ft.commit();
                            callBack.alarmsCurrentGoToView(alarmEdit, "alarmEditPage");
                        }
                    });

                    alert.cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            alert.dismiss();
                        }
                    });
                }
            });

            resetLetterSpacer();
            marginTop += boxHeight;
            evenOdd++;
        }


        return view;
    }

    public void resetLetterSpacer(){
        Resources r = getResources();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.density = r.getDisplayMetrics().density;
        switch((int)(displayMetrics.density * 160)){
            case (420):
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());

                break;
            case (480):
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());
                break;

            default:
                letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 170, r.getDisplayMetrics());
                break;
        }
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
        public void alarmsCurrentGoToView(Fragment frag, String view);
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
