package com.example.d3ths.clockapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;


import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Chris on 9/1/2016.
 */
public class SelectionMinute extends Fragment {
    private GestureDetector mGesture;
    Handler handler = new Handler();
    Handler scrollHandlerUp = new Handler();
    Handler scrollHandlerDown = new Handler();
    ScrollView scrollView;
    ArrayList<TextView> numbers;
    final int elements = 60;
    int[] svValue;
    int linedUp;
    float textSize;
    int twtfiveDp;
    int marginTop;
    int numberSpacer;
    int dp180;
    int minute;

    boolean reloaded = false;
    boolean notChanged = true;
    int scrollNum;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Resources r = getResources();

        //Pixel to dp values for different displays
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.density = getResources().getDisplayMetrics().density;
        switch((int)(displayMetrics.density * 160)){
            //Nexus 5x
            case(420):
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 103, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
                dp180 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, r.getDisplayMetrics());
                break;
            //HTC One M9
            case(480):
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                dp180 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 230, r.getDisplayMetrics());
                break;

            default:
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 103, r.getDisplayMetrics());
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
                dp180 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, r.getDisplayMetrics());
                break;
        }



        //When user is done scrolling the applicable method lines the scrollview up with a number
        View view = inflater.inflate(R.layout.selection_minute, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.numScrollView2);
        RelativeLayout minuteOverlay = (RelativeLayout) view.findViewById(R.id.minuteOverlay);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        requestDisallowParentInterceptTouchEvent(scrollView, true);
                        break;
                    case MotionEvent.ACTION_UP:
                        requestDisallowParentInterceptTouchEvent(scrollView, false);
                        //Log.i("scrollY", "Mod of scrollY = " + scrollView.getScrollY() % 270);
                        if (scrollView.getScrollY() % linedUp <= linedUp / 2) {
                            scrollSlowerUp();
                        } else {
                            scrollSlowerDown();
                        }
                        break;
                }
                //Log.i("scrollY", "Scroll Y is: " + scrollView.getScrollY());

                return false;
            }
        });

        //Putting each number into the scrollview
        numbers = new ArrayList<TextView>();
        RelativeLayout minuteNumList = (RelativeLayout) view.findViewById(R.id.minuteNumList);

        for (int i = -1; i < elements - 1; i++) {
            TextView textView = new TextView(getActivity());
            LayoutParams numLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (i < 9) {
                textView.setText("0" + Integer.toString(i + 1));

            } else {
                textView.setText(Integer.toString(i + 1));

            }
            numLayoutParams.setMargins(0, marginTop, 0, 0);

            textView.setLayoutParams(numLayoutParams);
            textView.setTextSize(textSize);
            numbers.add(textView);
            minuteNumList.addView(textView);
            marginTop += numberSpacer;
        }

        //In front tho
        minuteOverlay.bringToFront();

        //Padding at the bottom so it lines up
        View padding = new View(getActivity());
        LayoutParams paddingLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 100);
        paddingLayoutParams.setMargins(0, marginTop + twtfiveDp, 0, 0);
        padding.setLayoutParams(paddingLayoutParams);
        minuteNumList.addView(padding);


        //Each number's position on the screen WR to Scrollview
        svValue = new int[elements];
        view.post(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < elements; i++) {
                    TextView textView = numbers.get(i);
                    int[] onScreen = new int[2];
                    textView.getLocationOnScreen(onScreen);
                    svValue[i] = onScreen[1];
                    //Log.i("svValue", "" + svValue[i]);

                }

                if(reloaded && notChanged){
                    scrollView.smoothScrollTo(0, svValue[scrollNum] - dp180);
                    notChanged = false;
                }
            }
        });


        return view;

    }

    private void requestDisallowParentInterceptTouchEvent(View scrollView, boolean disallowIntercept) {
        while (scrollView.getParent() != null && scrollView.getParent() instanceof View) {
            if (scrollView.getParent() instanceof ScrollView) {
                scrollView.getParent().requestDisallowInterceptTouchEvent(disallowIntercept);
            }
            scrollView = (View) scrollView.getParent();
        }
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

                }else{
                    TextView selected = new TextView(getActivity());
                    for(int i = 0; i <svValue.length - 1; i++) {
                        if(scrollView.getScrollY() > 0) {
                            if (svValue[i] == scrollView.getScrollY() + dp180) {
                                selected = numbers.get(i);
                            }
                        }
                        else{
                            selected = numbers.get(0);
                            break;
                        }
                        if(scrollView.getScrollY() + dp180 >= svValue[svValue.length - 1]){
                            selected = numbers.get(numbers.size() - 1);
                            break;
                        }
                    }
                    try {
                        minute = Integer.parseInt(selected.getText().toString());
                        callBack.passMinute(minute);
                    }catch(NumberFormatException e){
                        Log.e("Unparsable", "There was a " +e +", scrolling UP b");
                    }
                    return;
                }
                scrollSlowerUp();
            }
        }, 10);

    }

    public void scrollSlowerDown(){
        scrollHandlerDown.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scrollView.getScrollY() % linedUp != 0 && scrollView.getScrollY() < (scrollView.getChildAt(0).getHeight() - scrollView.getHeight())) {
                    if(scrollView.getScrollY() % 10 != linedUp % 10) {
                        scrollView.smoothScrollBy(0, 1);
                    }else{
                        scrollView.smoothScrollBy(0, 10);
                    }

                }else{
                    TextView selected = new TextView(getActivity());
                    for(int i = 0; i <svValue.length - 1; i++) {
                        //Log.i("Comparing stuff", "svValue: " +svValue[i] +", yValue: " +(scrollView.getScrollY() + dp180));
                        if(scrollView.getScrollY() > 0) {
                            if (svValue[i] == scrollView.getScrollY() + dp180) {
                                selected = numbers.get(i);
                            }
                        }
                        else{
                            selected = numbers.get(0);
                            break;
                        }
                        if(scrollView.getScrollY() + dp180 >= svValue[svValue.length - 1]){
                            selected = numbers.get(numbers.size() - 1);
                            break;
                        }
                    }
                    try {
                        minute = Integer.parseInt(selected.getText().toString());
                        callBack.passMinute(minute);
                    }catch(NumberFormatException e){
                        Log.e("Unparsable", "There was a " +e +", scrolling DOWN b");
                    }
                    return;
                }
                scrollSlowerDown();
            }
        }, 10);

    }

    public void arrowScrollUp(){
        if(scrollView.getScrollY() + dp180 <= svValue[15]) {
            scrollView.smoothScrollTo(0, svValue[0] - dp180);
            callBack.passMinute(0);
        }else if(scrollView.getScrollY() + dp180 > svValue[15] && scrollView.getScrollY() + dp180 <= svValue[30]){
            scrollView.smoothScrollTo(0, svValue[15] - dp180);
            callBack.passMinute(15);
        }else if(scrollView.getScrollY() + dp180 > svValue[30] && scrollView.getScrollY() + dp180 <= svValue[45]){
            scrollView.smoothScrollTo(0, svValue[30] - dp180);
            callBack.passMinute(30);
        }else{
            scrollView.smoothScrollTo(0, svValue[45] - dp180);
            callBack.passMinute(45);
        }
    }
    public void arrowScrollDown(){
        if(scrollView.getScrollY() + dp180 < svValue[15]) {
            scrollView.smoothScrollTo(0, svValue[15] - dp180);
            callBack.passMinute(15);
        }else if(scrollView.getScrollY() + dp180 >= svValue[15] && scrollView.getScrollY() + dp180 < svValue[30]){
            scrollView.smoothScrollTo(0, svValue[30] - dp180);
            callBack.passMinute(30);
        }else if(scrollView.getScrollY() + dp180 >= svValue[30] && scrollView.getScrollY() + dp180 < svValue[45]){
            scrollView.smoothScrollTo(0, svValue[45] - dp180);
            callBack.passMinute(45);
        }else{
            scrollView.smoothScrollTo(0, svValue[59] - dp180);
            callBack.passMinute(59);
        }
    }
    public void checkAndMove(){
        if (scrollView.getScrollY() % linedUp <= linedUp / 2) {
            scrollSlowerUp();
        } else {
            scrollSlowerDown();
        }
    }

    public void scrollTo(int num){
        scrollNum = num;
        reloaded = true;
    }

    private static CallBacks callBack;

    public interface CallBacks{
        public void passMinute(int hour);
    }

    public static void setCallBack(CallBacks callBackk){
        callBack = callBackk;
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

                Log.i("scrollY", "height = " + (scrollView.getChildAt(0).getHeight() - scrollView.getHeight()));
                autoRefresh();
            }
        }, 1000);
    }
};
