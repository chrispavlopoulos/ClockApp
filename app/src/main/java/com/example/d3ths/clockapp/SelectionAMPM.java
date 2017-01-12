package com.example.d3ths.clockapp;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;


import java.util.ArrayList;

/**
 * Created by Chris on 9/1/2016.
 */
public class SelectionAMPM extends Fragment {
    private GestureDetector mGesture;
    Handler handler = new Handler();
    Handler scrollHandlerUp = new Handler();
    Handler scrollHandlerDown = new Handler();
    RelativeLayout ampmNumList;
    RelativeLayout fakeScrollView;
    ScrollView scrollView;
    ArrayList<TextView> numbers;
    boolean cannotChange;
    int ampm = 0;
    int[] svValue;
    int linedUp;
    float textSize;
    int marginTop;
    int twtfiveDp;
    int numberSpacer;
    int offsetLeft;

    RelativeLayout tappy;
    boolean reloaded = false;
    boolean notChanged = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();
        int elements = 2;

        //Pixel to dp value
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.density = getResources().getDisplayMetrics().density;
        switch((int)(displayMetrics.density * 160)){
            //Nexus 5x
            case(420):
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, r.getDisplayMetrics());
                offsetLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());;
                break;
            //HTC One M9
            case(480):
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, r.getDisplayMetrics());
                offsetLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 105, r.getDisplayMetrics());
                numberSpacer = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95, r.getDisplayMetrics());
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
                break;

            default:
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
                offsetLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 95, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());;
                break;
        }



        View view = inflater.inflate(R.layout.selection_ampm, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.numScrollView3);
        final RelativeLayout ampmOverlay = (RelativeLayout) view.findViewById(R.id.ampmOverlay);
//        scrollView.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        ampm = Math.abs(ampm - 1);
//                        if(ampm == 1){
//                            scrollView.smoothScrollTo(0, scrollView.getChildAt(0).getHeight() - scrollView.getHeight());
//                        }else{
//                            scrollView.smoothScrollTo(0,0);
//                        }
//                        callBack.passAMPM(ampm);
//                        return true;
//                }
//
//                return false;
//            }
//        });

        final RelativeLayout scroll4Me = (RelativeLayout)view.findViewById(R.id.scroll4Me);
        tappy = scroll4Me;
        moveToPM();
        scroll4Me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cannotChange)return;
                ampm = Math.abs(ampm - 1);
                if(ampm == 0){
                    moveToPM();
                }else{
                    moveToAM();
                }
                callBack.passAMPM(ampm);
            }
        });

//        scroll4Me.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                switch(motionEvent.getAction()){
//                    case MotionEvent.ACTION_DOWN:
//                        if(cannotChange) return false;
//                        return true;
//                    case MotionEvent.ACTION_UP:
//                        ampm = Math.abs(ampm - 1);
//                        if(ampm == 0){
//                            moveToPM();
//                        }else{
//                            moveToAM();
//                        }
//                        callBack.passAMPM(ampm);
//                        break;
//                }
//                return false;
//            }
//        });
        fakeScrollView = scroll4Me;

        numbers = new ArrayList<TextView>();
        ampmNumList = (RelativeLayout) view.findViewById(R.id.ampmNumList);


        int tempMarginTop = 0;
        for (int i = 0; i < elements; i++) {
            TextView textView = new TextView(getActivity());
            LayoutParams numLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (i < 9) {
                numLayoutParams.setMargins(offsetLeft, tempMarginTop, 0, 0);
            } else {
                numLayoutParams.setMargins(0, tempMarginTop, 0, 0);
            }

            textView.setLayoutParams(numLayoutParams);
            if(i == 0){
                textView.setText("AM");
            }else{
                textView.setText("PM");
            }
            textView.setTextSize(textSize);
            numbers.add(textView);
            ampmNumList.addView(textView);
            //tempMarginTop += numberSpacer;
        }
        numbers.get(0).setY(numbers.get(0).getY() + marginTop);
        numbers.get(1).setY(numbers.get(1).getY() + marginTop + numberSpacer);


        svValue = new int[elements];

        for (int i = 0; i < numbers.size() - 1; i++) {
            TextView textView = numbers.get(i);
            int[] onScreen = new int[2];
            textView.getLocationOnScreen(onScreen);
            svValue[i] = onScreen[1];
        }

        view.post(new Runnable() {
            @Override
            public void run() {
                interpolteAmt = fakeScrollView.getHeight() / 9;
                scroll4Me.requestFocus();
                scroll4Me.requestFocusFromTouch();

                if(reloaded && notChanged){
                    scroll4Me.callOnClick();
                    notChanged = false;
                }
            }
        });


        ampmOverlay.bringToFront();


        View padding = new View(getActivity());
        LayoutParams paddingLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 100);
        paddingLayoutParams.setMargins(0, marginTop + twtfiveDp, 0, 0);
        padding.setLayoutParams(paddingLayoutParams);
        ampmNumList.addView(padding);
        //autoRefresh();



        return view;

    }

    float interpolate = 1;
    int interpolteAmt;
    boolean speedUp;
    public void moveToPM(){
        scrollHandlerUp.postDelayed(new Runnable() {
            @Override
            public void run() {
                cannotChange = true;
                if(speedUp) {
                    if (interpolate < interpolteAmt) {
                        interpolate *= 1.5;
                        for (int i = 0; i < numbers.size(); i++) {
                            numbers.get(i).setY(numbers.get(i).getY() + interpolate);
                            //Log.i("numbs", ""+ numbers.get(i).getText());
                        }
                    }else{
                        speedUp = false;
                    }
                }else{
                    if(interpolate > 1) {
                        interpolate /= 1.5;
                        for (int i = 0; i < numbers.size(); i++) {
                            numbers.get(i).setY(numbers.get(i).getY() + interpolate);
                        }
                    }else{
                        interpolate = 1;
                        speedUp = true;
                        cannotChange = false;
                        return;
                    }
                }
                moveToPM();
            }
        }, 10);
    }

    public void moveToAM(){
        scrollHandlerDown.postDelayed(new Runnable() {
            @Override
            public void run() {
                cannotChange = true;
                if(speedUp) {
                    if (interpolate < interpolteAmt) {
                        interpolate *= 1.5;
                        for (int i = 0; i < numbers.size(); i++) {
                            numbers.get(i).setY(numbers.get(i).getY() - interpolate);
                            //Log.i("numbs", ""+ numbers.get(i).getText());
                        }
                    }else{
                        speedUp = false;
                    }
                }else{
                    if(interpolate > 1) {
                        interpolate /= 1.5;
                        for (int i = 0; i < numbers.size(); i++) {
                            numbers.get(i).setY(numbers.get(i).getY() - interpolate);
                        }
                    }else{
                        interpolate = 1;
                        speedUp = true;
                        cannotChange = false;
                        return;
                    }
                }
                moveToAM();
            }
        }, 10);
    }

    public void scrollSlowerUp(){
        scrollHandlerUp.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scrollView.getScrollY() % linedUp != 0 && scrollView.getScrollY() < (scrollView.getChildAt(0).getHeight() - scrollView.getHeight())) {
                    if(scrollView.getScrollY() % 10 != linedUp % 10) {
                        scrollView.smoothScrollBy(0, -1);
                    }else{
                        scrollView.smoothScrollBy(0, -10);
                    }
                    //Log.i("scrollY", "Mod of scrollY = " + scrollView.getScrollY() % 270);

                }else{
                    if(scrollView.getScrollY() <= 0){
                        callBack.passAMPM(0);
                    }else{
                        callBack.passAMPM(1);
                    }
                }
                scrollSlowerUp();
            }
        }, 10);

    }

    public void scrollSlowerDown(){
        scrollHandlerDown.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scrollView.getScrollY() % linedUp != 0 && scrollView.getScrollY() < (scrollView.getChildAt(0).getHeight() + twtfiveDp - scrollView.getHeight())) {
                    if(scrollView.getScrollY() % 10 != linedUp % 10) {
                        scrollView.smoothScrollBy(0, 1);
                    }else{
                        scrollView.smoothScrollBy(0, 10);
                    }

                }else{
                    if(scrollView.getScrollY() <= 0){
                        callBack.passAMPM(0);
                    }else{
                        callBack.passAMPM(1);
                    }
                    return;
                }
                scrollSlowerDown();
            }
        }, 10);

    }

    public void scrollTo(){
        reloaded = true;
    }

    private static CallBacks callBack;

    public interface CallBacks{
        public void passAMPM(int ampm);
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

    public static SelectionMinute getInstance() {
        SelectionMinute f = new SelectionMinute();

        return f;
    }

    private void autoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Log.i("scrollY", "Scroll Y is: " + scrollView.getScrollY());

                //Log.i("scrollY", "height = " + (scrollView.getChildAt(0).getHeight() - scrollView.getHeight()));

                autoRefresh();
            }
        }, 1000);
    }
};
