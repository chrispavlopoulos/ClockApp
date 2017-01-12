package com.example.d3ths.clockapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

/**
 * Created by Chris on 8/29/2016.
 */
public class Time extends Fragment{

    public TextView returnFromTime;
    private Home home;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_time, container, false);

        home = (Home)getActivity();
        returnFromTime = (TextView)view.findViewById(R.id.returnFromTime);
        returnFromTime.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override

                    public void onClick(View v){
                        if(home.cannotChange)return;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

                        ft.replace(R.id.container, new HomeContent());// Start the animated transition.
                        ft.commit();
                        callBack.leaveFromTime();
                    }
                }
        );

        return view;

    }

    public void goBack(){
        if(home.cannotChange)return;
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        ft.replace(R.id.container, new HomeContent());// Start the animated transition.
        ft.commit();
        callBack.leaveFromTime();
    }

    private static CallBacks callBack;

    public interface CallBacks{
        public void leaveFromTime();
    }

    public static void setCallBack(CallBacks callBack){
        callBack = callBack;
    }

    @Override
    public void onDetach() {
        callBack = null;
        super.onDetach();
    }

    @Override
    public void onAttach(Activity activity){
        callBack = (CallBacks) activity;
        super.onAttach(activity);
    }

    public static Time getInstance() {
        Time f = new Time();

        return f;
    }
}
