package com.example.d3ths.clockapp;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
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
public class SelectionHour extends Fragment {
    private GestureDetector mGesture;
    Handler handler = new Handler();
    Handler scrollHandlerUp = new Handler();
    Handler scrollHandlerDown = new Handler();
    ScrollView scrollView;
    int[] svValue;
    ArrayList<TextView> numbers;
    final int elements = 12;
    int linedUp;
    private int hour;
    int dp180;
    int numberSpacer;

    boolean reloaded = false;
    boolean notChanged = true;
    int scrollNum;

    private ImageButton aArrowUp;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Resources r = getResources();

        float textSize;
        int offsetLeft;
        int twtfiveDp;
        int marginTop;
        //Pixel to dp values for different displays
        DisplayMetrics displayMetrics = new DisplayMetrics();
        displayMetrics.density = getResources().getDisplayMetrics().density;
        switch((int)(displayMetrics.density * 160)){
            //Nexus 5x
            case (420):
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
                offsetLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 103, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                dp180 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 240, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());;
                break;
            //HTC One M9
            case (480):
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                offsetLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 90, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                dp180 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 228, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());;
                break;

            default:
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, r.getDisplayMetrics());
                offsetLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, r.getDisplayMetrics());
                linedUp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 103, r.getDisplayMetrics());
                numberSpacer = linedUp;
                twtfiveDp = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 25, r.getDisplayMetrics());
                dp180 = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 250, r.getDisplayMetrics());
                marginTop = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, r.getDisplayMetrics());
                break;
        }

        //When user is done scrolling the applicable method lines the scrollview up with a number
        View view = inflater.inflate(R.layout.selection_hour, container, false);
        scrollView = (ScrollView) view.findViewById(R.id.numScrollView);
        RelativeLayout hourOverlay = (RelativeLayout) view.findViewById(R.id.hourOverlay);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        requestDisallowParentInterceptTouchEvent(scrollView, true);
                        break;
                    case MotionEvent.ACTION_UP:
                        requestDisallowParentInterceptTouchEvent(scrollView, false);
                        if (scrollView.getScrollY() % linedUp <= linedUp / 2) {
                                scrollSlowerUp();
                        } else {
                                scrollSlowerDown();
                        }
                        break;
                }

                return false;
            }
        });


        // Create each number
        numbers = new ArrayList<TextView>();
        RelativeLayout hourNumList = (RelativeLayout) view.findViewById(R.id.hourNumList);

        for (int i = 0; i < elements; i++) {
            TextView textView = new TextView(getActivity());
            LayoutParams numLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            if (i < 9) {
                numLayoutParams.setMargins(offsetLeft, marginTop, 0, 0);
            } else {
                numLayoutParams.setMargins(0, marginTop, 0, 0);
            }

            textView.setLayoutParams(numLayoutParams);
            textView.setText(Integer.toString(i + 1));
            textView.setTextSize(textSize);
            textView.setTag(i + 1);
            numbers.add(textView);
            hourNumList.addView(textView);
            marginTop += numberSpacer;

        }
        //In front tho
        hourOverlay.bringToFront();


        //Blank space at the bottom so last number has room
        View padding = new View(getActivity());
        LayoutParams paddingLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, 100);
        paddingLayoutParams.setMargins(0, marginTop + twtfiveDp, 0, 0);
        padding.setLayoutParams(paddingLayoutParams);
        hourNumList.addView(padding);

        //autoRefresh();

        //Navigation Arrows



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
                    scrollView.smoothScrollTo(0, svValue[scrollNum - 1] - dp180);
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
                        hour = Integer.parseInt(selected.getText().toString());
                        callBack.passHour(hour);
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
                        if(scrollView.getScrollY() + dp180 > svValue[svValue.length - 2]){
                            selected = numbers.get(numbers.size() - 1);
                            break;
                        }
                    }
                    try {
                        hour = Integer.parseInt(selected.getText().toString());
                        callBack.passHour(hour);
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
        if(scrollView.getScrollY() + dp180 >= svValue[6]) {
            scrollView.smoothScrollTo(0, svValue[5] - dp180);
            callBack.passHour(6);
        }else{
            scrollView.smoothScrollTo(0, 0);
            callBack.passHour(1);
        }
        //scrollView.smoothScrollBy(0, -1);
    }
    public void arrowScrollDown(){
        if(scrollView.getScrollY() + dp180 < svValue[5]) {
            scrollView.smoothScrollTo(0, svValue[5] - dp180);
            callBack.passHour(6);
        }else{
            scrollView.smoothScrollTo(0, svValue[svValue.length - 1]);
            callBack.passHour(12);
        }
        //scrollView.smoothScrollBy(0, 1);
    }
    public void checkAndMove(){
        if (scrollView.getScrollY() % linedUp <= linedUp / 2) {
            scrollSlowerUp();
        } else {
            scrollSlowerDown();
        }
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

    public void scrollTo(int num){
        scrollNum = num;
        reloaded = true;
    }

    private CallBacks callBack;

    public interface CallBacks{
        public void passHour(int hour);
    }

    public void setCallBack(CallBacks callBack){
        this.callBack = callBack;
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

    public static SelectionHour getInstance() {
        SelectionHour f = new SelectionHour();

        return f;
    }
}
