package com.example.d3ths.clockapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Chris on 1/11/2017.
 */
public class DialogChecklist extends Dialog{

    public Activity c;
    public Dialog d;
    public ImageView ok;
    String msg;
    String[] list;
    String checked;
    CheckBox[] checkBoxes;

    public DialogChecklist(Activity a, String msg, String[] list, String checked) {
        super(a);
        this.c = a;
        this.msg = msg;
        this.list = list;
        this.checked = checked;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_checklist);
        ((TextView)findViewById(R.id.alertTitle)).setText(msg);
        ok = (ImageView) findViewById(R.id.alertOkButton);

        Resources r = c.getResources();
        int marginTop = 0;
        int marginTopChange = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, r.getDisplayMetrics());
        int marginL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        int marginR = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
        int textSize = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, r.getDisplayMetrics());
        Typeface sourceSans = Typeface.createFromAsset(c.getAssets(), "SourceSansProR.otf");

        RelativeLayout container = (RelativeLayout)findViewById(R.id.container);
        checkBoxes = new CheckBox[7];
        int evenOdd = 0;
        for(String i: list){
            View v = new View(getContext());
            RelativeLayout.LayoutParams lp1 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, marginTopChange);
            lp1.setMargins(0, marginTop, 0, 0);
            v.setLayoutParams(lp1);
            if(evenOdd % 2 == 0){
                v.setBackgroundColor(r.getColor(R.color.greyish));
            }else v.setBackgroundColor(r.getColor(R.color.whiteTaint));
            container.addView(v);

            TextView item = new TextView(getContext());
            RelativeLayout.LayoutParams lp2 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp2.setMargins(marginL, marginTop + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics()), 0, 0);
            item.setLayoutParams(lp2);
            item.setText(i);
            item.setTextSize(textSize);
            item.setTypeface(sourceSans);
            container.addView(item);

            final CheckBox checkBox = new CheckBox(new ContextThemeWrapper(getContext(), R.style.OrangeCheckBox));
            RelativeLayout.LayoutParams lp3 = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            lp3.setMargins(0, marginTop + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, r.getDisplayMetrics()), marginR, 0);
            lp3.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            checkBox.setLayoutParams(lp3);
            if(checked.charAt(evenOdd) == '1')checkBox.setChecked(true);
            container.addView(checkBox);
            checkBoxes[evenOdd] = checkBox;

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(checkBox.isChecked()){
                        checkBox.setChecked(false);
                    }else{
                        checkBox.setChecked(true);
                    }
                }
            });

            marginTop += marginTopChange;
            evenOdd++;
        }
    }

}
