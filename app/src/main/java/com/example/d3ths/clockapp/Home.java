package com.example.d3ths.clockapp;


import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.Typeface;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.Build;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextClock;
import android.widget.TextView;
import android.os.Handler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Chris on 8/30/2016.
 */
public class Home extends AppCompatActivity implements SelectionHour.CallBacks, SelectionMinute.CallBacks, HomeContent.CallBacks,
        SelectionAMPM.CallBacks, Alarms.Callbacks, Time.CallBacks, AlarmNew.Callbacks, AlarmEdit.Callbacks, AlarmsCurrent.Callbacks{

    FragmentManager manager;
    ArrayList<Alarm> alarms = new ArrayList<>();

    GestureDetector gestureDetector;
    Handler handler = new Handler();
    FragmentTransaction fragTran;
    FragmentTransaction otherScreens;
    HomeContent homeContentPage;
    Alarms alarmsPage;
    Time timePage;
    AlarmsCurrent alarmsCurrent;
    AlarmNew alarmNew;
    AlarmEdit alarmEdit;
    TextClock textClock;
    ImageView addaptableArrow;

    String currentView;
    int timeX = 593;
    int timeY = 20;
    int x;
    int y;

    @TargetApi(Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);

        manager = getSupportFragmentManager();
        homeContentPage = new HomeContent();
        fragTran = manager.beginTransaction();

        if(fragTran.isEmpty()) {
            fragTran.add(R.id.container, homeContentPage, "homeContent");
        }

        fragTran.commit();

        otherScreens = manager.beginTransaction();
        otherScreens.commit();
        alarmsPage = new Alarms();
        homeContentPage = new HomeContent();



        //ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color.lighterBlue));
        //getSupportActionBar().setBackgroundDrawable(colorDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }



        Typeface typeface = Typeface.createFromAsset(getAssets(), "SourceSansProR.otf");

        Calendar cal = Calendar.getInstance();
        final Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("K:mm a");
        date.setTimeZone(java.util.TimeZone.getTimeZone("GMT" + timeZone()));
        String localTime = date.format(currentLocalTime);



        View orangeBackground = (View)findViewById(R.id.orangeBackground);
        View whiteBackground = (View)findViewById(R.id.whiteBackground);


        textClock = new TextClock(this);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textClock.setTextSize(TypedValue.COMPLEX_UNIT_SP, 50);
        textClock.setTextColor(getResources().getColor(R.color.white));
        textClock.setTypeface(typeface);
        textClock.setLayoutParams(layoutParams);
        LinearLayout timeContainer = (LinearLayout)findViewById(R.id.timeContainer);
        timeContainer.addView(textClock);


        LinearLayout arrowContainer = (LinearLayout)findViewById(R.id.backArrowContainter);
        addaptableArrow = new ImageView(this);
        addaptableArrow.setBackground(getResources().getDrawable(R.drawable.arrow_pointing_up));
        addaptableArrow.setLayoutParams(layoutParams);
        addaptableArrow.setScaleX(0.15f);
        addaptableArrow.setScaleY(0.19f);
        addaptableArrow.setRotation(180f);
        addaptableArrow.setAlpha(0f);
        arrowContainer.addView(addaptableArrow);

        addaptableArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentView.equals("homePage"))return;

                switch(currentView){
                    case "alarmsPage":
                        alarmsPage.goBack();
                        break;
                    case "alarmsCurrentPage":
                        alarmsCurrent.goBack();
                        break;
                    case "newAlarmPage":
                        alarmNew.goBack();
                        break;
                    case "alarmEditPage":
                        alarmEdit.goBack();
                        break;

                    case "timePage":
                        timePage.goBack();
                        break;

                }
            }
        });


        ViewCompat.setTranslationZ(whiteBackground, 0.1f);
        ViewCompat.setTranslationZ(orangeBackground, 0.2f);
        ViewCompat.setTranslationZ(timeContainer, 0.3f);
        ViewCompat.setTranslationZ(arrowContainer, 0.3f);


        //doTheAutoRefresh();


    }




    //Variables for Movement
    Handler movementHandler = new Handler();
    int count;
    float interpolate = 1;
    int interpolateAmt = 60;
    double interpolateRate = 1.4;
    boolean cannotChange = false;
    boolean speedUp = true;
    float arrowRotation = 180f;
    float arrowAlpha = 0f;
    float interpolate2 = 1;
    int interpolateAmt2 = 120;
    double interpolateRate2 = 1.392;

    //Slides the time left
    public void timeSlideLeft(){
        movementHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cannotChange = true;
                if(speedUp) {
//                    if(arrowAlpha != 0){
//                        arrowAlpha -= 0.05f;
//                        addaptableArrow.setAlpha(arrowAlpha);
//                    }else{
//                        arrowAlpha = 0;
//                    }
                    if(arrowRotation > 90){
                        arrowRotation -= 4;
                        addaptableArrow.setRotation(arrowRotation);
                    }
                    if (interpolate < interpolateAmt) {
                        interpolate *= interpolateRate;
                        textClock.setX(textClock.getX() - interpolate);
                    }
                    if (interpolate2 < interpolateAmt2) {
                        interpolate2 *= interpolateRate2;
                        addaptableArrow.setX(addaptableArrow.getX() + interpolate2);
                    }else{
                        speedUp = false;
                    }
                }else{
                    if(arrowAlpha != 1){
                        arrowAlpha += 0.05f;
                        addaptableArrow.setAlpha(arrowAlpha);
                    }else{
                        arrowAlpha = 1.0f;
                    }
                    if(arrowRotation > 90){
                        arrowRotation -= 5;
                        addaptableArrow.setRotation(arrowRotation);
                    }
                    if(interpolate > 1) {
                        interpolate /= interpolateRate;
                        textClock.setX(textClock.getX() - interpolate);
                        interpolate2 /= interpolateRate2;
                        addaptableArrow.setX(addaptableArrow.getX() + interpolate2);
                    }else{
                        arrowRotation = 90;
                        addaptableArrow.setRotation(arrowRotation);
                        arrowAlpha = 1;
                        addaptableArrow.setAlpha(arrowAlpha);
                        if(count < 1){
                            count++;
                        }else {
                            count = 0;
                            cannotChange = false;
                            interpolate = 1;
                            interpolate2 = 1;
                            speedUp = true;
                            return;
                        }
                    }
                }
                timeSlideLeft();
            }
        }, 10);
    }
    //Slides the time right
    public void timeSlideRight(){
        movementHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                cannotChange = true;
                if(speedUp) {
                    if(arrowAlpha != 0){
                        arrowAlpha -= 0.05f;
                        addaptableArrow.setAlpha(arrowAlpha);
                    }else{
                        arrowAlpha = 0;
                    }
                    if(arrowRotation < 180){
                        arrowRotation += 4;
                        addaptableArrow.setRotation(arrowRotation);
                    }
                    if (interpolate < interpolateAmt) {
                        interpolate *= interpolateRate;
                        textClock.setX(textClock.getX() + interpolate);
                    }
                    if (interpolate2 < interpolateAmt2) {
                        interpolate2 *= interpolateRate2;
                        addaptableArrow.setX(addaptableArrow.getX() - interpolate2);
                    }else{
                        speedUp = false;
                    }
                }else{
//                    if(arrowAlpha != 1){
//                        arrowAlpha += 0.05f;
//                        addaptableArrow.setAlpha(arrowAlpha);
//                    }else{
//                        arrowAlpha = 1.0f;
//                    }
                    if(arrowAlpha != 0){
                        arrowAlpha -= 0.05f;
                        addaptableArrow.setAlpha(arrowAlpha);
                    }else{
                        arrowAlpha = 0;
                    }
                    if(arrowRotation < 180){
                        arrowRotation += 5;
                        addaptableArrow.setRotation(arrowRotation);
                    }
                    if(interpolate > 1) {
                        interpolate /= interpolateRate;
                        textClock.setX(textClock.getX() + interpolate);
                        interpolate2 /= interpolateRate2;
                        addaptableArrow.setX(addaptableArrow.getX() - interpolate2);
                    }else{
                        arrowRotation = 180;
                        addaptableArrow.setRotation(arrowRotation);
                        arrowAlpha = 0;
                        addaptableArrow.setAlpha(0f);
                        if(count < 1){
                            count++;
                        }else {
                            count = 0;
                            cannotChange = false;
                            interpolate = 1;
                            interpolate2 = 1;
                            speedUp = true;
                            return;
                        }
                    }
                }
                timeSlideRight();
            }
        }, 10);
    }

    //Turns the arrow left
    public void turnArrowLeft() {
        movementHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(arrowAlpha != 1){
                    arrowAlpha += 0.07f;
                    addaptableArrow.setAlpha(arrowAlpha);
                }else{
                    arrowAlpha = 1.0f;
                }
                if(arrowRotation < 225){
                    arrowRotation += 8;
                    addaptableArrow.setRotation(arrowRotation);
                }else{
                    if(arrowRotation < 270){
                        arrowRotation += 10;
                        addaptableArrow.setRotation(arrowRotation);
                    }else{
                        arrowAlpha = 1.0f;
                        addaptableArrow.setAlpha(arrowAlpha);
                        addaptableArrow.setRotation(270f);
                        return;
                    }
                }
                turnArrowLeft();
            }
        }, 10);
    }
    //Turns it back to pointing down after the method above is called
    public void turnArrowFromLeft() {
        movementHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(arrowAlpha != 0f){
                    arrowAlpha -= 0.07f;
                    addaptableArrow.setAlpha(arrowAlpha);
                }else{
                    arrowAlpha = 0f;
                }
                if(arrowRotation > 225){
                    arrowRotation -= 8;
                    addaptableArrow.setRotation(arrowRotation);
                }else{
                    if(arrowRotation > 180){
                        arrowRotation -= 10;
                        addaptableArrow.setRotation(arrowRotation);
                    }else{
                        arrowAlpha = 0f;
                        addaptableArrow.setAlpha(arrowAlpha);
                        addaptableArrow.setRotation(180f);
                        return;
                    }
                }
                turnArrowFromLeft();
            }
        }, 10);
    }


    //Callbacks
    //HomeContent
    @Override
    public void goToView(String view){
        currentView = view;
        switch (view){
            case "alarmsPage":
                turnArrowLeft();
                break;
            case "timePage":
                timeSlideLeft();
                break;
        }
    }
    @Override
    public void passFragment(Fragment frag){
        switch(frag.getTag()){
            case "homePageFragment":

                break;
            case "alarmFragment":
                break;
            case "alarmsCurrentFrag":
                alarmsCurrent = (AlarmsCurrent) frag;
                break;
            case "newAlarmFrag":
                alarmNew = (AlarmNew) frag;
                break;
            case "alarmEditFrag":
                alarmEdit = (AlarmEdit) frag;
                break;

            case "timeFragment":
                timePage = (Time) frag;
                break;
        }
    }

    //Alarms
    @Override
    public void leaveFromAlarms(){
        turnArrowFromLeft();
        currentView = "homePage";
    }
    @Override
    public void alarmGoToView(Fragment frag, String view){
        passFragment(frag);
        goToView(view);
    }

    //Current Alarms
    @Override
    public void leaveFromAlarmsCurrent(){
        currentView = "alarmsPage";
    }

    //New Alarm

        @Override
        public void changeValues(Alarm alarm){
            ((SelectionHour)getSupportFragmentManager().findFragmentByTag("SelectionHour")).scrollTo(alarm.hour);
            ((SelectionMinute)getSupportFragmentManager().findFragmentByTag("SelectionMinute")).scrollTo(alarm.minute);
            if(alarm.ampm == 1)((SelectionAMPM) getSupportFragmentManager().findFragmentByTag("SelectionAMPM")).scrollTo();
        }

        @Override
        public void returnFromNewAlarm(){
            currentView = "alarmPage";
        }

        @Override
        public void newAlarmGoToView(Fragment frag, String view){
            passFragment(frag);
            goToView(view
            );
        }

        //Hour
        @Override
        public void passHour(int hr){
            AlarmNew frag = (AlarmNew)
                    getSupportFragmentManager().findFragmentByTag("newAlarmFrag");

            frag.setHour(hr);
        }

        //Minute
        @Override
        public void passMinute(int min){
            AlarmNew frag = (AlarmNew)
                    getSupportFragmentManager().findFragmentByTag("newAlarmFrag");
            frag.setMinute(min);
        }
        //AMPM
        @Override
        public void passAMPM(int ampm){
            AlarmNew frag = (AlarmNew)
                    getSupportFragmentManager().findFragmentByTag("newAlarmFrag");
            frag.setAMPM(ampm);
        }

        //For Arrows in Alarm Slider
        @Override
        public void hrScrollUp(){
            SelectionHour frag = (SelectionHour)
                    getSupportFragmentManager().findFragmentByTag("SelectionHour");
            frag.arrowScrollUp();
        }
        @Override
        public void hrScrollDown(){
            SelectionHour frag = (SelectionHour)
                    getSupportFragmentManager().findFragmentByTag("SelectionHour");
            frag.arrowScrollDown();
        }
        @Override
        public void hrCheckAndMove(){
            SelectionHour frag = (SelectionHour)
                    getSupportFragmentManager().findFragmentByTag("SelectionHour");
            frag.checkAndMove();
        }
        @Override
        public void minScrollUp(){
            SelectionMinute frag = (SelectionMinute)
                    getSupportFragmentManager().findFragmentByTag("SelectionMinute");
            frag.arrowScrollUp();
        }
        @Override
        public void minScrollDown(){
            SelectionMinute frag = (SelectionMinute)
                    getSupportFragmentManager().findFragmentByTag("SelectionMinute");
            frag.arrowScrollDown();
        }
        @Override
        public void minCheckAndMove(){
            SelectionMinute frag = (SelectionMinute)
                    getSupportFragmentManager().findFragmentByTag("SelectionMinute");
            frag.checkAndMove();
        }

    //Edit Alarm
    @Override
    public void returnFromEditAlarm(Fragment frag, String view){
        goToView(view);
        passFragment(frag);
    }
    @Override
    public void newAlarmReload(Alarm alarm){
        alarmNew.setReloaded(alarm);
    }
    @Override
    public void alarmEditFinish(String view){
        if(view.equals("newAlarmPage")){
            currentView = "homePage";
            turnArrowFromLeft();
        }
    }

    //Time
    @Override
    public void leaveFromTime(){
        currentView = "homePage";
        timeSlideRight();
    }



    public static String timeZone()
    {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        String   timeZone = new SimpleDateFormat("Z").format(calendar.getTime());
        return timeZone.substring(0, 3) + ":"+ timeZone.substring(3, 5);
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar cal = Calendar.getInstance();
                Date currentLocalTime = cal.getTime();
                DateFormat date = new SimpleDateFormat("K:mm a");
                date.setTimeZone(java.util.TimeZone.getTimeZone("GMT" + timeZone()));
                String localTime = date.format(currentLocalTime);
                TextView time = (TextView) findViewById(R.id.time);
                //time.setText(localTime);


                doTheAutoRefresh();
            }
        }, 10);
    }

    @Override
    public void onRestart(){
        super.onRestart();
        try {
            fragTran.remove(homeContentPage);
        }catch(Exception e){
            Log.println(Log.ERROR ,"err","The error was " + e);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            fragTran.remove(homeContentPage);
        }catch(Exception e){
            Log.println(Log.ERROR ,"err","The error was " + e);
        }


    }

    @Override
    public void onBackPressed(){
        finish();
        System.exit(0);

    }

}
