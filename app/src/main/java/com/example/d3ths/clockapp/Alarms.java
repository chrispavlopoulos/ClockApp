package com.example.d3ths.clockapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Chris on 8/29/2016.
 */
public class Alarms extends Fragment {
    FragmentTransaction ft;
    public String time;
    RelativeLayout alarmsLayout;
    int dp15;
    int dp142;
    int dp184;
    RelativeLayout view;

    Handler handler = new Handler();
    TextView returnFromAlarms;
    View toNewAlarm;

    private Button changeShit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();
        dp15 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics());
        dp142 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 142, r.getDisplayMetrics());
        dp184 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 184, r.getDisplayMetrics());

        view = (RelativeLayout)inflater.inflate(R.layout.alarms, container, false);


        returnFromAlarms = (TextView)view.findViewById(R.id.returnFromAlarms);
        returnFromAlarms.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

                        ft.replace(R.id.container, ((Home)getActivity()).homeContentPage);
                        ft.commit();
                        callBack.leaveFromAlarms();
                    }
                }
        );

        final AlarmsCurrent alarmsCurrent = new AlarmsCurrent();
        View toCurrentAlarms = (View)view.findViewById(R.id.toCurrentAlarms);
        toCurrentAlarms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ft = getFragmentManager().beginTransaction();

                ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                ft.replace(R.id.container, alarmsCurrent, "alarmsCurrentFrag");
                ft.commit();
                callBack.alarmGoToView(alarmsCurrent, "alarmsCurrentPage");
            }
        });

        final AlarmNew alarmNew = new AlarmNew();
        toNewAlarm = (View)view.findViewById(R.id.toAlarmNew);
        toNewAlarm.setOnClickListener(
                new TextView.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        ft.replace(R.id.container, alarmNew, "newAlarmFrag");
                        ft.commit();
                        callBack.alarmGoToView(alarmNew, "newAlarmPage");
                    }
                }
        );

        //alarmsLayout = (RelativeLayout)view.findViewById(R.id.alarmsLayout);

//        final AudioManager audio = (AudioManager)getActivity().getSystemService(Context.AUDIO_SERVICE);
//        view.findViewById(R.id.raiseVolume).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                audio.adjustStreamVolume(AudioManager.STREAM_ALARM,
//                        AudioManager.ADJUST_RAISE, AudioManager.FLAG_SHOW_UI);
//            }
//        });




        return view;

    }

    public void goBack(){
        ft = getFragmentManager().beginTransaction();

        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

        ft.replace(R.id.container, ((Home)getActivity()).homeContentPage);// Start the animated transition.
        ft.commit();
        callBack.leaveFromAlarms();
    }

    private Callbacks callBack;

    public interface Callbacks{
        public void leaveFromAlarms();
        public void alarmGoToView(Fragment frag, String view);
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


    public static Alarms getInstance() {
        Alarms f = new Alarms();

        return f;
    }
}
