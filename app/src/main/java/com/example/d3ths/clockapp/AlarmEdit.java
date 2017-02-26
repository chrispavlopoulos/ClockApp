package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;


/**
 * Created by Chris on 1/3/2017.
 */
public class AlarmEdit extends Fragment{
    FragmentTransaction ft;
    RelativeLayout view;
    Alarm alarm;
    Fragment lastFragment;
    String lastView;
    String tag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();
        final TextView[] dotwViews = new TextView[7];
        int marginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        view = (RelativeLayout)inflater.inflate(R.layout.alarm_edit, container, false);

        final TextView alarmName = (TextView) view.findViewById(R.id.alarmName);
        String name = alarm.name;
        if(name.length() <= 14){
            alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        }else if(name.length() > 14 && name.length() < 21){
            alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }else if(name.length() >= 21){
            alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            name = name.substring(0, 20) + "...";
        }
        alarmName.setText(name);



        (view.findViewById(R.id.alarmNameSelector)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                final DialogEditText alert = new DialogEditText(getActivity(), "Alarm Name", alarm.name);
                alert.show();
                alert.setCanceledOnTouchOutside(false);

                alert.ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!alert.input.getText().toString().equals("")){
                            String input = alert.input.getText().toString();
                            if(input.length() <= 14){
                                alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
                            }else if(input.length() > 14 && input.length() < 21){
                                alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
                            }else if(input.length() >= 21){
                                alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
                                input = input.substring(0, 21) + "...";
                            }
                            alarmName.setText(input);
                            alarm.setName(alert.input.getText().toString());
                        }
                        alert.dismiss();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                });

                alert.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                });

                alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                        if(keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                            Log.i("oh", "called");
                            if(!alert.input.getText().toString().equals("")){
                                alarmName.setText(alert.input.getText());
                                alarm.setName(alert.input.getText().toString());
                            }
                            alert.dismiss();
                            Log.i("oh", "called");
                            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                            return true;
                        }
                        return false;
                    }
                });

            }
        });

        view.findViewById(R.id.dotwTextSelector).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] arr = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
                final DialogChecklist checklist = new DialogChecklist(getActivity(), "Days", arr, alarm.daysOfTheWeek);
                checklist.show();
                checklist.setCanceledOnTouchOutside(false);

                checklist.ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String dotw = "";
                        CheckBox[] checkBoxes = checklist.checkBoxes;
                        for(int i = 0; i < checkBoxes.length; i++){
                            if(checkBoxes[i].isChecked()){
                                dotw += "1";
                                dotwViews[i].setTextColor(getResources().getColor(R.color.grey));
                            }else{
                                dotw += "0";
                                dotwViews[i].setTextColor(getResources().getColor(R.color.greyish));
                            }
                        }
                        alarm.daysOfTheWeek = dotw;
                        checklist.dismiss();
                    }
                });
            }
        });
        Typeface sourceSans = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansProR.otf");
        RelativeLayout layoutContainer = (RelativeLayout)view.findViewById(R.id.container);

        ((TextView)view.findViewById(R.id.timeText)).setTypeface(sourceSans);

        String time = "";
        time += alarm.hour + ":";
        if(alarm.minute < 10)time += "0";
        time += alarm.minute;
        if(alarm.ampm == 0)time += "AM";
        else time += "PM";
        final TextView displayedTime = new TextView(getActivity());
        LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0, 0, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics()), 0);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        displayedTime.setLayoutParams(lp2);
        displayedTime.setText(time);
        displayedTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        displayedTime.setTypeface(sourceSans);
        layoutContainer.addView(displayedTime);

        (view.findViewById(R.id.alarmTimeSelector)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                final DialogEditText alert = new DialogEditText(getActivity(), "Time", displayedTime.getText().toString(), 150);
                alert.show();
                alert.setCanceledOnTouchOutside(false);

                alert.ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!alert.input.getText().toString().equals("")){
                            String input = alert.input.getText().toString();
                            if(input.length() < 3){
                                alert.dismiss();
                                return;
                            }
                            int colonPos = -1;
                            int ampmPos = -1;
                            int hour = 0;
                            int minute = 0;
                            int ampm = 0;
                            if(input.length() < 5){
                                for(int i = 0; i < input.length(); i++){
                                    char current = input.charAt(i);
                                    if((current == 'm' || current == 'M') && i > 1)ampmPos = i - 1;
                                }
                                if(ampmPos == -1){
                                    dismiss(alert);
                                    return;
                                }
                                try {
                                    hour = Integer.parseInt(input.substring(0, ampmPos));
                                } catch (NumberFormatException e) {
                                    Log.i("Invalid Input", "Not a number");
                                }
                                minute = 0;
                            }else {
                                for (int i = 0; i < input.length(); i++) {
                                    char current = input.charAt(i);
                                    if (current == ':') colonPos = i;
                                    if ((current == 'm' || current == 'M') && i > colonPos + 2)
                                        ampmPos = i - 1;
                                }
                                if (colonPos == -1) {
                                    dismiss(alert);
                                    return;
                                }
                                try {
                                    hour = Integer.parseInt(input.substring(0, colonPos));
                                    if (ampmPos != -1)
                                        minute = Integer.parseInt(input.substring(colonPos + 1, ampmPos));
                                    else
                                        minute = Integer.parseInt(input.substring(colonPos + 1, input.length()));
                                } catch (NumberFormatException e) {
                                    Log.i("Invalid Input", "Not a number");
                                }
                            }
                            if(hour > 12 || minute > 59){
                                dismiss(alert);
                                return;
                            }
                            if(ampmPos == -1) ampm = 0;
                            else{
                                if(input.substring(ampmPos, input.length()).toLowerCase().equals("am"))ampm = 0;
                                else ampm = 1;
                            }
                            alarm.hour = hour;
                            alarm.minute = minute;
                            alarm.ampm = ampm;
                            String time = "";
                            time += alarm.hour + ":";
                            if(alarm.minute < 10)time += "0";
                            time += alarm.minute;
                            if(alarm.ampm == 0)time += "AM";
                            else time += "PM";
                            displayedTime.setText(time);

                        }
                        dismiss(alert);
                    }
                });

                alert.cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alert.dismiss();
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    }
                });

            }
        });


        ((TextView)view.findViewById(R.id.ringtoneText)).setTypeface(sourceSans);

        TextView setRingtone = (TextView)view.findViewById(R.id.setRingtone);
        setRingtone.setTypeface(sourceSans);
        setRingtone.setText(alarm.ringtone.substring(0, alarm.ringtone.length() - 4));

        int letterSpacer = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180, r.getDisplayMetrics());
        String[] dotwLetters = {"M","T","W","TH","F", "S", "SU"};
        int counter = 0;
        for(int i = 0; i < dotwLetters.length; i++){
            TextView l = new TextView(getActivity());
            LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            lp.setMargins(0, (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 145, r.getDisplayMetrics()), letterSpacer, 0);
            l.setLayoutParams(lp);
            l.setTypeface(sourceSans);
            l.setText(dotwLetters[i]);
            l.setTextSize(TypedValue.COMPLEX_UNIT_SP, 26);
            char onOff = alarm.daysOfTheWeek.charAt(i);
            if(onOff == '0') l.setTextColor(getResources().getColor(R.color.greyish));
            layoutContainer.addView(l);
            if(i == 2 || i == 5){
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
            }else if(i == 1){
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
            }else if(i == 4 || i == 3){
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
            }else
                letterSpacer -= (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());

            dotwViews[counter] = l;
            counter++;
        }

        ((ImageView)view.findViewById(R.id.nextArrow)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                if(tag.equals("newAlarmFrag")) {
                    ft.replace(R.id.container, ((Home)getActivity()).homeContentPage);
                }else{
                    ft.replace(R.id.container, ((Home)getActivity()).alarmsCurrent);
                }
                callBack.alarmEditFinish(lastView);
                alarm.init();
                ((Home)getActivity()).buildBackup();
                ft.commit();

            }
        });
        final ImageView deleteButton = (ImageView)view.findViewById(R.id.deleteButton);
        deleteButton.setAlpha(0f);
        if(tag.equals("newAlarmFrag"))((RelativeLayout)view.findViewById(R.id.scrollContainer)).removeView(deleteButton);
        else {
            fadeIn(deleteButton);
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alarm.deactivateAll();
                    ((Home) getActivity()).alarms.remove(alarm);
                    goBack();
                }
            });
        }

        return view;

    }

    int counter;
    Handler handler = new Handler();
    public void fadeIn(final View v){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(counter > 4) v.setAlpha(v.getAlpha() + 0.2f);
                counter++;

                if(v.getAlpha() < 1f)fadeIn(v);
                else counter = 0;
            }
        }, 50);
    }

    public void goBack(){
        ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        if(tag.equals("newAlarmFrag")) {
            ft.replace(R.id.container, lastFragment, "newAlarmFrag");
            callBack.newAlarmReload(alarm);
        }else{
            ft.replace(R.id.container, lastFragment, "alarmsCurrentFrag");
        }
        ft.commit();
        callBack.returnFromEditAlarm(lastFragment, lastView);
    }

    public void setAlarm(Alarm alarm){
        this.alarm = alarm;
    }

    public void setLastFrag(Fragment frag, String view, String tag){
        this.lastFragment = frag;
        this.lastView = view;
        this.tag = tag;
    }

    private Callbacks callBack;

    public interface Callbacks{
        public void returnFromEditAlarm(Fragment frag, String view);
        public void newAlarmReload(Alarm alarm);
        public void alarmEditFinish(String view);
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

    public void dismiss(Dialog alert){
        alert.dismiss();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

}
