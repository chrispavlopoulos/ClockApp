package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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
        int marginStart = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());

        view = (RelativeLayout)inflater.inflate(R.layout.edit_alarm, container, false);

        final TextView alarmName = (TextView) view.findViewById(R.id.alarmName);
        String name = alarm.name;
        if(name.length() <= 14){
            alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 35);
        }else if(name.length() > 14 && name.length() < 21){
            alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
        }else if(name.length() >= 21){
            alarmName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            name = name.substring(0, 21) + "...";
        }
        alarmName.setText(name);

        (view.findViewById(R.id.alarmNameSelector)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((InputMethodManager)getActivity().getSystemService(getContext().INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                final DialogEditText alert = new DialogEditText(getActivity(), "Alarm Name", alarmName.getText().toString());
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
        Typeface sourceSans = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansProR.otf");
        RelativeLayout layoutContainer = (RelativeLayout)view.findViewById(R.id.container);

        final TextView timeText = new TextView(getActivity());
        LayoutParams lp1 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp1.setMargins(marginStart, 0, 0, 0);
        timeText.setLayoutParams(lp1);
        timeText.setText("Time:");
        timeText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        timeText.setTypeface(sourceSans);
        layoutContainer.addView(timeText);


        String time = "";
        time += alarm.hour + ":";
        if(alarm.minute < 10)time += "0";
        time += alarm.minute;
        if(alarm.ampm == 0)time += "AM";
        else time += "PM";
        TextView displayedTime = new TextView(getActivity());
        LayoutParams lp2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp2.setMargins(0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics()), 0);
        lp2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        displayedTime.setLayoutParams(lp2);
        displayedTime.setText(time);
        displayedTime.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        displayedTime.setTypeface(sourceSans);
        layoutContainer.addView(displayedTime);

        return view;

    }

    public void goBack(){
        ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        if(tag.equals("newAlarmFrag")) {
            ft.replace(R.id.container, (NewAlarm)lastFragment, "newAlarmFrag");
            callBack.newAlarmReload(alarm);
        }else{

        }
        ft.commit();
        callBack.returnFromEditAlarm((NewAlarm)lastFragment, lastView);
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

}
