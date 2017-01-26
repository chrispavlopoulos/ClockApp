package com.example.d3ths.clockapp;

import android.app.Activity;
import android.graphics.Typeface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class HomeContent extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_home_content, container, false);

        //Find views
        final TextView alarms = (TextView)view.findViewById(R.id.alarms);
        TextView timeSet = (TextView)view.findViewById(R.id.timeSet);
        TextView memos = (TextView)view.findViewById(R.id.memos);

        View alarmsRef = (View)view.findViewById(R.id.alarmsRef);
        View timeRef = (View)view.findViewById(R.id.timeRef);
        View memosRef = (View)view.findViewById(R.id.memosRef);
        View orangeBackground = (View)view.findViewById(R.id.returnToTop);
        final ScrollView scrollView = (ScrollView)view.findViewById(R.id.scrollView1);
        RelativeLayout mainMenu = (RelativeLayout)view.findViewById(R.id.mainMenu);

        //Set fonts
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "SourceSansProR.otf");
        alarms.setTypeface(typeface);
        timeSet.setTypeface(typeface);
        memos.setTypeface(typeface);


        ViewCompat.setTranslationZ(orangeBackground, 0.4f);

        final Time timePage = new Time();

        final Home home = (Home)getActivity();
        final Alarms alarmsPage = home.alarmsPage;

        //Click listeners
        alarmsRef.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                        ft.replace(R.id.container, alarmsPage, "alarmFragment");
                        ft.addToBackStack(null);
                        ft.commit();
                        callBack.goToView("alarmsPage");
                        callBack.passFragment(alarmsPage);
                    }
                }
        );

        timeRef.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(home.cannotChange)return;
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

                        ft.replace(R.id.container, timePage, "timeFragment");
                        ft.addToBackStack(null);
                        ft.commit();
                        callBack.goToView("timePage");
                        callBack.passFragment(timePage);
                    }
                }
        );

        memosRef.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        FragmentTransaction ft = getFragmentManager().beginTransaction();

                        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

                        ft.replace(R.id.container, Memos.getInstance());
                        ft.addToBackStack(null);
                        ft.commit();
                        callBack.goToView("memosPage");
                    }
                }
        );

        orangeBackground.setOnClickListener (
                new View.OnClickListener() {
                    public void onClick(View v){
                        scrollView.smoothScrollTo(0, 0);

                    }

                }


        );


        return view;
    }

    private static CallBacks callBack;

    public interface CallBacks{
        public void goToView(String viewNum);
        public void passFragment(Fragment frag);
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


}
