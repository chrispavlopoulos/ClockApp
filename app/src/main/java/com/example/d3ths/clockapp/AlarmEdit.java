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
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

        view = (RelativeLayout)inflater.inflate(R.layout.edit_alarm, container, false);

        final TextView alarmName = (TextView) view.findViewById(R.id.alarmName);
        alarmName.setText(alarm.name);
        alarmName.setTextSize(35);


        (view.findViewById(R.id.alarmNameSelector)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogEditText alert = new DialogEditText(getActivity(), "Alarm Name", 30);
                alert.show();
                alert.setCanceledOnTouchOutside(false);

                alert.ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(!alert.input.getText().toString().equals("")){
                            alarmName.setText(alert.input.getText());
                            alarm.setName(alert.input.getText().toString());
                        }
                        alert.dismiss();
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
        Typeface sourceSans = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansProR.otf");

        ((TextView)view.findViewById(R.id.timeText)).setTypeface(sourceSans);
        String time = "";
        time += alarm.hour + ":";
        if(alarm.minute < 10)time += "0";
        time += alarm.minute;
        if(alarm.ampm == 0)time += "AM";
        else time += "PM";
        TextView setTime = (TextView)view.findViewById(R.id.timeAlarm);
        setTime.setText(time);
        setTime.setTypeface(sourceSans);


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
